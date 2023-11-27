package at.dertanzbogen.api.foundation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

// Video for Generics
// https://www.youtube.com/watch?v=K1iu1kXkVoA


public class JsonUtils
{
    private static Logger LOGGER = Logger.getLogger(JsonUtils.class.getName());


    // JSON Serialization/Deserialization
    private static final ObjectMapper mapper =
            JsonMapper.builder()
                    // Serialize and deserialize Java 8 date/time classes
                    .findAndAddModules()
                    // Needed for exporting JSON and then to make the mongo importer happy
                    .addModule(MongoImporterInstantSerializer.module)
                    .build();

    // static initializer
//    static {
//        mapper.findAndRegisterModules();
//    }

    // Loads the json file from the file system and converts it to a list of objects of type T
    public static <T> List<T> loadJsonListFromFile(
            String path, Class<T> clazz) throws IOException
    {
        LOGGER.info("Reading JSON from file: " + path);

        return mapper.readValue(new File(path),
                mapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }


    // Loads the json file from the file system and converts it to a single object of type T
    public static <T> T loadJsonFromFile(
            String path, Class<T> clazz) throws IOException
    {
        LOGGER.info("Reading JSON from file: " + path);

        return mapper.readValue(new File(path), clazz);
    }


    // Write a list of objects to a json file
    public static <T> void writeJsonListToFile(
            List<T> list, String path) throws IOException
    {
        LOGGER.info("Writing JSON to file: " + path);

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), list);
    }


    // Write a single object to a json file
    public static <T> void writeJsonToFile(
            T object, String path) throws IOException
    {
        LOGGER.info("Writing JSON to file: " + path);

        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), object);
    }
}
