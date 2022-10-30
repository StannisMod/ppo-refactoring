package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import ru.akirakozov.sd.refactoring.DbTest;

import javax.servlet.http.HttpServletRequest;

public class ServletTest extends DbTest {

    @Mock
    private HttpServletRequest request;

    private CachingResponse response;

    @BeforeEach
    public void prepareResponse() {
        response = new CachingResponse();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public CachingResponse getResponse() {
        return response;
    }
}
