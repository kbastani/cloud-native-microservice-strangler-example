package demo.profile;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends PagingAndSortingRepository<Profile, Long> {
    Profile getProfileByUsername(@Param("username") String username);
}
