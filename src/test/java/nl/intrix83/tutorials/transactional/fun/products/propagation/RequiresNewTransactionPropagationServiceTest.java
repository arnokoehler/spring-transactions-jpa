package nl.intrix83.tutorials.transactional.fun.products.propagation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RequiresNewTransactionPropagationServiceTest {

    @Autowired
    private RequiresNewTransactionPropagationService requiresNewTransactionPropagationService;

    @Autowired
    private ProductRepository productRepository;

    // Only works with JtaTransactionManager ?!
    @Test
    public void shouldRoleBackOuterOnly() {
        assertThatThrownBy(() -> requiresNewTransactionPropagationService.addProduct(true, false)) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(5);
    }

    @Test
    public void shouldRoleBackBoth() {
        assertThatThrownBy(() -> requiresNewTransactionPropagationService.addProduct(false, true)) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(0);
    }
}
