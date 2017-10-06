package util.persistence;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.jongo.Jongo;
import org.jongo.Mapper;
import org.jongo.marshall.jackson.JacksonMapper.Builder;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * One helper class for getting connections to multiple databases.
 * <p>
 * Created by Samuil.
 *
 * @author Samuil Dichev
 */
public final class MongoHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(MongoHelper.class);
  private static final Mapper MAPPER = new Builder().registerModule(new JodaModule()).build();
  private static final Config conf = ConfigFactory.load();
  private static final String PACKAGES = "models";
  private static final MongoHelper INSTANCE = new MongoHelper();
  private static Map<String, Jongo> connections = new ConcurrentHashMap<>();

  /**
   * The Morphia management class, used primarily to get the datastore.
   */
  private final Morphia morphia;

  /**
   * The Morphia datastore.
   */
  private final Datastore datastore;

  public static MongoHelper getInstance() {
    return INSTANCE;
  }

  private MongoHelper() {
    morphia = initMorphia();

    Datastore datastore = null;
    try {
      datastore = createDatastore();
    } catch (MissingDatabaseException e) {
      LOGGER.error("Unable to create data store", e);
    }

    this.datastore = datastore;
  }

  private Morphia initMorphia() {
    Morphia morphia = new Morphia();

    // Find out from the configuration which packages should be mapped with Morphia
    for (String pkg : getMappedPackages()) {
      // Map the package, ignore invalid classes.
      morphia.mapPackage(pkg, true);
    }

    return morphia;
  }

  private Datastore createDatastore() throws MissingDatabaseException {
    String databaseName = DatabaseResolver.getInstance().resolve(Object.class);
    MongoClientURI uri = getURI(databaseName);
    MongoClient mongoClient = new MongoClient(uri);

    return morphia.createDatastore(mongoClient, uri.getDatabase());
  }

  /**
   * Retrieves the named database, which will be returned from the internal connection map if it is
   * already connected, otherwise try to connect to the database and store in the connection map.
   *
   * @param name database name
   * @return a Jongo DB
   */
  public Jongo getDatabase(String name) {
    LOGGER.debug("Retrieving database: {}", name);

    Jongo database = null;

    if (!connections.containsKey(name)) {
      LOGGER.debug("{} is not already connected", name);
      try {
        database = new Jongo(connect(name), MAPPER);
        connections.put(name, database);
      } catch (MissingDatabaseException e) {
        LOGGER.error("Couldn't retrieve database: {}", name, e);
      }
    } else {
      LOGGER.debug("{} is already connected", name);

      database = connections.get(name);
    }

    return database;
  }

  public Morphia getMorphia() {
    return morphia;
  }

  /**
   * @return the Morphia datastore.
   */
  public Datastore getDatastore() {
    return datastore;
  }

  public <T> String getDatabaseUri(Class<T> type) {
    String name = DatabaseResolver.getInstance().resolve(type);
    String key = propertyName(name);
    return Configuration.root().getString(key);
  }

  /**
   * We permit this method to use a deprecated Mongo API as Jongo still requires it.
   *
   * @param databaseName database to connect to
   * @return a DB
   * @throws MissingDatabaseException If database cannot be found
   */
  @SuppressWarnings("deprecation")
  private DB connect(String databaseName) throws MissingDatabaseException {
    MongoClientURI uri = getURI(databaseName);

    return connect(uri).getDB(uri.getDatabase());
  }

  private MongoClientURI getURI(String databaseName) throws MissingDatabaseException {
    String uri = conf.getString("db.main");

    if (uri != null) {
      return new MongoClientURI(uri);
    } else {
      throw new MissingDatabaseException(databaseName);
    }
  }

  private MongoClient connect(MongoClientURI uri) {
    return new MongoClient(uri);
  }

  private String propertyName(String database) {
    return String.format("db.%s", database);
  }

  private Collection<String> getMappedPackages() {
    return Arrays.asList(PACKAGES.split(","));
  }
}
