package at.dertanzbogen.api.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class CommandLineHelper
{
    public static int executeCommand(String command, String... args)
    {
        try {
            // Create a command list with the command and its arguments
            var commandList = Stream.concat(
                Stream.of(command),
                Arrays.stream(args)
            ).toList();

            // Execute the command and wait for the process to finish
            return new ProcessBuilder()
                .command(commandList)
                .inheritIO()
                .start()
                .waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }
}
