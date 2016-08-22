package demo.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/v1")
public class ProfileControllerV1 {

    private ProfileServiceV1 profileService;

    @Autowired
    public ProfileControllerV1(ProfileServiceV1 profileService) {
        this.profileService = profileService;
    }

    @RequestMapping(path = "/profiles")
    public ResponseEntity getProfile(@RequestParam("username") String username) throws Exception {
        return Optional.ofNullable(profileService.getProfile(username))
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("Profile for user does not exist"));
    }
}
