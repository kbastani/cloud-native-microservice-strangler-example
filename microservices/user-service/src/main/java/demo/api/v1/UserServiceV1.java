package demo.api.v1;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import demo.user.User;
import demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceV1 {

    private UserRepository userRepository;

    @Autowired
    public UserServiceV1(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @HystrixCommand
    public User getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findUserByUsername(username)).map(u -> u).orElse(null);
    }
}
