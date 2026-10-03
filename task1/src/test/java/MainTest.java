import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void shouldReturnSameValueWhenUnitsAreEqual() {
        double result = Main.convertTemperature(100.0, "Celsius", "Celsius");
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void shouldConvertCelsiusToFahrenheit() {
        double result = Main.convertTemperature(100.0, "Celsius", "Fahrenheit");
        assertEquals(212.0, result, 0.001);
    }

    @Test
    void shouldConvertFahrenheitToCelsius() {
        double result = Main.convertTemperature(32.0, "Fahrenheit", "Celsius");
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void shouldConvertKelvinToCelsius() {
        double result = Main.convertTemperature(273.15, "Kelvin", "Celsius");
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void shouldConvertCelsiusToKelvin() {
        double result = Main.convertTemperature(0.0, "Celsius", "Kelvin");
        assertEquals(273.15, result, 0.001);
    }

    @Test
    void shouldThrowExceptionWhenTargetUnitIsInvalid() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Main.convertTemperature(100.0, "Celsius", "WrongUnit")
        );
        assertEquals("Invalid temperature units", exception.getMessage());
    }
}