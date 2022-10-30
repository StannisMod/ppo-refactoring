package ru.akirakozov.sd.refactoring;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DbTest {

    private AutoCloseable mockitoObj;

    @BeforeEach
    public void prepareMocks() {
        mockitoObj = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void closeMocks() throws Exception {
        mockitoObj.close();
    }

    @BeforeEach
    public void prepareDB() {
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

    @AfterAll
    public static void clearDB() {
        String pathToDB;
        {
            String[] parts = Main.DB_PATH.split(":");
            pathToDB = parts[parts.length - 1];
        }
        new File(pathToDB).delete();
    }

    protected void assertDbContents(DbDriver driver, String[] names, Integer[] prices) {
        assertDbContents(driver, "SELECT * FROM Product", names, prices);
    }

    protected void assertDbContents(DbDriver driver, String query, String[] names, Integer[] prices) {
        final Set<String> expectedNames = Arrays.stream(names).collect(Collectors.toSet());
        final Set<Integer> expectedPrices = Arrays.stream(prices).collect(Collectors.toSet());
        driver.executeGet(query, rs -> {
            while (rs.next()) {
                assert expectedNames.remove(rs.getString("name"));
                assert expectedPrices.remove(rs.getInt("price"));
            }
        });

        // test that GET receives four right records
        assert expectedNames.size() == 0 && expectedPrices.size() == 0;
    }
}
