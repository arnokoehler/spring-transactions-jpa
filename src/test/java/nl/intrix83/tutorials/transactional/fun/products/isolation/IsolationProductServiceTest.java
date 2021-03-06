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
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(statements = { "CREATE TABLE product (id SERIAL, name VARCHAR (255));\n"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = { "DROP TABLE product;\n"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IsolationProductServiceTest extends TestBase {

    @Autowired
    private IsolationProductService isolationProductService;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void before() {
        productRepository.deleteAll();

    }

    // So this is an optimization for (as the best example) Hibernate but there is no guarantee that it is indeed faster

//    @Repeat(value = 10)
    @Test
    public void readOnlyShouldBeFasterButIsnt() {
        isolationProductService.addHundredProducts();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<Product> allFast = isolationProductService.getAllFast();
            assert allFast.size() == 100;
        }
        long end = System.currentTimeMillis();
        long timeReadOnlyFast = end - start;

        long normalStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            List<Product> allNormal = isolationProductService.getAll();
            assert allNormal.size() == 100;
        }
        long normalEnd = System.currentTimeMillis();
        long timeNormal = normalEnd - normalStart;

        assertThat(timeReadOnlyFast).isLessThan(timeNormal);
    }


}
