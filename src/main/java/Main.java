import com.mongodb.client.MongoDatabase;
import controllers.PrimeController;
import database.MongoConnection;
import database.RedisConnection;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.swagger.v3.oas.models.info.Info;
import java.util.HashMap;

public class Main {

  private static OpenApiOptions getOpenApiOptions() {
    Info applicationInfo = new Info()
        .version("1.0")
        .description("Prime Generator API");
    return new OpenApiOptions(applicationInfo).path("/docs");
  }

  public static void main(String[] args) {
    Javalin app = Javalin.create(config -> {
      config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
    }).start(7000);

    // app routes
    app.post("/", PrimeController.generatePrimesInRange);

    /*
     * Handle BadRequestResponse Exception.
     * Thrown when failed to parse the body object.
     */
    app.exception(BadRequestResponse.class, (e, ctx) -> {
      // create response object
      HashMap<String, String[]> response = new HashMap<>();
      String[] errors = new String[]{e.getMessage()};
      response.put("errors", errors);

      ctx.status(400).json(response);
    });

    /*
     * Handle Generic Errors.
     */
    app.exception(Exception.class, (e, ctx) -> {
      // create response object
      HashMap<String, String[]> response = new HashMap<>();
      String[] errors = new String[]{e.getMessage()};
      response.put("errors", errors);

      ctx.status(500).json(response);
    });
  }
}
