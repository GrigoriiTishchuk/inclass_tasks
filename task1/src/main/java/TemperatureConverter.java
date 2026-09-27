public class TemperatureConverter {

    public double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5.0 / 9.0;
    }

    public double celsiusToFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32;
    }

    public boolean isExtremeTemperature(double celsius) {
        return celsius < -40.0 || celsius > 50.0;
    }

    public double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }


    public static void main(String[] args) {
        TemperatureConverter converter = new TemperatureConverter();

        System.out.println("100°F to °C: " + converter.fahrenheitToCelsius(100));
        System.out.println("25°C to °F: " + converter.celsiusToFahrenheit(25));
        System.out.println("Extreme temperature -50°C? " + converter.isExtremeTemperature(-50));
        System.out.println("300 K to °C: " + converter.kelvinToCelsius(300));
    }
}