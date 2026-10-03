package dao;

import db.DBConnection;
import model.TemperatureRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TemperatureRecordDAO {

    public List<TemperatureRecord> findAll() {
        List<TemperatureRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM temperature_conversion";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                TemperatureRecord record = new TemperatureRecord(
                        rs.getInt("source_unit_id"),
                        rs.getInt("target_unit_id"),
                        rs.getDouble("original_value"),
                        rs.getDouble("converted_value")
                );
                if (rs.getTimestamp("created_at") != null) {
                    record.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                records.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public void save(TemperatureRecord record) {
        String sql = "INSERT INTO temperature_conversion (source_unit_id, target_unit_id, original_value, converted_value) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, record.getSourceUnitId());
            statement.setInt(2, record.getTargetUnitId());
            statement.setDouble(3, record.getOriginalValue());
            statement.setDouble(4, record.getConvertedValue());

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}