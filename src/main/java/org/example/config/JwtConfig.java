package org.example.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {

    private String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private long expiration = 86400000; // 24 hours
    private long refreshExpiration = 604800000; // 7 days
    private String tokenPrefix = "Bearer ";
    private String headerString = "Authorization";
}
