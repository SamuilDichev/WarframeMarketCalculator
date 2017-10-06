package util.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class JsonHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);
  private static final ObjectMapper MAPPER;

  static {
    MAPPER = new ObjectMapper();

    MAPPER.registerModule(ObjectIdSerializer.getModule());
    MAPPER.registerModule(new JodaModule());

    MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    MAPPER.configure(MapperFeature.AUTO_DETECT_GETTERS, false);
    MAPPER.configure(MapperFeature.AUTO_DETECT_SETTERS, false);
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    MAPPER.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(
            JsonAutoDetect.Visibility.ANY));
  }

  /**
   * Returns the ObjectMapper instance, for extended use cases
   */
  public static ObjectMapper getObjectMapper() {
    return MAPPER;
  }

  /**
   * Updates the given object with the supplied JSON and returns the status as to whether or not an
   * update actually succeeded.
   *
   * @param object the object to update
   * @param json   the string of json to update the object with
   * @return status(true=success, false=failure)
   */
  public static boolean update(Object object, String json) {
    boolean success = true;

    try {
      MAPPER.readerForUpdating(object).readValue(json);
    } catch (IOException e) {
      LOGGER.error("Could not update value with JSON: {}", json, e);
      success = false;
    }

    return success;
  }

  /**
   * Serialises the given object into a JSON String
   *
   * @param obj: the object to be serialised
   * @return String representing the object in JSON form
   */
  public static String toJson(Object obj) {
    try {
      return MAPPER.writeValueAsString(obj);
    } catch (IOException e) {
      LOGGER.error("Error mapping object to String", e);
      return "{}";
    }
  }

  /**
   * Deserialises the contents of the given File into an instance of the given type
   *
   * @param f    a file
   * @param type a type
   * @param <T>
   * @return
   */
  public static <T> T fromJson(File f, Class<T> type) {
    T mappedObject = null;

    try {
      mappedObject = MAPPER.readValue(f, type);
    } catch (IOException e) {
      LOGGER.error("Couldn't map JSON from file", e);
    }

    return mappedObject;
  }

  /**
   * Takes JSON as a String and maps (using the Jackson ObjectMapper) the JSON to an Object of the
   * desired type.
   *
   * @param json:   the JSON in String form
   * @param asType: a Class representing the type of object to be returned.
   * @return an Object of type asType, or null on failure.
   */
  public static <T> T fromJson(String json, Class<T> asType) {
    T mappedObject = null;

    try {
      mappedObject = MAPPER.readValue(json, asType);
    } catch (IOException e) {
      LOGGER.error("Exception mapping JSON to object", e);
    }

    return mappedObject;
  }

  /**
   * The same as the fromJson(String, Class) method, only this one takes a TypeReference
   *
   * @param json          a string of json
   * @param typeReference a type reference
   * @param <T>
   * @return
   */
  public static <T> T fromJson(String json, TypeReference<T> typeReference) {
    T mappedObject = null;

    try {
      mappedObject = MAPPER.readValue(json, typeReference);
    } catch (IOException e) {
      LOGGER.error("Exception mapping JSON to object", e);
    }

    return mappedObject;
  }

  /**
   * The same as the fromJson(File, Class) method, only this one takes a TypeReference
   *
   * @param f             a file
   * @param typeReference a type reference
   * @param <T>
   * @return
   */
  public static <T> T fromJson(File f, TypeReference<T> typeReference) {
    T mappedObject = null;

    try {
      mappedObject = MAPPER.readValue(f, typeReference);
    } catch (IOException e) {
      LOGGER.error("Couldn't map JSON from file", e);
    }

    return mappedObject;
  }

  /**
   * Takes JSON as a {@link InputStream} and maps (using the Jackson ObjectMapper) the JSON to an Object of the
   * desired type.
   *
   * @param is:     the JSON {@link InputStream}
   * @param asType: a Class representing the type of object to be returned.
   * @return an Object of type asType, or null on failure.
   */
  public static <T> T fromJson(InputStream is, Class<T> asType) {
    T mappedObject = null;

    try {
      mappedObject = MAPPER.readValue(is, asType);
    } catch (IOException e) {
      LOGGER.error("Exception mapping JSON to object", e);
    }

    return mappedObject;
  }

  /**
   * The same as the fromJson(InputStream, Class) method, only this one takes a TypeReference
   *
   * @param is            the Input Stream
   * @param typeReference a type reference
   * @param <T>
   * @return
   */
  public static <T> T fromJson(InputStream is, TypeReference<T> typeReference) {
    T mappedObject = null;

    try {
      mappedObject = MAPPER.readValue(is, typeReference);
    } catch (IOException e) {
      LOGGER.error("Couldn't map JSON from file", e);
    }

    return mappedObject;
  }
}