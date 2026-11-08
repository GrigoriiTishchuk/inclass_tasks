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
        return findAllByLang("en");
    }

    public List<TemperatureUnit> findAllByLang(String lang) {
        List<TemperatureUnit> units = new ArrayList<>();
        String sql = "SELECT u.id, u.symbol, " +
                "COALESCE(t.name, u.name) AS name, " +
                "t.description " +
                "FROM temperature_unit u " +
                "LEFT JOIN unit_translation t ON u.id = t.unit_id AND t.lang = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, lang);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    units.add(new TemperatureUnit(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("symbol"),
                            rs.getString("description")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return units;
    }

    public TemperatureUnit findByName(String name) {
        return findByNameAndLang(name, "en");
    }

    public TemperatureUnit findByNameAndLang(String name, String lang) {
        String sql = "SELECT u.id, u.symbol, COALESCE(t.name, u.name) AS name, t.description " +
                "FROM temperature_unit u " +
                "LEFT JOIN unit_translation t ON u.id = t.unit_id AND t.lang = ? " +
                "WHERE COALESCE(t.name, u.name) = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, lang);
            statement.setString(2, name);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new TemperatureUnit(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("symbol"),
                            rs.getString("description")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}