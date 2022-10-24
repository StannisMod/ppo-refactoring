package ru.akirakozov.sd.refactoring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbDriverTest {

    @BeforeEach
    public void prepareDB() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");
             Statement stmt = c.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS PRODUCT;");
            stmt.executeUpdate("CREATE TABLE PRODUCT (" +
                                        "name VARCHAR(50), " +
                                        "price INT" +
                                   ");");
            stmt.executeUpdate("INSERT INTO PRODUCT (name, price) VALUES " +
                                    "('apple', 1), ('coffee', 10), ('computer', 100);");
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating test DB!!!", e);
        }
    }

    @Test
    public void testExecuteGet() {
        DbDriver driver = new DbDriver("jdbc:sqlite:test.db");
        int[] counter = new int[1];

        driver.executeGet("SELECT * FROM PRODUCT", rs -> {
            while (rs.next()) {
                counter[0]++;
            }
        });

        // test that GET receives three records
        assert counter[0] == 3;
        // and this is enough, because there is very unbelievable situation if data in records just change
    }
}
