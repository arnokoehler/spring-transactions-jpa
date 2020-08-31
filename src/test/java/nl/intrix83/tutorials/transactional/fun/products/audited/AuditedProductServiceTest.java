package nl.intrix83.tutorials.transactional.fun.products.audited;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import nl.intrix83.tutorials.transactional.fun.products.TestBase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(scripts = {"audited_product.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = { "DROP TABLE audited_product;\n"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuditedProductServiceTest extends TestBase {

    @Autowired
    private AuditedProductRepository auditedProductRepository;

    @Autowired
    private AuditedProductService auditedProductService;

    private AuditedProduct save;

    @Before
    public void before() {
        save = auditedProductRepository.save(getProduct());
    }

    @Test
    public void shouldMakeHistoryRecoreds() {
        Optional<AuditedProduct> auditedProduct = auditedProductService.find(save.getId());
        auditedProduct.map(replaceName("product 2"));
        auditedProductService.update(auditedProduct.get());
        auditedProduct.map(replaceName("product 3"));
        auditedProductService.update(auditedProduct.get());

        List<AuditedProduct> history = auditedProductService.getHistory(save.getId());
        assertThat(history).hasSize(3);

    }

    private AuditedProduct getProduct() {
        AuditedProduct auditedProduct = new AuditedProduct();
        auditedProduct.setLongName("Some long name");
        auditedProduct.setName("Product");
        auditedProduct.setOrderNumber(345);
        auditedProduct.setQuantity(10);
        return auditedProduct;
    }

    @org.jetbrains.annotations.NotNull
    private Function<AuditedProduct, AuditedProduct> replaceName(final String newName) {
        return it -> {
            it.setName(newName);
            return it;
        };
    }
}
