package nl.intrix83.tutorials.transactional.fun.locking.pessimistic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        writeLockedProductRepository.save(new WriteLockedProduct(1L, "Mortgage"));
    }

    @Test
    public void writeLockProduct_shouldBeReadableByReadingService_whenNotLockedByLockingService_becauseDoneTooFast() throws InterruptedException {
        lockingService.findProductAndLockItByWriting("Mortgage", "LinearMortgage", 2000);
        readingService.readWriteLockedProduct(1L);
    }

    @Test
    public void writeLockProduct_shouldNotBeReadableByReadingService_whenLockedByLockingService() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Optional<WriteLockedProduct>> lock = executorService.submit(() -> lockingService.findProductAndLockItByWriting("Mortgage", "LinearMortgage", 300000));

        Future<Optional<WriteLockedProduct>> locked = executorService.submit(() -> {
            Thread.sleep(1000);
            return readingService.readWriteLockedProduct(1L);
        });

        while (!(lock.isDone() && locked.isDone())) {
            printStatus(lock, locked);
            Thread.sleep(300);
        }

        Optional<WriteLockedProduct> writeLockedProduct = lock.get();
        Optional<WriteLockedProduct> optionalWriteLockedProduct = locked.get();

        assertThat(optionalWriteLockedProduct).isNotPresent();
        assertThat(writeLockedProduct).isPresent();

        executorService.shutdown();
    }

    private Future<Optional<WriteLockedProduct>> printStatus(Future<Optional<WriteLockedProduct>> lock, Future<Optional<WriteLockedProduct>> locked) {
        System.out.println(
                String.format(
                        "future1 is %s and future2 is %s",
                        lock.isDone() ? "done" : "not done",
                        locked.isDone() ? "done" : "not done"
                )
        );
        return locked;
    }
}
