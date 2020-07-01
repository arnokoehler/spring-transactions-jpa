package nl.intrix83.tutorials.transactional.fun.products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionalProductService {

    private final ProductRepository productRepository;

    public void addProduct() {
        productRepository.save(new Product(null, "Beer"));
        throw new RuntimeException();
    }

    public void addTenProducts() {
        for (int i = 0; i < 10; i++) {
            productRepository.save(new Product(null, "Beer " + i));
            if (i == 5) {
                throw new RuntimeException();
            }
        }
    }
}
