package nl.intrix83.tutorials.transactional.fun.products.propagation;
import nl.intrix83.tutorials.transactional.fun.products.Product;
import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NestedInnerTransactionService {

    private final ProductRepository productRepository;

    @Transactional(propagation = Propagation.NESTED)
    public void addTenProducts(final boolean inner) {
        for (int i = 0; i < 10; i++) {
            productRepository.save(new Product(null, "Beer " + i));
            if (inner && i == 5) {
                throw new RuntimeException();
            }
        }
    }
}
