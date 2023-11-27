package at.dertanzbogen.api.foundation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.time.Instant;

// This is just used in our JsonUtils class to serialize and deserialize the Instant objects
// Merely to make the MongoDB importer happy.

// The MongoDB Extended JSON format MongoDB Extended JSON
// https://www.mongodb.com/docs/manual/reference/mongodb-extended-json/?_ga=2.174726055.485943083.1678609284-226039830.1669704325#mongodb-bsontype-Date


public class MongoImporterInstantSerializer
{
    public static final SimpleModule module = new SimpleModule();

    static {
        module.addSerializer(Instant.class, new InstantSerializer());
        module.addDeserializer(Instant.class, new InstantDeserializer());
    }

    // This is the format that the MongoDB importer expects for the Instant objects:
    // { "$date" : "2021-08-01T00:00:00Z" }
    public static class InstantSerializer extends JsonSerializer<Instant>
    {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException
        {
            gen.writeStartObject();
            gen.writeFieldName("$date");
            gen.writeString(value.toString());
            gen.writeEndObject();
        }
    }

    // The reverse of the above serialization in order to test the deserialization
    public static class InstantDeserializer extends JsonDeserializer<Instant>
    {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            JsonNode node = p.getCodec().readTree(p);
            String dateString = node.get("$date").asText();
            return Instant.parse(dateString);
        }
    }

}
