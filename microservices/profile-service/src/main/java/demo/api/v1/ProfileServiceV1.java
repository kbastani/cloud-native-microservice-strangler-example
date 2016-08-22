package demo.api.v1;

import demo.customer.CustomerClient;
import demo.profile.Profile;
import demo.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceV1 {

    private ProfileRepository profileRepository;
    private CustomerClient customerClient;

    @Autowired
    public ProfileServiceV1(ProfileRepository profileRepository, CustomerClient customerClient) {
        this.profileRepository = profileRepository;
        this.customerClient = customerClient;
    }

    public Profile getProfile(String username) {
        // Check for the profile record
        Profile profile = profileRepository.getProfileByUsername(username);

        if(profile == null) {
            // Request the customer record from the legacy customer SOAP service
            profile = Optional.ofNullable(customerClient.getCustomerResponse(username).getCustomer())
                    .map(p -> new Profile(p.getFirstName(), p.getLastName(), p.getEmail(), p.getUsername()))
                    .orElseGet(null);

            if(profile != null) {
                // Migrate the system of record for the profile to this microservice
                profile = profileRepository.save(profile);
            }
        }

        return profile;
    }
}
