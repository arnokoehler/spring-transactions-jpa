package nl.intrix83.tutorials.transactional.fun.products;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

import org.awaitility.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AsyncServiceTest {

    @Autowired
    private AsyncService asyncService;

    @Test
    public void test() {
        asyncService.initialize();

        await().until(asyncService::isInitialized);
        asyncService.addValue();

        await().atLeast(Duration.FIVE_HUNDRED_MILLISECONDS);

        await().until(asyncService::getValue, equalTo(100));
    }
}
