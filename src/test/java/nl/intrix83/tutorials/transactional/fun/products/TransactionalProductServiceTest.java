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
public class TransactionalProductServiceTest {

    @Autowired
    private TransactionalProductService transactionalProductService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldCreateOneAndNotCareAboutException() {
        assertThatThrownBy(() -> transactionalProductService.addProduct()) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(0);
    }

    @Test
    public void shouldCreateTenAndNotCareAboutException() {
        assertThatThrownBy(() -> transactionalProductService.addTenProducts()) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(0);
    }
}