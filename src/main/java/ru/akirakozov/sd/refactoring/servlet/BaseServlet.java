package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DbDriver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseServlet extends HttpServlet {

    private final DbDriver db;

    protected BaseServlet(final DbDriver db) {
        this.db = db;
    }

    public DbDriver getDb() {
        return db;
    }

    protected void formResponse(HttpServletResponse response, String getQuery, ResponseFormer former) {
        getDb().executeGet(getQuery, rs -> {
            PrintWriter rWriter = response.getWriter();
            rWriter.println("<html><body>");
            former.form(response, rWriter, rs);
            rWriter.println("</body></html>");
        });
    }

    protected void listProducts(ResultSet rs, PrintWriter writer) throws SQLException {
        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            writer.println(name + "\t" + price + "</br>");
        }
    }

    protected void printListProducts(HttpServletResponse response, String sql, String header) {
        formResponse(response, sql, (r, writer, rs) -> {
            writer.println("<h1>" + header + "</h1>");
            listProducts(rs, writer);
        });
    }

    protected void printNumber(HttpServletResponse response, String sql, String header) {
        formResponse(response, sql, (r, writer, rs) -> {
            writer.println("<h1>" + header + "</h1>");
            if (rs.next()) {
                writer.println(rs.getInt(1));
            }
        });
    }

    protected interface ResponseFormer {
        void form(HttpServletResponse response, PrintWriter writer, ResultSet rs) throws SQLException, IOException;
    }
}
