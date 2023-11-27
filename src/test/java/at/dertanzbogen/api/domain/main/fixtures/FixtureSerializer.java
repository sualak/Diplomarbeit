package at.dertanzbogen.api.domain.main.fixtures;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.data.annotation.TypeAlias;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

// Video for Generics
// https://www.youtube.com/watch?v=K1iu1kXkVoA

// 1. Configures the Jackson JSON ObjectMapper via a builder
//    mapper = JsonMapper.builder()

// 2. Discover and register any available datatype modules in the classpath
//    .findAndAddModules()

// 3. Serializes Instant objects as ISO 8601 dates:
//    https://de.wikipedia.org/wiki/ISO_8601
//    -> For serializing dates for a client like a browser
//    Uses the format "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
//    -> "2021-08-01T00:00:00.000Z"
//    The format consists of the date and time (including milliseconds),
//    separated by the 'T' character and ending with the 'Z' character
//    to indicate that it is in UTC (Coordinated Universal Time).
// .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

// 3. Serializes Instant objects as BSON dates:
//    https://www.mongodb.com/docs/manual/reference/mongodb-extended-json#mongodb-bsontype-Date
//    -> For serializing dates for the MongoDB importer
//    Uses the format { "$date" : ISO-8601 }
//    -> { "$date" : "2021-08-01T00:00:00.000Z" }
// .addModule(module)

// 4. Ignore unknown properties during deserialization.
//    We need to add this because the `_class` field is not part of the domain model.
// .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


// This class is used to serialize and deserialize the fixture data.
// That fixture data is used to test the application or populate the test or main database.

public class FixtureSerializer
{
    private static Logger LOGGER = Logger.getLogger(FixtureSerializer.class.getName());

    private static final ObjectMapper mapper;


    static
    {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new InstantSerializer());
        module.addDeserializer(Instant.class, new InstantDeserializer());

        mapper = JsonMapper.builder()
                // Discover and register any available datatype modules in the classpath
                .findAndAddModules()
                // Add custom Instant serialization/deserialization module
                .addModule(module)
                // Ignore unknown properties during deserialization (`_class`)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }


    // Loads the json file from the file system and converts it to a list of objects of type T
    public static <T> List<T> loadJsonListFromFile(String path, Class<T> clazz) throws IOException
    {
        LOGGER.info("Reading JSON from file: " + path);

        return mapper.readValue(new File(path),
                mapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }


    // Write the list of objects of type T to a json file
    public static <T> void writeJsonListToFile(List<T> list, String path) throws IOException
    {
        LOGGER.info("Writing JSON to file: {}" + path);

        // Create the directory of the fixture path if it doesn't exist
        createDirectoryIfNotExists(path);

        // Create a list of items with `_class` field added
        List<Map<String, Object>> listWithClass = createListWithClassField(list);

        // Write the list to the file
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), listWithClass);
    }


    // Create the directory if it doesn't exist
    public static void createDirectoryIfNotExists(String path) throws IOException
    {
        Path filePath = Paths.get(path);
        Path parentDir = filePath.getParent();
        if (parentDir != null && !Files.exists(parentDir))
        {
            LOGGER.info("Creating directory: " + parentDir);
            Files.createDirectories(parentDir);
        }
    }


    // Create a list of items with `_class` field added
    @SuppressWarnings("unchecked")
    private static <T> List<Map<String, Object>> createListWithClassField(List<T> list) {
        return list.stream()
                .map(item -> {
                    // Convert the item to a map (key = field name, value = field value) using Jackson
                    Map<String, Object> itemMap = mapper.convertValue(item, Map.class);
                    // Add the '_class' field to the item map, with the value obtained from getTypeAliasOrClassName()
                    itemMap.put("_class", getTypeAliasOrClassName(item));
                    return itemMap;
                })
                .toList();
    }


    // Returns the value of the `@TypeAlias` annotation if present,
    // otherwise the fully qualified class name.
    private static <T> String getTypeAliasOrClassName(T item)
    {
        Class<?> clazz = item.getClass();
        TypeAlias typeAlias = clazz.getDeclaredAnnotation(TypeAlias.class);
        return typeAlias != null ? typeAlias.value() : clazz.getName();
    }



    // Jackson Custom Serializers
    // ----------------------------------

    // The serialization of the Instant object into the MongoDB Extended JSON format
    static class InstantSerializer extends JsonSerializer<Instant>
    {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException
        {
            gen.writeStartObject();
            gen.writeFieldName("$date");
            // Generates ISO-8601 representation
            gen.writeString(value.toString());
            gen.writeEndObject();
        }
    }


    // The deserialization of the Instant object from the MongoDB Extended JSON format
    static class InstantDeserializer extends JsonDeserializer<Instant>
    {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
        {
            JsonNode node = p.getCodec().readTree(p);
            // Gets the ISO-8601 representation and parses it into an Instant object
            String dateString = node.get("$date").asText();
            return Instant.parse(dateString);
        }
    }

}
