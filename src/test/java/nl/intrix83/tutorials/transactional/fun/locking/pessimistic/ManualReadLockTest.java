package nl.intrix83.tutorials.transactional.fun.locking.pessimistic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import nl.intrix83.tutorials.transactional.fun.products.Product;
import nl.intrix83.tutorials.transactional.fun.products.TestBase;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * Credits for most of this code go to Vlad Mihalcea's Hibernate Master class
 *
 * can't get it working don't know how to combine the Postgresql container with creating a SessionFactory in config.
 * See MyOtherConfig.java for a setup on how I think it should be done, but every time I run into new issues
 */
@Slf4j
public class ManualReadLockTest extends TestBase {

    public static final int WAIT_MILLIS = 500;

    private final CountDownLatch endLatch = new CountDownLatch(1);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
        Thread threadForTest = new Thread(r);
        threadForTest.setName("threadForTest");
        return threadForTest;
    });

    @Autowired
    private SessionFactory sf;

    @Test
    public void testPessimisticReadBlocksUpdate() throws InterruptedException {
        log.info("Test PESSIMISTIC_READ blocks UPDATE");
        testPessimisticLocking((session, product) -> {
            session.buildLockRequest(new LockOptions(LockMode.PESSIMISTIC_READ))
                    .lock(product);
            log.info("PESSIMISTIC_READ acquired");
        }, (session, product) -> {
            product.setName("");
            session.flush();
            log.info("Implicit lock acquired");
        });
    }

    protected void awaitOnLatch(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void doInTransaction(HibernateTransactionConsumer callable) {
        Session session = null;
        Transaction txn = null;
        try {
            session = sf.openSession();
            callable.beforeTransactionCompletion();
            txn = session.beginTransaction();

            callable.accept(session);
            txn.commit();
        } catch (RuntimeException e) {
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw e;
        } finally {
            callable.afterTransactionCompletion();
            if (session != null) {
                session.close();
            }
        }
    }

    @SuppressWarnings("java:S2925")
    protected <T> void executeAsync(Runnable callable, final Runnable completionCallback) {
        final Future future = executorService.submit(callable);
        new Thread(() -> {
            while (!future.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
            try {
                completionCallback.run();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }).start();
    }

    @SuppressWarnings("java:S2925")
    protected void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void testPessimisticLocking(ProductLockRequestCallable primaryLockRequestCallable, ProductLockRequestCallable secondaryLockRequestCallable) {
        doInTransaction(session -> {
            try {
                Product product = (Product) session.get(Product.class, 1L);
                primaryLockRequestCallable.lock(session, product);
                executeAsync(() -> {
                    doInTransaction(_session -> {
                        Product _product = (Product) _session.get(Product.class, 1L);
                        secondaryLockRequestCallable.lock(_session, _product);
                    });
                }, endLatch::countDown);
                sleep(WAIT_MILLIS);
            } catch (StaleObjectStateException e) {
                log.info("Optimistic locking failure: ", e);
            }
        });
        awaitOnLatch(endLatch);
    }

    private static interface ProductLockRequestCallable {

        void lock(Session session, Product product);
    }

    @FunctionalInterface
    protected interface HibernateTransactionConsumer extends Consumer<Session> {

        default void afterTransactionCompletion() {

        }

        default void beforeTransactionCompletion() {

        }
    }
}
