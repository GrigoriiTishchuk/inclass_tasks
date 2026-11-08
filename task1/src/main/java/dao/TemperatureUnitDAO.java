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
        // Сначала ищем по базовой таблице (английское имя)
        String sql = "SELECT id, name, symbol FROM temperature_unit WHERE name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new TemperatureUnit(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("symbol")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Если не нашли в базовой, ищем в таблице переводов
        String translationSql = "SELECT u.id, u.symbol, t.name, t.description " +
                "FROM temperature_unit u " +
                "JOIN unit_translation t ON u.id = t.unit_id " +
                "WHERE t.name = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(translationSql)) {

            statement.setString(1, name);
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

    public TemperatureUnit findByNameAndLang(String name, String lang) {
        if (lang == null || lang.equals("en")) {
            return findByName(name);
        }

        String sql = "SELECT u.id, u.symbol, t.name, t.description " +
                "FROM temperature_unit u " +
                "JOIN unit_translation t ON u.id = t.unit_id " +
                "WHERE t.name = ? AND t.lang = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, lang);
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

        // Fallback на английский
        return findByName(name);
    }
}