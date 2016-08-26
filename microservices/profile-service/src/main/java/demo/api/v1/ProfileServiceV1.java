package demo.api.v1;

import demo.customer.CustomerClient;
import demo.profile.Profile;
import demo.profile.ProfileRepository;
import demo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceV1 {

    private ProfileRepository profileRepository;
    private CustomerClient customerClient;
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    public ProfileServiceV1(ProfileRepository profileRepository, CustomerClient customerClient,
                            OAuth2RestTemplate oAuth2RestTemplate) {
        this.profileRepository = profileRepository;
        this.customerClient = customerClient;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    public Profile getProfile() {

        // Check for the profile record
        Profile profile = null;

        // Get current authenticated user
        User user = oAuth2RestTemplate.getForObject("http://user-service/uaa/v1/me", User.class);

        if (user != null) {
            profile = profileRepository.getProfileByUsername(user.getUsername());
        }

        // If the profile does not exist in the repository, import it from the SOAP service
        if (profile == null && user != null) {

            // Request the customer record from the legacy customer SOAP service
            profile = Optional.ofNullable(customerClient.getCustomerResponse(user.getUsername())
                    .getCustomer())
                    .map(p -> new Profile(p.getFirstName(), p.getLastName(),
                            p.getEmail(), p.getUsername()))
                    .orElseGet(null);

            if (profile != null) {
                // Migrate the system of record for the profile to this microservice
                profile = profileRepository.save(profile);
            }
        }

        return profile;
    }
}
