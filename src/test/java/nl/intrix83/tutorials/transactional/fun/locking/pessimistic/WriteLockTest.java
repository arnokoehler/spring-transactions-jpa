package nl.intrix83.tutorials.transactional.fun.locking.pessimistic;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProductRepository;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write.WriteLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write.WriteLockedProductRepository;
import nl.intrix83.tutorials.transactional.fun.products.TestBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(statements = { "CREATE TABLE write_locked_product (id SERIAL, name VARCHAR (255));\n" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = { "DROP TABLE write_locked_product;\n" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class WriteLockTest extends TestBase {

    @Autowired
    private LockingService lockingService;

    @Autowired
    private ReadingService readingService;

    @Autowired
    private WriteLockedProductRepository writeLockedProductRepository;

    @Before
    public void before() {
        writeLockedProductRepository.save(new WriteLockedProduct(1L, "sjaak"));
    }

    @Test
    public void readLockProduct_shouldNotBeReadableByReadingService_whenLockedByLockingService() throws InterruptedException {
        lockingService.findProductAndLockItByWriting("piet", "kees", 2000);
        readingService.readReadLockedProduct("piet");
    }

}
