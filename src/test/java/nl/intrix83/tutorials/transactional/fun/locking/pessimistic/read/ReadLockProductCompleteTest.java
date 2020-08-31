package nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;
import nl.intrix83.tutorials.transactional.fun.products.TestBase;
import nl.intrix83.tutorials.transactional.fun.products.propagation.NestedOuterTransactionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(statements = { "CREATE TABLE product (id SERIAL, name VARCHAR (255));\n"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = { "DROP TABLE product;\n"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ReadLockProductCompleteTest extends TestBase {

    @Autowired
    private NestedOuterTransactionService nestedOuterTransactionService;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void before() {
        productRepository.deleteAll();
    }

    @Test
    public void shouldCommit() {
        nestedOuterTransactionService.addProduct(false, false);
        assertThat(productRepository.findAll()).hasSize(11);
    }

    @Test
    public void shouldRoleBackAll() {
        assertThatThrownBy(() -> nestedOuterTransactionService.addProduct(true, false)) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(0);
    }

    @Test
    public void shouldRoleBackInnerOnly() {
        assertThatThrownBy(() -> nestedOuterTransactionService.addProduct(false, true)) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(1);

    }
}
