package nl.intrix83.tutorials.transactional.fun.products.isolation;

import nl.intrix83.tutorials.transactional.fun.products.Product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/greeting")
    public Product greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Product();
    }
}
