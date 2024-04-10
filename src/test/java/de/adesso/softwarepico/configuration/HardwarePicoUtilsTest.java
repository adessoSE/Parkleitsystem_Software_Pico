package de.adesso.softwarepico.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HardwarePicoUtilsTest {

    @Test
    void idFromUri(){

        String validUri = "1/abc";
        String invalidUri = "2xyz";

        assertEquals(1, HardwarePicoUtils.hpIdFromUri(validUri));
        assertEquals(-1, HardwarePicoUtils.hpIdFromUri(invalidUri));

    }

    @Test
    void ipFromUri(){

        String validUri = "1/abc";
        String invalidUri = "2xyz";

        assertEquals("abc", HardwarePicoUtils.hpIpFromUri(validUri));
        assertEquals("....", HardwarePicoUtils.hpIpFromUri(invalidUri));

    }

}