package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.DbDriver;
import ru.akirakozov.sd.refactoring.Main;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

import static org.mockito.Mockito.when;

public class QueryServletTest extends ServletTest {

    private QueryServlet servlet;

    @BeforeEach
    public void prepareServlet() {
        servlet = new QueryServlet(new DbDriver(Main.DB_PATH));
    }

    private void runQueryTest(String command, int status) throws IOException {
        when(getRequest().getParameter("command")).thenReturn(command);

        servlet.doGet(getRequest(), getResponse());

        getResponse().assertStatus(status);
    }

    private void runQueryTest(String command, String expected) throws IOException {
        when(getRequest().getParameter("command")).thenReturn(command);

        servlet.doGet(getRequest(), getResponse());

        getResponse().assertStatus(HttpServletResponse.SC_OK);
        getResponse().assertContains(expected);
    }

    @Test
    public void testQueryServletGetMax() throws IOException {
        runQueryTest("max", "computer\t100");
    }

    @Test
    public void testQueryServletGetMin() throws IOException {
        runQueryTest("min", "apple\t1");
    }

    @Test
    public void testQueryServletGetSum() throws IOException {
        runQueryTest("sum", String.valueOf(100 + 10 + 1));
    }

    @Test
    public void testQueryServletGetCount() throws IOException {
        runQueryTest("count", String.valueOf(3));
    }

    @Test
    public void testQueryServletGetUnknown() throws IOException {
        Random rand = new Random();
        String sample = "abcdefg234YS354647FGD89:lklajaLISDFuHsdfhjgsKSLFDY9pOL:iahj";
        for (int i = 0; i < 1000; i++) {
            int[] indexes = rand.ints(sample.length(), 0, sample.length()).toArray();
            StringBuilder b = new StringBuilder();
            for (int ind : indexes) {
                b.append(sample.charAt(ind));
            }
            runQueryTest(b.toString(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
