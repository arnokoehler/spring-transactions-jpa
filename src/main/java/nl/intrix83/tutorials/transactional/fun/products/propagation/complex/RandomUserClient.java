package nl.intrix83.tutorials.transactional.fun.products.propagation.complex;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RandomUserClient {

    private static final String HTTPS_RANDOMUSER_ME_API = "https://randomuser.me/api/";

    private final RestTemplate restTemplate;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public User getdata() {

        ResponseEntity<User> response = restTemplate.getForEntity(HTTPS_RANDOMUSER_ME_API, User.class);

        return response.getBody();
    }

}
