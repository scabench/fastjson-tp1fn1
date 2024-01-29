package scabench;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test case to verify fastjson vulnerabilities.
 * Note the preconditions on OS / JRE expressed in @Enabled* annotations for tests.
 * @author jens dietrich
 */
public class ConfirmVulnerabilitiesTests {

    @BeforeEach
    public void clearGeneratedFile() {
        File file = new File("foo");
        if (file.exists()) {
            Assumptions.assumeTrue(file.delete());
        }
    }

    @Test
    @EnabledOnOs({OS.MAC,OS.LINUX})
    @EnabledForJreRange(min=JRE.JAVA_8,max=JRE.JAVA_11)
    public void confirmCVE202225845 () throws Exception {
        Path generatedFile = Path.of("foo");
        Assumptions.assumeFalse(Files.exists(generatedFile));

        CheckJSON.main(new String[]{"CVE-2022-25845.json"});
        Thread.sleep(1000);  // wait for external (async)  process to create the file

        assertTrue(Files.exists(generatedFile));
    }
}
