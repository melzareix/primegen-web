package database;

import io.github.cdimascio.dotenv.Dotenv;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Represents the connection to Redis service.
 */
public class RedisConnection {

  private static RedisConnection instance;

  /**
   * Connection to Redis service.
   */
  private JedisPool redisPool;

  private RedisConnection() {
    Dotenv dotenv = Dotenv.load();

    String host = dotenv.get("REDIS_HOST");
    int port = Integer.parseInt(dotenv.get(("REDIS_PORT")));

    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxTotal(Integer.parseInt(dotenv.get(("REDIS_THREAD_POOL_COUNT"))));

    redisPool = new JedisPool(jedisPoolConfig, host, port);
  }

  private static class RedisHelper {

    private static final RedisConnection INSTANCE = new RedisConnection();
  }

  public static RedisConnection getInstance() {
    return RedisHelper.INSTANCE;
  }

  /**
   * Returns the connection to the Redis service.
   *
   * @return The Redis connection (client)
   */
  public JedisPool getRedisPool() {
    return redisPool;
  }
}
