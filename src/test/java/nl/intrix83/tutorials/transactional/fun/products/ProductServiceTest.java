package nl.intrix83.tutorials.transactional.fun.products;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.After;
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
    public void shouldCreateFirstNumberOfItemsAndNotCareAboutException() {
        assertThatThrownBy(() -> productService.addTenProducts()) //
                .isInstanceOf(RuntimeException.class) //
                .hasMessageContaining("Third of ten");

        assertThat(productRepository.findAll()).hasSize(4);
    }
}
