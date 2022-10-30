package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.DbDriver;
import ru.akirakozov.sd.refactoring.Main;

import java.io.IOException;

public class GetProductsTest extends ServletTest {

    private GetProductsServlet servlet;
    private DbDriver db;

    @BeforeEach
    public void prepareServlet() {
        db = new DbDriver(Main.DB_PATH);
        servlet = new GetProductsServlet(db);
    }

    @Test
    public void testGet() throws IOException {
        servlet.doGet(getRequest(), getResponse());

        getResponse().assertContains(
                "apple\t1</br>" +
                "coffee\t10</br>" +
                "computer\t100</br>"
        );
    }
}
