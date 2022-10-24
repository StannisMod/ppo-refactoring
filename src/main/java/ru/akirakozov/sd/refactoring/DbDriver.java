package ru.akirakozov.sd.refactoring;

import java.io.IOException;
import java.sql.*;

public class DbDriver {

    private final String dbAddress;

    public DbDriver(String pathToDb) {
        this.dbAddress = pathToDb;
        runSQL(stmt -> {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            stmt.executeUpdate(sql);
            return null;
        });
    }

    public void executeUpdate(String query) {
        runSQL(stmt -> {
            stmt.executeUpdate(query);
            return null;
        });
    }

    public void executeGet(String query, ResultSetHandler handler) {
        try {
            handler.run(runSQL(stmt -> stmt.executeQuery(query)));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet runSQL(SQLRunnable runnable) {
        try (Connection c = DriverManager.getConnection(dbAddress);
             Statement stmt = c.createStatement()) {
            return runnable.run(stmt);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private interface SQLRunnable {
        ResultSet run(Statement stmt) throws SQLException;
    }

    public interface ResultSetHandler {
        void run(ResultSet rs) throws SQLException, IOException;
    }
}
