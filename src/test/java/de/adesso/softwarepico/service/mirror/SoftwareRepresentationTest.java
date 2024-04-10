package de.adesso.softwarepico.service.mirror;

import de.adesso.softwarepico.configuration.LedStatus;
import de.adesso.softwarepico.configuration.SensorStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoftwareRepresentationTest {

    static SoftwareRepresentation softwareRepresentationToTest;

    @BeforeAll
    static void init(){

        softwareRepresentationToTest = new SoftwareRepresentation();

    }

    @Test
    void computeLedStatus() {

        assertEquals(LedStatus.RED, softwareRepresentationToTest.computeLedStatus(SensorStatus.BLOCKED, false));
        assertEquals(LedStatus.RED, softwareRepresentationToTest.computeLedStatus(SensorStatus.BLOCKED, true));

        assertEquals(LedStatus.YELLOW, softwareRepresentationToTest.computeLedStatus(SensorStatus.FREE, true));
        assertEquals(LedStatus.YELLOW, softwareRepresentationToTest.computeLedStatus(SensorStatus.UNKNOWN, true));

        assertEquals(LedStatus.GREEN, softwareRepresentationToTest.computeLedStatus(SensorStatus.FREE, false));

        assertEquals(LedStatus.NONE, softwareRepresentationToTest.computeLedStatus(SensorStatus.UNKNOWN, false));

    }

}