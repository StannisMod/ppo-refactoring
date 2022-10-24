package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DbDriver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends BaseServlet {

    public QueryServlet(final DbDriver db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        switch (command) {
            case "max":
                printListProducts(response,
                        "SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1",
                        "Product with max price:");
                break;
            case "min":
                printListProducts(response,
                        "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1",
                        "Product with min price:");
                break;
            case "sum":
                printNumber(response,
                        "SELECT SUM(price) FROM PRODUCT",
                        "Summary price:");
                break;
            case "count":
                printNumber(response,
                        "SELECT COUNT(*) FROM PRODUCT",
                        "Number of products:");
                break;
            default:
                response.getWriter().println("Unknown command: " + command);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
