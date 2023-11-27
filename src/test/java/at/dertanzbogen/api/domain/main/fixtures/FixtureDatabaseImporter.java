package at.dertanzbogen.api.domain.main.fixtures;

import java.io.IOException;

// This class is used to import the json files into the database
// The json files are created using the FixtureSerializer class

public class FixtureDatabaseImporter
{
    public static int fromJson(String dbName, String taskName, String jsonName)
    {
        try {
            // The following command is executed in the terminal and waits for the process to finish
            return new ProcessBuilder()
                .command("./gradlew", taskName, "-Pdb=" + dbName, "-Pfile=" + jsonName)
                .inheritIO()
                .start()
                .waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // Indicate an error
            return 1;
        }
    }
}
