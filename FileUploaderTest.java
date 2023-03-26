import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class FileUploaderTest {

    @Test
    public void testCountWords() {
        FileUploader fileUploader = new FileUploader();
        String text = "Tapping at my chamber door- Only this adn";
        Map<String, Integer> wordCountMap = fileUploader.countWords(text);
        assertEquals(8, wordCountMap.size());
        assertEquals(1, wordCountMap.get("tapping"));
        assertEquals(1, wordCountMap.get("at"));
        assertEquals(1, wordCountMap.get("my"));
        assertEquals(1, wordCountMap.get("chamber"));
        assertEquals(1, wordCountMap.get("door"));
        assertEquals(1, wordCountMap.get("only"));
        assertEquals(1, wordCountMap.get("this"));
        assertEquals(1, wordCountMap.get("adn"));
    }

    @Test
    public void testSortMapByValue() {
        FileUploader fileUploader = new FileUploader();
        Map<String, Integer> wordCountMap = Map.of("the", 3, "quick", 2, "brown", 1, "fox", 4);
        String expectedString = "fox : 4\nthe : 3\nquick : 2\nbrown : 1\n";
        String actualString = fileUploader.sortMapByValue(wordCountMap);
        assertEquals(expectedString, actualString);
    }

}