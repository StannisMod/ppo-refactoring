package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.DbDriver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends BaseServlet {

    public GetProductsServlet(final DbDriver db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        printListProducts(response, "SELECT * FROM PRODUCT", "");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
