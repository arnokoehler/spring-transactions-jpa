package nl.intrix83.tutorials.transactional.fun.products.propagation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;
import nl.intrix83.tutorials.transactional.fun.products.propagation.NestedTransactionPropagationService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NestedTransactionPropagationServiceTest {

    @Autowired
    private NestedTransactionPropagationService nestedTransactionPropagationService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldRoleBackAll() {
        assertThatThrownBy(() -> nestedTransactionPropagationService.addProduct(true, false)) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(0);
    }

    // Does not work with JPA only with JDBC DataSourceTransactionManager
    @Test
    public void shouldRoleBackInnerOnly() {
        assertThatThrownBy(() -> nestedTransactionPropagationService.addProduct(false, true)) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(0);
//        assertThat(productRepository.findAll()).hasSize(1);

    }
}
