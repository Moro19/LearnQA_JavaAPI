import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShortPhrase {
    @Test
    public void testJUnit() {

        String hello = "Hello, world from Russia with love";
        assertTrue(hello.length() > 15, "В тексте менее 15 символов");

    }
}
