import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Application;
import play.GlobalSettings;

/**
 * Created by Samuil.
 *
 * @author Samuil Dichev
 */
@SuppressWarnings("deprecation")
public class Global extends GlobalSettings {
  private static final Logger LOGGER = LoggerFactory.getLogger(Global.class);
  private static final String DB_KEY = "db.main";

  @Override
  public void beforeStart(Application app) {
    Thread.currentThread().setName("Global");
  }

  @Override
  public void onStart(Application app) {
    LOGGER.info("Starting application...");

    // Log database connection info
    String DB = app.configuration().getString(DB_KEY);
    if (DB == null) {
      LOGGER.error("No database configured in application.conf. Stopping application...");
      System.exit(1);
    } else {
      LOGGER.info("Connecting to database {}", DB.substring(DB.lastIndexOf("@") + 1));
    }
  }
}
