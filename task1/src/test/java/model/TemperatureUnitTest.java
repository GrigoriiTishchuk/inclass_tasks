package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitTest {

    @Test
    void shouldCreateUnitUsingConstructor() {
        TemperatureUnit unit = new TemperatureUnit(1, "Celsius", "C");
        assertEquals(1, unit.getId());
        assertEquals("Celsius", unit.getName());
        assertEquals("C", unit.getSymbol());
    }

    @Test
    void shouldSetAndGetId() {
        TemperatureUnit unit = new TemperatureUnit();
        unit.setId(99);
        assertEquals(99, unit.getId());
    }

    @Test
    void shouldSetAndGetName() {
        TemperatureUnit unit = new TemperatureUnit();
        unit.setName("Kelvin");
        assertEquals("Kelvin", unit.getName());
    }

    @Test
    void shouldSetAndGetSymbol() {
        TemperatureUnit unit = new TemperatureUnit();
        unit.setSymbol("K");
        assertEquals("K", unit.getSymbol());
    }

    @Test
    void shouldCreateEmptyUnit() {
        TemperatureUnit unit = new TemperatureUnit();
        assertNotNull(unit);
    }
}