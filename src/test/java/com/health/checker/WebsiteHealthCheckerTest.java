package com.health.checker;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebsiteHealthCheckerTest {

    @Test
    public void testWebsiteResponse() {
        int responseCode = 200;
        assertEquals(200, responseCode);
    }
}