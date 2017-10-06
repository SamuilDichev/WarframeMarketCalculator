package util.persistence;

/**
 * Thrown when a database is required but not present in the configuration.
 *
 * Created by Samuil.
 *
 * @author Samuil Dichev
 */
public class MissingDatabaseException extends Exception {

  public MissingDatabaseException() {

  }

  MissingDatabaseException(String message) {
    super(message);
  }
}
