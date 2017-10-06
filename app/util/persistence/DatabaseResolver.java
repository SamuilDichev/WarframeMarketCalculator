package util.persistence;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves (and caches) class name -> database name mappings from the persistence configuration.
 * <p>
 * Created by Samuil.
 *
 * @author Samuil Dichev
 */
class DatabaseResolver {
  private static final Config conf = ConfigFactory.load();
  private static final DatabaseResolver INSTANCE = new DatabaseResolver();
  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseResolver.class);
  private Map<Class<?>, String> databases;
  private static final String NOT_FOUND = "DB_NAME_NOT_FOUND";

  static DatabaseResolver getInstance() {
    return INSTANCE;
  }

  private DatabaseResolver() {
    databases = new ConcurrentHashMap<>();
  }

  /**
   * Finds a database name from a Class
   *
   * @param klass Class for which to find database name
   * @return Database name
   */
  public String resolve(Class<?> klass) {
    String databaseName;

    if (!databases.containsKey(klass)) {
      Class<?> currentClass = klass;

      // Keep following up the class inheritance tree until either we hit null, Object, or a
      // configuration variable exists for a class somewhere in the tree.
      while (currentClass != null && !Object.class.equals(currentClass)
              && !configHas(propertyName(currentClass))) {
        currentClass = currentClass.getSuperclass();
      }

      // Calling getSuperclass on an interface (e.g. java.util.Map) returns null.
      // In the case of null, use the default database i.e. Object.
      if (currentClass == null) {
        currentClass = Object.class;
      }

      // Use the configured database name for the current class
      databaseName = Configuration.root().getString(propertyName(currentClass), "main");

      if (databaseName == null) {
        LOGGER.error("Could not find database value to use for {}", currentClass);
        // We don't go any further - return a String clearly indicating that a problem was found
        return NOT_FOUND;
      }

      // Prevent future inheritance tree traversal by caching this <original class> -> database
      // mapping
      databases.put(klass, databaseName);
    } else {
      databaseName = databases.get(klass);
    }

    return databaseName;
  }

  /**
   * Gets the configuration property name for the entry defining which database a given class should
   * use
   *
   * @param klass Class for which to find configuration property name
   * @return Configuration property name
   */
  private String propertyName(Class<?> klass) {
    return String.format("models.%s.db", klass.getCanonicalName());
  }

  /**
   * Checks if application.conf containts a certain config key.
   *
   * @param property The key to check for
   * @return true if it contains it, false otherwise.
   */
  private boolean configHas(String property) {
    return conf.hasPath(property);
  }
}