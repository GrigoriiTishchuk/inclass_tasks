package model;

import java.time.LocalDateTime;

public class TemperatureRecord {
    private int id;
    private int sourceUnitId;
    private int targetUnitId;
    private double originalValue;
    private double convertedValue;
    private LocalDateTime createdAt;

    public TemperatureRecord() {}

    public TemperatureRecord(int sourceUnitId, int targetUnitId, double originalValue, double convertedValue) {
        this.sourceUnitId = sourceUnitId;
        this.targetUnitId = targetUnitId;
        this.originalValue = originalValue;
        this.convertedValue = convertedValue;
    }

    public int getSourceUnitId() { return sourceUnitId; }
    public int getTargetUnitId() { return targetUnitId; }
    public double getOriginalValue() { return originalValue; }
    public double getConvertedValue() { return convertedValue; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}