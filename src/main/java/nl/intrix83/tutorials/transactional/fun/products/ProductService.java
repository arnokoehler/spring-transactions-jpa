package nl.intrix83.tutorials.transactional.fun.products;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct() {
        productRepository.save(new Product(null, "Beer"));
        throw new RuntimeException("One is enough");
    }

    public void addTenProducts() {
        for (int i = 0; i < 10; i++) {
            productRepository.save(new Product(null, "Beer " + i));
            if (i == 3) {
                throw new RuntimeException("Third of ten");
            }
        }
    }
}
