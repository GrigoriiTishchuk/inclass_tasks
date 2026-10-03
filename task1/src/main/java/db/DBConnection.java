package db;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final Dotenv dotenv = loadDotenv();

    private static Dotenv loadDotenv() {
        // check if .env exists in the current directory
        if (new File(".env").exists()) {
            return Dotenv.configure().ignoreIfMissing().load();
        }
        // check if .env exists in the task1 directory
        if (new File("./task1/.env").exists()) {
            return Dotenv.configure().directory("./task1").ignoreIfMissing().load();
        }
        // fallback to loading from the current directory (will ignore if missing)
        return Dotenv.configure().ignoreIfMissing().load();
    }

    private static String getConfig(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = dotenv.get(key);
        }
        return value;
    }

    public static Connection getConnection() throws SQLException {
        String host = getConfig("DB_HOST");
        String port = getConfig("DB_PORT");
        String database = getConfig("DB_NAME");

        String user = getConfig("DB_USERNAME");
        if (user == null || user.isBlank()) {
            user = getConfig("DB_USER");
        }

        String password = getConfig("DB_PASSWORD");

        if (host == null) host = "localhost";
        if (port == null) port = "3306";
        if (database == null) database = "temperature_converter";
        if (user == null) user = "root";
        if (password == null) password = "pawooord";

        String url = String.format("jdbc:mariadb://%s:%s/%s", host, port, database);
        return DriverManager.getConnection(url, user, password);
    }
}