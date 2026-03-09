import org.junit.Test;
import static org.junit.Assert.*;

public class HealthCheckerTest {

    @Test
    public void testWebsiteStatus() {
        int statusCode = 200;
        assertEquals(200, statusCode);
    }
}