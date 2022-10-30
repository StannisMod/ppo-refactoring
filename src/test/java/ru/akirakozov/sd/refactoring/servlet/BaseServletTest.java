package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.DbDriver;
import ru.akirakozov.sd.refactoring.DbTest;
import ru.akirakozov.sd.refactoring.Main;

public class BaseServletTest extends DbTest {

    private CachingResponse response;
    private BaseServlet servlet;

    @BeforeEach
    public void prepareServlet() {
        response = new CachingResponse();
        servlet = new BaseServlet(new DbDriver(Main.DB_PATH)) {};
    }

    @Test
    public void testHTMLResponseGet() {
        servlet.formResponse(response, "SELECT * FROM PRODUCT WHERE price < 100", (writer, rs) -> {
            writer.println("Hello");
        });

        response.assertContents(wrapHtmlBody("Hello"));
    }

    @Test
    public void testHTMLResponseGetList() {
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
        String header = "Sum price of products:";
        servlet.printNumber(response, "SELECT SUM(price) FROM PRODUCT", header);

        response.assertContents(wrapHtmlBody(
                "<h1>" + header + "</h1>" +
                        (100 + 10 + 1)
        ));
    }

    static String wrapHtmlBody(String contents) {
        return "<html><body>" + contents + "</body></html>";
    }
}
