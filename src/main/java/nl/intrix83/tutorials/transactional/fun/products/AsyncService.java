package nl.intrix83.tutorials.transactional.fun.products;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import nl.intrix83.tutorials.transactional.fun.products.isolation.IsolationProductService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final int DELAY = 500;
    private final int INIT_DELAY = 2000;

    private final IsolationProductService isolationProductService;

    private Executor executor = Executors.newFixedThreadPool(4);
    private volatile boolean initialized = false;

    void initialize() {
        executor.execute(() -> {
            sleep(INIT_DELAY);
            initialized = true;
        });
    }

    boolean isInitialized() {
        return initialized;
    }

    void addValue() {
        throwIfNotInitialized();
        executor.execute(() -> {
            sleep(DELAY);
            isolationProductService.addHundredProducts();
        });
    }

    public int getValue() {
        throwIfNotInitialized();
        return isolationProductService.getAll().size();
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
    }

    private void throwIfNotInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Service is not initialized");
        }
    }
}
