package nl.intrix83.tutorials.transactional.fun.locking.pessimistic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import nl.intrix83.tutorials.transactional.fun.TransactionalFunApplication;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProductRepository;
import nl.intrix83.tutorials.transactional.fun.products.TestBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(statements = { "CREATE TABLE read_locked_product (id SERIAL, name VARCHAR (255));\n" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = { "DROP TABLE read_locked_product;\n" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ReadLockTest extends TestBase {

    @Autowired
    private LockingService lockingService;

    @Autowired
    private ReadLockedProductRepository readLockedProductRepository;

    @Autowired
    private ReadingService readingService;

    @Before
    public void before() {
        readLockedProductRepository.save(new ReadLockedProduct(1L, "Mortgage"));
    }

    @Test
    public void readLockProduct_shouldBeReadableByReadingService_whenNotLockedByLockingService() throws InterruptedException {
        // these calls are done serial and therefore etc.
        Optional<ReadLockedProduct> piet = lockingService.findProductAndLockIt(1L, 2000);
        Optional<ReadLockedProduct> piet1 = readingService.readReadLockedProduct(1L);

        assertThat(piet.get()).isNotNull();
        assertThat(piet1.get()).isNotNull();

    }

    @Test
    public void readLockProduct_shouldBeReadableByReadingService_whenReadByLockingService() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Optional<ReadLockedProduct>> lock = executorService.submit(() -> lockingService.findProductAndLockIt(1L, 5000));

        Future<Optional<ReadLockedProduct>> locked = executorService.submit(() -> {
            Thread.sleep(1000);
            return readingService.readReadLockedProduct(1L);
        });

        while (!(lock.isDone() && locked.isDone())) {
            printStatus(lock, locked);
            Thread.sleep(300);
        }


        Optional<ReadLockedProduct> readLockedProduct = lock.get();
        Optional<ReadLockedProduct> optionalReadLockedProduct = locked.get();

        assertThat(optionalReadLockedProduct).isNotPresent();
        assertThat(readLockedProduct).isPresent();

        executorService.shutdown();
    }

    @Test
    public void readLockProduct_shouldNotBeAbleToWriteByReadingService_whenReadByLockingService() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Optional<ReadLockedProduct>> lock = executorService.submit(() -> lockingService.findProductAndLockIt(1L, 5000));

        Future<Optional<ReadLockedProduct>> locked = executorService.submit(() -> {
            Thread.sleep(1000);
            return readingService.tryToFindAndUpdateLockedProduct(1L, "LinearMortgage");
        });

        while (!(lock.isDone() && locked.isDone())) {
            printStatus(lock, locked);
            Thread.sleep(300);
        }


        Optional<ReadLockedProduct> readLockedProduct = lock.get();
        Optional<ReadLockedProduct> optionalReadLockedProduct = locked.get();

        assertThat(optionalReadLockedProduct).isNotPresent();
        assertThat(readLockedProduct).isPresent();

        executorService.shutdown();
    }

    private Future<Optional<ReadLockedProduct>> printStatus(Future<Optional<ReadLockedProduct>> lock, Future<Optional<ReadLockedProduct>> locked) {
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
