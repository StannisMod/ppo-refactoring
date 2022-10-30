package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.DbDriver;
import ru.akirakozov.sd.refactoring.Main;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class AddProductTest extends ServletTest {

    private AddProductServlet servlet;
    private DbDriver db;

    @BeforeEach
    public void prepareServlet() {
        db = new DbDriver(Main.DB_PATH);
        servlet = new AddProductServlet(db);
    }

    @Test
    public void testInsert() throws IOException {
        when(getRequest().getParameter("name")).thenReturn("pineapple");
        when(getRequest().getParameter("price")).thenReturn("2");

        servlet.doGet(getRequest(), getResponse());

        assertDbContents(db,
                new String[] { "apple", "pineapple", "coffee", "computer" },
                new Integer[] { 1, 2, 10, 100 });
    }
}
