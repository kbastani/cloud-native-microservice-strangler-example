package demo.api.v1;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import demo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceV1 {

    private OAuth2RestTemplate restTemplate;

    @Autowired
    public UserServiceV1(OAuth2RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand
    public User getUserByUsername(String username) {
        return Optional.ofNullable(restTemplate.getForObject("http://profile-service/v1/profiles?username={username}",
                User.class, username)).map(u -> u).orElse(null);
    }
}
