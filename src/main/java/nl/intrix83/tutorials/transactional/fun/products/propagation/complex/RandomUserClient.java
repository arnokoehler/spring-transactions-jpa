package nl.intrix83.tutorials.transactional.fun.products.propagation.complex;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RandomUserClient {

    public String getdata() {
        String fooResourceUrl = "https://randomuser.me/api/";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);

        return response.getBody();
    }

}
