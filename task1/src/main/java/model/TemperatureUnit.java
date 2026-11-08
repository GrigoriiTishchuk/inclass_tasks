package model;

public class TemperatureUnit {
    private int id;
    private String name;
    private String symbol;
    private String description;

    public TemperatureUnit() {}

    public TemperatureUnit(int id, String name, String symbol) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
    }

    public TemperatureUnit(int id, String name, String symbol, String description) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}