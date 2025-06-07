package ro.unibuc.filespace.Configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtTokenInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalStateException("No authentication found in SecurityContext");
        }

        // Handle UsernamePasswordAuthenticationToken (for seeding)
        String token = (String) authentication.getCredentials();
        if (token != null) {
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            return execution.execute(request, body);
        }

        // Handle JWT (for OAuth2)
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue());
            return execution.execute(request, body);
        }

        throw new IllegalStateException("Unsupported authentication type: " + authentication.getClass());
    }
}