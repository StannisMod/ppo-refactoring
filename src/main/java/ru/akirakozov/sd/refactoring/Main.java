package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import javax.servlet.http.HttpServlet;

/**
 * @author akirakozov
 */
public class Main {

    public static final String DB_PATH = "jdbc:sqlite:test.db";

    private static void addServlet(ServletContextHandler context, HttpServlet servlet, String path) {
        context.addServlet(new ServletHolder(servlet), "/" + path);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        DbDriver db = new DbDriver(DB_PATH);

        addServlet(context, new AddProductServlet(db), "add-product");
        addServlet(context, new GetProductsServlet(db), "get-products");
        addServlet(context, new QueryServlet(db), "query");

        server.start();
        server.join();
    }
}
