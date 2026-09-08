package cz.wikimedia.stats;

import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class UserAgentTest {
    private static final String EXPECTED = "Wikinside/1.2.3 (test@example.org)";

    private static UserAgent userAgent() {
        Properties properties = new Properties();
        properties.setProperty("name", "Wikinside");
        properties.setProperty("version", "1.2.3");
        properties.setProperty("contact", "test@example.org");
        return new UserAgent(new BuildProperties(properties));
    }

    @Test
    void valueContainsNameVersionAndContact() {
        assertEquals(EXPECTED, userAgent().value());
    }

    @Test
    void applySetsUserAgentHeaderOnRequests() {
        RestTemplate restTemplate = userAgent().apply(new RestTemplate());
        MockRestServiceServer server = MockRestServiceServer.bindTo(restTemplate).build();
        server.expect(requestTo("https://example.invalid/profile"))
                .andExpect(header(HttpHeaders.USER_AGENT, EXPECTED))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));

        restTemplate.getForObject("https://example.invalid/profile", String.class);

        server.verify();
    }
}
