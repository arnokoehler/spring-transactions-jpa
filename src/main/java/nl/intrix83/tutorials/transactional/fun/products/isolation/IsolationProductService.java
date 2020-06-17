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
     *
     * - dirty reads        READ_UNCOMMITTED
     * - data is modified during read by other thread
     *
     * - Nonrepeatablereads READ_UNCOMMITTED READ_COMMITTED
     * - rollback happened on 1 and therefor modification on 2 is corrupt
     *
     * - phantom reads      READ_UNCOMMITTED READ_COMMITTED SERIALIZABLE
     * - phantom read = reaad 10 records of a hundred read the next 10 and geet some overlap because inserts in between
     *
     *
     * T1 ---- 100 --------- 110 ------> 20
     *
     * T2 ------------10 --------------> 10 ????
     **/
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void addHundredProducts() {
        for (int i = 0; i < 100; i++) {
            Product s = new Product();
            s.setName("Beer " + i);
            productRepository.save(s);
        }
    }

    /* Hypothese: solve problems with versions and you get less errors */


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

    @Transactional(readOnly = false)
    public List<Product> getAll() {
        return productRepository.findAll();
    }
}
