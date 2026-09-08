package cz.wikimedia.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;

@Component
public class AuthorizationConfig {

    @Autowired
    private Environment env;

    @Autowired
    private UserAgent userAgent;

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        // Same error handler as the Spring Security default template
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        delegate.setRestOperations(userAgent.apply(restTemplate));

        return request -> {
            OAuth2User user = delegate.loadUser(request);
            if (Arrays.stream(Objects.requireNonNull(env.getProperty("TEMP_SUB")).split(",")).anyMatch(t -> t.equals(user.getName())))
                return user;
            else
                throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, "user not authorized to access", ""));
        };
    }
}
