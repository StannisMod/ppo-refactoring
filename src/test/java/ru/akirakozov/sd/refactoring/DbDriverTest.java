package ru.akirakozov.sd.refactoring;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DbDriverTest {

    public static void prepareTestDB() {
        try (Connection c = DriverManager.getConnection(Main.DB_PATH);
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

    @BeforeEach
    public void prepareDB() {
        prepareTestDB();
    }

    @AfterAll
    public static void clearDB() {
        String pathToDB;
        {
            String[] parts = Main.DB_PATH.split(":");
            pathToDB = parts[parts.length - 1];
        }
        new File(pathToDB).delete();
    }

    @Test
    public void testExecuteGet() {
        DbDriver driver = new DbDriver(Main.DB_PATH);
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

    @Test
    public void testExecuteModify() {
        DbDriver driver = new DbDriver(Main.DB_PATH);

        driver.executeUpdate("INSERT INTO PRODUCT (name, price) VALUES ('monitor', 1100);");

        final Set<String> expectedNames = Arrays.stream(new String[]{"apple", "coffee", "computer", "monitor"})
                .collect(Collectors.toSet());
        final Set<Integer> expectedPrices = Arrays.stream(new Integer[]{1, 10, 100, 1100})
                .collect(Collectors.toSet());
        driver.executeGet("SELECT * FROM PRODUCT", rs -> {
            while (rs.next()) {
                assert expectedNames.remove(rs.getString("name"));
                assert expectedPrices.remove(rs.getInt("price"));
            }
        });

        // test that GET receives four right records
        assert expectedNames.size() == 0 && expectedPrices.size() == 0;
    }
}
