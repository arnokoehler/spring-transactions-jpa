package nl.intrix83.tutorials.transactional.fun.products;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest extends TestBase {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldCreateOneAndNotCareAboutException() {
        assertThatThrownBy(() -> productService.addProduct()) //
                .isInstanceOf(RuntimeException.class) //
                .hasMessageContaining("One is enough");

        assertThat(productRepository.findAll()).hasSize(1);
    }

    @Test
    public void shouldCreateTenAndNotCareAboutException() {
        assertThatThrownBy(() -> productService.addTenProducts()) //
                .isInstanceOf(RuntimeException.class) //
                .hasMessageContaining("Third of ten");

        assertThat(productRepository.findAll()).hasSize(5);
    }
}
