package ru.akirakozov.sd.refactoring;

import org.junit.jupiter.api.Test;

public class DbDriverTest extends DbTest {

    @Test
    public void testExecuteGet() {
        DbDriver driver = new DbDriver(Main.DB_PATH);
        int[] counter = new int[1];

        driver.executeGet("SELECT * FROM PRODUCT", rs -> {
            while (rs.next()) {
                counter[0]++;
            }
        });

        // test that GET receives three records
        assert counter[0] == 3;
        // and this is enough, because there is very unbelievable situation if data in records just change
    }

    @Test
    public void testExecuteModify() {
        DbDriver driver = new DbDriver(Main.DB_PATH);

        driver.executeUpdate("INSERT INTO PRODUCT (name, price) VALUES ('monitor', 1100);");

        assertDbContents(driver,
                new String[]{"apple", "coffee", "computer", "monitor"},
                new Integer[]{1, 10, 100, 1100});
    }
}
