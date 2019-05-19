package server.lib;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class GsonBootstrap {
    public static Gson create() {
        return new GsonBuilder()
            .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    long epoch = json.getAsJsonObject().get("$date").getAsLong();
                    return new Date(epoch);
                }
            })
            .registerTypeAdapter(ObjectId.class, new TypeAdapter<ObjectId>() {
                @Override
                public void write(final JsonWriter out, final ObjectId value) throws IOException {
                    out.beginObject()
                            .name("$oid")
                            .value(value.toString())
                            .endObject();
                }

                @Override
                public ObjectId read(final JsonReader in) throws IOException {
                    in.beginObject();
                    assert "$oid".equals(in.nextName());
                    String objectId = in.nextString();
                    in.endObject();
                    return new ObjectId(objectId);
                }
            })
            .create();
    }
}
