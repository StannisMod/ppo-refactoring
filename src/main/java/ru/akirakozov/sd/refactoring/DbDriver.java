package ru.akirakozov.sd.refactoring;

import java.io.IOException;
import java.sql.*;

public class DbDriver {

    private final String dbAddress;

    public DbDriver(String pathToDb) {
        this.dbAddress = pathToDb;
        executeUpdate(
                "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)"
        );
    }

    public void executeUpdate(String query) {
        runSQL(null, stmt -> {
            stmt.executeUpdate(query);
            return null;
        });
    }

    public void executeGet(String query, ResultSetHandler handler) {
        runSQL(handler, stmt -> stmt.executeQuery(query));
    }

    private void runSQL(ResultSetHandler handler, SQLRunnable runnable) {
        try (Connection c = DriverManager.getConnection(dbAddress);
            Statement stmt = c.createStatement()) {
            ResultSet rs = runnable.run(stmt);
            if (handler != null) {
                handler.run(rs);
            }
        } catch (SQLException | IOException e) {
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
