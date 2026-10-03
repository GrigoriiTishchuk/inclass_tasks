package db;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void shouldCreateConnection() {
        assertDoesNotThrow(() -> {
            try (Connection connection = DBConnection.getConnection()) {
                assertNotNull(connection);
                assertFalse(connection.isClosed());
            }
        });
    }
}
