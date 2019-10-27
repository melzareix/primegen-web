package database;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ConnectionPoolSettings.Builder;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Objects;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;


/**
 * Singleton Class to connect to MongoDB Server.
 */
public class MongoConnection {

  private static MongoConnection mongoConnection = null;

  private MongoClient mongoClient;
  private MongoDatabase mongoDatabase;

  public static MongoConnection getInstance() {
    if (mongoConnection == null) {
      mongoConnection = new MongoConnection();
    }
    return mongoConnection;
  }

  private MongoConnection() {
    Dotenv dotenv = Dotenv.load();

    // Parse Environment Variables
    String dbName = dotenv.get("DATABASE_NAME");
    int minPoolSize = Integer.parseInt(Objects.requireNonNull(dotenv.get("DB_POOL_MINSIZE")));
    int maxPoolSize = Integer.parseInt(Objects.requireNonNull(dotenv.get("DB_POOL_MAXSIZE")));

    // Configure DB to use POJO
    // https://mongodb.github.io/mongo-java-driver/3.11/driver/getting-started/quick-start-pojo/

    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    // Create mongo client with pooling & pojo settings
    MongoClientSettings settings = MongoClientSettings
        .builder()
        .codecRegistry(pojoCodecRegistry)
        .applyToConnectionPoolSettings(new Block<Builder>() {
          @Override
          public void apply(Builder builder) {
            builder
                .minSize(minPoolSize)
                .maxSize(maxPoolSize);
          }
        })
        .build();

    // connect to mongo server & database
    mongoClient = MongoClients.create(settings);
    mongoDatabase = mongoClient.getDatabase(dbName);
  }

  public MongoDatabase getDatabase() {
    return mongoDatabase;
  }
}
