import org.junit.jupiter.api.Test;
import org.qualityscore.QualityScoreController;
import org.qualityscore.QualityScoreService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QualityScoreTests {

    @Test
    public void checkLocationFilter() throws IOException {

        String dogResource = convertDogResourceFromFile();
        String afterConversion = QualityScoreService.convertHttpResponseToDogResponse(dogResource);

        assertEquals(3099, afterConversion.length());
    }

    private String convertDogResourceFromFile() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/java/dog.resource")));
    }

}


