package nl.intrix83.tutorials.transactional.fun.products;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
public class TransactionalProductServiceTest extends TestBase {

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
