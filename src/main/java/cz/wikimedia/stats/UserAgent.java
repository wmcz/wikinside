package cz.wikimedia.stats;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds the User-Agent header for all outbound HTTP requests.
 *
 * The Wikimedia User-Agent policy requires a distinctive name and a contact.
 * The Wikimedia edge returns HTTP 403 to library default user agents (T400119).
 */
@Component
public class UserAgent {
    private final String value;

    public UserAgent(BuildProperties properties) {
        this.value = properties.getName() + "/" + properties.getVersion() + " (" + properties.get("contact") + ")";
    }

    /**
     * @return the header value, for example {@code Wikinside/0.3.3 (info@wikimedia.cz)}
     */
    public String value() {
        return value;
    }

    /**
     * @return an interceptor that sets the User-Agent header on each request
     */
    public ClientHttpRequestInterceptor interceptor() {
        return (request, body, execution) -> {
            request.getHeaders().set(HttpHeaders.USER_AGENT, value);
            return execution.execute(request, body);
        };
    }

    /**
     * Add the User-Agent interceptor to the given template.
     *
     * @return the same template, for chaining
     */
    public RestTemplate apply(RestTemplate restTemplate) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
        interceptors.add(interceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
