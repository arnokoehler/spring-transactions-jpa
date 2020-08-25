package nl.intrix83.tutorials.transactional.fun.products.propagation.complex;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class User {

    private String name;

    private String lastname;

    private String role;

}
