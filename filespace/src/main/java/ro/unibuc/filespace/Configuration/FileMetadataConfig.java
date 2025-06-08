package ro.unibuc.filespace.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;


@Configuration
@Slf4j
public class FileMetadataConfig {
    @Bean
    @LoadBalanced
    @RequestScope
    public RestTemplate keycloakRestTemplate(HttpServletRequest inReq) {
        final String authHeader = inReq.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Incoming Authorization Header: {}", authHeader); // Add logging

        final RestTemplate restTemplate = new RestTemplate();

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            restTemplate.getInterceptors().add((outReq, bytes, exec) -> {
                outReq.getHeaders().set(HttpHeaders.AUTHORIZATION, authHeader);
                log.info("Outgoing request to {} with auth header", outReq.getURI()); // Add logging
                return exec.execute(outReq, bytes);
            });
        } else {
            log.warn("No valid Authorization header found in request");
        }

        return restTemplate;
    }
}
