package nl.intrix83.tutorials.transactional.fun.products.isolation;

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
    public void addTenProducts() {
        for (int i = 0; i < 10; i++) {
            productRepository.save(new Product(null, "Beer " + i));
            if (i == 3) {
                throw new RuntimeException("Third of ten");
            }
        }
    }
}
