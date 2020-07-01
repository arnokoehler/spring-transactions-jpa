package nl.intrix83.tutorials.transactional.fun.products.propagation;

import nl.intrix83.tutorials.transactional.fun.products.Product;
import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequiresNewOuterTransactionService {

    private final ProductRepository productRepository;

    private final RequiresNewInnerTransactionService addTenProducts;

    @Transactional
    public void addProduct(final boolean outer, final boolean inner) {
        productRepository.save(new Product(null, "Beer"));
        addTenProducts.addTenProducts(inner);
        if (outer) {
            throw new RuntimeException();
        }
    }


}
