package scabench;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple application that checks whether a file is valid JSON by parsing it,
 * and printing a brief confirmation message to the console.
 * @author jens dietrich
 */
public class CheckJSON {

    public static void main(String[] args) throws IOException {
        Path payload = Path.of(args[0]);
        String json = Files.readString(payload);
        JSON.parse(json);
        System.out.println("JSON is parsable: " + payload.toFile().getAbsolutePath());
    }
}
