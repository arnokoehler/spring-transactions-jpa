package nl.intrix83.tutorials.transactional.fun.products.propagation.complex;

import nl.intrix83.tutorials.transactional.fun.products.Product;
import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductWrappingRestClientService {

    private final ProductRepository productRepository;

    private final RandomUserClient randomUserClient;

    public void addTenProducts(int throwErrorOnItem) {
        for (int i = 0; i < 10; i++) {
            User getdata = randomUserClient.getdata();
            log.info("for user " + getdata);
            productRepository.save(new Product(null, "Beer " + i));
            if (throwErrorOnItem > 0 && i == throwErrorOnItem) {
                throw new RuntimeException();
            }
        }
    }
}
