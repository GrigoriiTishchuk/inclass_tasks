import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {

    private TemperatureConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TemperatureConverter();
    }

    @Test
    void testFahrenheitToCelsius() {
        // freezing, boiling, and the cross-over point (-40)
        assertEquals(0.0, converter.fahrenheitToCelsius(32.0), 0.0001);
        assertEquals(100.0, converter.fahrenheitToCelsius(212.0), 0.0001);
        assertEquals(-40.0, converter.fahrenheitToCelsius(-40.0), 0.0001);
    }

    @Test
    void testCelsiusToFahrenheit() {
        assertEquals(32.0, converter.celsiusToFahrenheit(0.0), 0.0001);
        assertEquals(212.0, converter.celsiusToFahrenheit(100.0), 0.0001);
        assertEquals(-40.0, converter.celsiusToFahrenheit(-40.0), 0.0001);
    }

    @Test
    void testIsExtremeTemperature() {
        // Normal range (should return false)
        assertFalse(converter.isExtremeTemperature(20.0));
        // Exact boundary conditions (should return false)
        assertFalse(converter.isExtremeTemperature(-40.0));
        assertFalse(converter.isExtremeTemperature(50.0));
        // Beyond boundaries (should return true)
        assertTrue(converter.isExtremeTemperature(-40.1));
        assertTrue(converter.isExtremeTemperature(50.1));
    }
}