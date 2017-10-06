package util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Simple ObjectId serialiser that just outputs the ObjectId as a String
 *
 * Created by Samuil.
 *
 * @author Samuil Dichev
 */
public class ObjectIdSerializer {

  private static final SimpleModule MODULE = new SimpleModule(
          ObjectIdSerializer.class.getSimpleName(), new Version(1, 0, 0, "git", "cardiff",
          "oid-serializer"));

  static {
    MODULE.addSerializer(ObjectId.class, new Serializer());
  }

  static SimpleModule getModule() {
    return MODULE;
  }

  public static class Serializer extends JsonSerializer<ObjectId> {

    @Override
    public void serialize(ObjectId objectId, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
      jsonGenerator.writeString(objectId.toString());
    }
  }
}

