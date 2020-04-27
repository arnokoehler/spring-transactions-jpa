package nl.intrix83.tutorials.transactional.fun.products.isolation;

import java.util.List;

import nl.intrix83.tutorials.transactional.fun.products.Product;
import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IsolationProductService {

    private final ProductRepository productRepository;

    /**
     * READ_COMMITTED best optimum
     *
     * Problems:
     * - dirty reads        READ_UNCOMMITTED
     * - repeatablereads    READ_UNCOMMITTED READ_COMMITTED
     * - phantom reads      READ_UNCOMMITTED READ_COMMITTED SERIALIZABLE
     *
     * T1 ---- 100 --------- 110 ------> 20
     *
     * T2 ------------10 --------------> 10 ????
     **/
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void addHundredProducts() {
        for (int i = 0; i < 100; i++) {
            productRepository.save(new Product(null, "Beer " + i));
        }
    }

    /**
     * readOnly = true
     *
     * only for pure gets this might be fast because the way it's handled (without transaction overhead)
     *
     * NOTE: A transaction manager which cannot interpret the read-only hint wil
     * not throw an exception when asked for a read-only transaction
     *
     * This might vary over different database implementations
     * @return
     */
    @Transactional(readOnly = true)
    public List<Product> getAllFast() {
       return productRepository.findAll();
    }

    @Transactional
    public List<Product> getAll() {
        return productRepository.findAll();
    }
}
