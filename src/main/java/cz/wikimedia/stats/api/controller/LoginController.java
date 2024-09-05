package cz.wikimedia.stats.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class LoginController {
    @GetMapping("/login/login-only")
    public ResponseEntity<Void> loginOnly(HttpServletRequest request) throws URISyntaxException {
        return ResponseEntity.status(HttpStatus.FOUND)
                             .location(new URI(request.getHeader("referer")))
                             .build();
    }

    @GetMapping("/oauth2")
    public ResponseEntity<Void> check() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
