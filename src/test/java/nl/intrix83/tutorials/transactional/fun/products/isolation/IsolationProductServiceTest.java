package nl.intrix83.tutorials.transactional.fun.products.isolation;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import nl.intrix83.tutorials.transactional.fun.products.Product;
import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;
import nl.intrix83.tutorials.transactional.fun.products.TestBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IsolationProductServiceTest extends TestBase {

    @Autowired
    private IsolationProductService isolationProductService;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void before() {
        productRepository.deleteAll();
    }

    @Test
    public void shouldCommit() {
        isolationProductService.addHundredProducts();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<Product> allFast = isolationProductService.getAllFast();
            assert allFast.size() == 100;
        }
        long end = System.currentTimeMillis();
        long timeReadOnly = end - start;

        long normalStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<Product> allFast = isolationProductService.getAll();
            assert allFast.size() == 100;
        }
        long normalEnd = System.currentTimeMillis();
        long timeNormal = normalEnd - normalStart;

        assertThat(timeReadOnly).isLessThan(timeNormal);
    }


}
