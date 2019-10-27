package controllers;

import algos.LinearSieveAlgorithm;
import algos.MillerRabinAlgorithm;
import algos.PrimeGenerator;
import algos.SieveAlgorithm;
import algos.TrialDivision;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.MongoConnection;
import database.RedisConnection;
import io.javalin.http.Handler;
import java.util.ArrayList;
import java.util.HashMap;
import models.Prime;
import models.RequestBody;
import redis.clients.jedis.Jedis;

public class PrimeController {

  /**
   * Initialize the algorithm based on input.
   */
  private static PrimeGenerator selectAlgorithm(RequestBody.Algorithms algorithm) {
    PrimeGenerator primeGenerator;
    switch (algorithm) {
      case Sieve:
        primeGenerator = new SieveAlgorithm();
        break;
      case LinearSieve:
        primeGenerator = new LinearSieveAlgorithm();
        break;
      case MillerRabin:
        primeGenerator = new MillerRabinAlgorithm();
        break;
      default:
        primeGenerator = new TrialDivision();
    }
    return primeGenerator;
  }

  public static Handler generatePrimesInRange = ctx -> {
    // validate & parse body
    RequestBody body = ctx.bodyValidator(RequestBody.class)
        .check(requestBody -> requestBody.getStart() <= requestBody.getEnd(),
            "End has to be greater than or equal start range.").get();

    long timeBefore, timeAfter;
    int primesSize;

    // redis key
    String redisKey = body.getAlgorithm()
        + "/"
        + body.getStart()
        + "/"
        + body.getEnd();

    // check cache and calc execution time
    timeBefore = System.currentTimeMillis();
    String cacheResponse;
    try (Jedis redisClient = RedisConnection.getInstance().getRedisPool().getResource()) {
      cacheResponse = redisClient.get(redisKey);
    } catch (Exception e) {
      // in case of redis error nullify the cache and calculate
      cacheResponse = null;
    }
    timeAfter = System.currentTimeMillis();

    if (cacheResponse == null) {
      // generate the prime
      PrimeGenerator primeGenerator = selectAlgorithm(body.getAlgorithm());

      // calculate execution time
      timeBefore = System.currentTimeMillis();
      ArrayList<Integer> primes = primeGenerator.generatePrimes(body.getStart(), body.getEnd());
      timeAfter = System.currentTimeMillis();

      // cache to redis
      try (Jedis redisClient = RedisConnection.getInstance().getRedisPool().getResource()) {
        redisClient.set(redisKey, String.valueOf(primes.size()));
      } catch (Exception e) {
        // don't halt if redis not working
      }

      primesSize = primes.size();
    } else {
      // size is now from cache
      primesSize = Integer.parseInt(cacheResponse);
    }

    // create object & insert to database
    Prime prime = new Prime(String.valueOf(body.getAlgorithm()), body.getStart(), body.getEnd(),
        primesSize, timeAfter - timeBefore,
        java.time.LocalDateTime.now());

    try {
      // connect to db & collection & insert record
      MongoDatabase database = MongoConnection.getInstance().getDatabase();
      MongoCollection<Prime> collection = database.getCollection("primes", Prime.class);
      collection.insertOne(prime);
    } catch (MongoException e) {
      // return response without saving to db
    }

    // create response
    HashMap<String, Object> response = new HashMap<>();
    response.put("data", prime);
    ctx.json(response);
  };
}
