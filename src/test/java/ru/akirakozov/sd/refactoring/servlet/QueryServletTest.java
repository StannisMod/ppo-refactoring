package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.akirakozov.sd.refactoring.DbDriver;
import ru.akirakozov.sd.refactoring.DbDriverTest;
import ru.akirakozov.sd.refactoring.Main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

import static org.mockito.Mockito.when;

public class QueryServletTest {

    @Mock
    private HttpServletRequest request;

    private AutoCloseable mockitoObj;

    private CachingResponse response;

    private QueryServlet servlet;

    @BeforeEach
    public void prepareMocks() {
        mockitoObj = MockitoAnnotations.openMocks(this);
        response = new CachingResponse();
        servlet = new QueryServlet(new DbDriver(Main.DB_PATH));
        DbDriverTest.prepareTestDB();
    }

    @AfterEach
    public void closeMocks() throws Exception {
        mockitoObj.close();
    }

    @AfterAll
    public static void clearDB() {
        DbDriverTest.clearDB();
    }

    private void runQueryTest(String command, int status) throws IOException {
        when(request.getParameter("command")).thenReturn(command);

        servlet.doGet(request, response);

        response.assertStatus(status);
    }

    private void runQueryTest(String command, String expected) throws IOException {
        when(request.getParameter("command")).thenReturn(command);

        servlet.doGet(request, response);

        response.assertStatus(HttpServletResponse.SC_OK);
        response.assertContains(expected);
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
