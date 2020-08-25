package nl.intrix83.tutorials.transactional.fun.products.propagation.complex;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nl.intrix83.tutorials.transactional.fun.products.ProductRepository;
import nl.intrix83.tutorials.transactional.fun.products.TestBase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@RunWith(SpringRunner.class)
@Sql(statements = { "CREATE TABLE product (id SERIAL, name VARCHAR (255));\n" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(statements = { "DROP TABLE product;\n" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProductWrappingRestClientServiceTest extends TestBase {

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public ProductWrappingRestClientService productWrappingRestClientService;

    @Test
    public void shouldRunRestCallInsideTransaction() {
        Mockito.when(restTemplate.getForEntity("https://randomuser.me/api/", User.class))
                .thenReturn(new ResponseEntity(getUser(), HttpStatus.OK));

        productWrappingRestClientService.addTenProducts(0);

        assertThat(productRepository.findAll()).hasSize(10);
    }

    @Test
    public void shouldRunRestCallInsideTransactionAndDoFailOnRestCall() {
        Mockito.when(restTemplate.getForEntity("https://randomuser.me/api/", User.class))
                .thenThrow(new RestClientException("fake error"));

        assertThatThrownBy(() -> productWrappingRestClientService.addTenProducts(0)) //
                .isInstanceOf(RestClientException.class);

        assertThat(productRepository.findAll()).hasSize(0);
    }

    @Test
    public void shouldRunRestCallInsideTransactionThatIsRolledBackByError() {
        Mockito.when(restTemplate.getForEntity("https://randomuser.me/api/", User.class))
                .thenReturn(new ResponseEntity(getUser(), HttpStatus.OK));

        assertThatThrownBy(() -> productWrappingRestClientService.addTenProducts(5)) //
                .isInstanceOf(RuntimeException.class);

        assertThat(productRepository.findAll()).hasSize(0);
    }

    private User getUser() {
        return User.builder()
                .name("sjaak")
                .lastname("zwart")
                .role("user")
                .build();
    }
}
