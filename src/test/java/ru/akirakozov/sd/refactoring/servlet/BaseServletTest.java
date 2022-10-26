package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.DbDriver;
import ru.akirakozov.sd.refactoring.DbDriverTest;
import ru.akirakozov.sd.refactoring.Main;

public class BaseServletTest {

    @BeforeEach
    public void prepareDB() {
        DbDriverTest.prepareTestDB();
    }

    @Test
    public void testHTMLResponseGet() {
        CachingResponse response = new CachingResponse();
        BaseServlet servlet = new BaseServlet(new DbDriver(Main.DB_PATH)) {};

        servlet.formResponse(response, "SELECT * FROM PRODUCT WHERE price < 100", (writer, rs) -> {
            writer.println("Hello");
        });

        response.assertContents(wrapHtmlBody("Hello"));
    }

    @Test
    public void testHTMLResponseGetList() {
        CachingResponse response = new CachingResponse();
        BaseServlet servlet = new BaseServlet(new DbDriver(Main.DB_PATH)) {};

        String header = "Products with price < 100:";
        servlet.printListProducts(response, "SELECT * FROM PRODUCT WHERE price < 100", header);

        response.assertContents(wrapHtmlBody(
                "<h1>" + header + "</h1>" +
                        "apple\t1</br>" +
                        "coffee\t10</br>"
        ));
    }

    @Test
    public void testHTMLResponseGetNumber() {
        CachingResponse response = new CachingResponse();
        BaseServlet servlet = new BaseServlet(new DbDriver(Main.DB_PATH)) {};

        String header = "Sum price of products:";
        servlet.printNumber(response, "SELECT SUM(price) FROM PRODUCT", header);

        response.assertContents(wrapHtmlBody(
                "<h1>" + header + "</h1>" +
                        (100 + 10 + 1)
        ));
    }

    private static String wrapHtmlBody(String contents) {
        return "<html><body>" + contents + "</body></html>";
    }
}
