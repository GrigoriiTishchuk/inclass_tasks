import model.TemperatureUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void shouldReturnSameValueWhenUnitsAreEqual() {
        TemperatureUnit celsius = new TemperatureUnit(1, "Celsius", "C");
        double result = Main.convertTemperature(100.0, celsius, celsius);
        assertEquals(100.0, result, 0.001);
    }

    @Test
    void shouldConvertCelsiusToFahrenheit() {
        TemperatureUnit celsius = new TemperatureUnit(1, "Celsius", "C");
        TemperatureUnit fahrenheit = new TemperatureUnit(2, "Fahrenheit", "F");
        double result = Main.convertTemperature(100.0, celsius, fahrenheit);
        assertEquals(212.0, result, 0.001);
    }

    @Test
    void shouldConvertFahrenheitToCelsius() {
        TemperatureUnit fahrenheit = new TemperatureUnit(2, "Fahrenheit", "F");
        TemperatureUnit celsius = new TemperatureUnit(1, "Celsius", "C");
        double result = Main.convertTemperature(32.0, fahrenheit, celsius);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void shouldConvertKelvinToCelsius() {
        TemperatureUnit kelvin = new TemperatureUnit(3, "Kelvin", "K");
        TemperatureUnit celsius = new TemperatureUnit(1, "Celsius", "C");
        double result = Main.convertTemperature(273.15, kelvin, celsius);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void shouldConvertCelsiusToKelvin() {
        TemperatureUnit celsius = new TemperatureUnit(1, "Celsius", "C");
        TemperatureUnit kelvin = new TemperatureUnit(3, "Kelvin", "K");
        double result = Main.convertTemperature(0.0, celsius, kelvin);
        assertEquals(273.15, result, 0.001);
    }

    @Test
    void shouldThrowExceptionWhenTargetUnitIsInvalid() {
        TemperatureUnit celsius = new TemperatureUnit(1, "Celsius", "C");
        TemperatureUnit wrongUnit = new TemperatureUnit(99, "WrongUnit", "X");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Main.convertTemperature(100.0, celsius, wrongUnit)
        );
        assertEquals("Invalid temperature units", exception.getMessage());
    }
}