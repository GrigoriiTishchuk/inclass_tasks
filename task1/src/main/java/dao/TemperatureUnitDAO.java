package dao;

import db.DBConnection;
import model.TemperatureUnit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TemperatureUnitDAO {

    public List<TemperatureUnit> findAll() {
        List<TemperatureUnit> units = new ArrayList<>();
        String sql = "SELECT id, name, symbol FROM temperature_unit";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                units.add(new TemperatureUnit(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("symbol")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return units;
    }

    public TemperatureUnit findByName(String name) {
        String sql = "SELECT id, name, symbol FROM temperature_unit WHERE name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new TemperatureUnit(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("symbol")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}