package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DbDriver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends BaseServlet {

    public QueryServlet(final DbDriver db) {
        super(db);
    }

    private void listProducts(ResultSet rs, PrintWriter writer) throws SQLException {
        while (rs.next()) {
            String name = rs.getString("name");
            int price = rs.getInt("price");
            writer.println(name + "\t" + price + "</br>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        String sql;
        String header;
        switch (command) {
            case "max":
                sql = "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1";
                header = "Product with max price:";
                formResponse(response, sql, (r, writer, rs) -> {
                    writer.println("<h1>" + header + "</h1>");
                    listProducts(rs, writer);
                });
                break;
            case "min":
                sql = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1";
                header = "Product with min price:";
                formResponse(response, sql, (r, writer, rs) -> {
                    writer.println("<h1>" + header + "</h1>");
                    listProducts(rs, writer);
                });
                break;
            case "sum":
                try {
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        Statement stmt = c.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
                        response.getWriter().println("<html><body>");
                        response.getWriter().println("Summary price: ");

                        if (rs.next()) {
                            response.getWriter().println(rs.getInt(1));
                        }
                        response.getWriter().println("</body></html>");

                        rs.close();
                        stmt.close();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                sql = "SELECT SUM(price) FROM PRODUCT";
                header = "Summary price:";
                formResponse(response, sql, (r, writer, rs) -> {
                    writer.println("<h1>" + header + "</h1>");
                    listProducts(rs, writer);
                });
            case "count":
                try {
                    try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
                        Statement stmt = c.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
                        response.getWriter().println("<html><body>");
                        response.getWriter().println("Number of products: ");

                        if (rs.next()) {
                            response.getWriter().println(rs.getInt(1));
                        }
                        response.getWriter().println("</body></html>");

                        rs.close();
                        stmt.close();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            default:
                response.getWriter().println("Unknown command: " + command);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
