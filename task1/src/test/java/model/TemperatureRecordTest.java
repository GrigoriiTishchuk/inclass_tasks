package model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureRecordTest {

    @Test
    void shouldCreateRecordUsingConstructor() {
        TemperatureRecord record = new TemperatureRecord(1, 2, 100.0, 212.0);
        assertEquals(1, record.getSourceUnitId());
        assertEquals(2, record.getTargetUnitId());
        assertEquals(100.0, record.getOriginalValue());
        assertEquals(212.0, record.getConvertedValue());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        TemperatureRecord record = new TemperatureRecord();
        LocalDateTime now = LocalDateTime.now();
        record.setCreatedAt(now);
        assertEquals(now, record.getCreatedAt());
    }

    @Test
    void shouldCreateEmptyRecord() {
        TemperatureRecord record = new TemperatureRecord();
        assertNotNull(record);
    }
}
