package hristovski.nikola.auth.configuration;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import hristovski.nikola.auth.models.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("customUserDetailsService")
@Slf4j
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String SERVICE = "users-service";
    private final EurekaClient discoveryClient;
    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            InstanceInfo instanceInfo = discoveryClient.getNextServerFromEureka(SERVICE, false);
            log.info("The next server is: {}",instanceInfo.getHomePageUrl());

            String url = instanceInfo.getHomePageUrl() + username;

            ApplicationUser applicationUser = restTemplate.getForObject(url, ApplicationUser.class);

            if (applicationUser == null) {
                log.error("Failed to get user by username application user is null");
                throw new UsernameNotFoundException("Username: " + username + " not found");
            }

            log.info("User fetched successfully");

            return new User(applicationUser.getUsername(),
                    applicationUser.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(applicationUser.getRolesAsString()));

        } catch (Exception ex) {
            log.error("Failed to get user by username", ex);
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
    }
}
