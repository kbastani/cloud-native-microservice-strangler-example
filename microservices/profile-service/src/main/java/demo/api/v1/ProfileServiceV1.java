package demo.api.v1;

import demo.customer.CustomerClient;
import demo.profile.Profile;
import demo.profile.ProfileRepository;
import demo.user.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProfileServiceV1 {

    private ProfileRepository profileRepository;
    private CustomerClient customerClient;
    private OAuth2RestTemplate oAuth2RestTemplate;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public ProfileServiceV1(ProfileRepository profileRepository, CustomerClient customerClient,
                            OAuth2RestTemplate oAuth2RestTemplate, AmqpTemplate amqpTemplate) {
        this.profileRepository = profileRepository;
        this.customerClient = customerClient;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.amqpTemplate = amqpTemplate;
    }

    public Profile getProfile(String username) {

        // Check for the profile record
        Profile profile;

        profile = profileRepository.getProfileByUsername(username);

        // If the profile does not exist in the repository, import it from the SOAP service
        if (profile == null) {
            // Request the customer record from the legacy customer SOAP service
            profile = Optional.ofNullable(customerClient.getCustomerResponse(username)
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

    public Profile updateProfile(Profile profile) throws IOException {

        Assert.notNull(profile);

        // Get current authenticated user
        User user = oAuth2RestTemplate.getForObject("http://user-service/uaa/v1/me", User.class);

        // Get current profile
        Profile currentProfile = getProfile(user.getUsername());

        if (currentProfile != null) {
            if (currentProfile.getUsername().equals(profile.getUsername())) {
                // Save the profile
                profile.setId(currentProfile.getId());
                profile.setCreatedAt(currentProfile.getCreatedAt());
                profile = profileRepository.save(profile);

                // Replicate the write to the legacy customer service
                amqpTemplate.convertAndSend("customer.update",
                        new ObjectMapper().writeValueAsString(profile));
            }
        }

        return profile;
    }
}
