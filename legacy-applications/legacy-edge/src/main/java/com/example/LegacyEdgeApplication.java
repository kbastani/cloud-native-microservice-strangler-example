package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
@EnableEurekaClient
@EnableOAuth2Client
@EnableHystrix
public class LegacyEdgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegacyEdgeApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("security.oauth2.client")
    public ClientCredentialsResourceDetails oauth2ClientCredentialsResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @LoadBalanced
    @Bean
    public OAuth2RestTemplate loadBalancedOauth2RestTemplate(
            ClientCredentialsResourceDetails resource) {
        ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
        clientCredentialsResourceDetails.setAccessTokenUri(resource.getAccessTokenUri());
        clientCredentialsResourceDetails.setClientId(resource.getClientId());
        clientCredentialsResourceDetails.setClientSecret(resource.getClientSecret());
        clientCredentialsResourceDetails.setClientAuthenticationScheme(resource.getClientAuthenticationScheme());
        clientCredentialsResourceDetails.setScope(resource.getScope());
        clientCredentialsResourceDetails.setGrantType(resource.getGrantType());
        OAuth2RestTemplate auth2RestTemplate = new OAuth2RestTemplate(clientCredentialsResourceDetails);
        auth2RestTemplate.setAccessTokenProvider(new ClientCredentialsAccessTokenProvider());
        return auth2RestTemplate;
    }
}
