package com.example.customer;


import com.kennybastani.guides.customer_service.Customer;
import com.kennybastani.guides.customer_service.GetCustomerRequest;
import com.kennybastani.guides.customer_service.GetCustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Optional;

@Endpoint
public class CustomerEndpoint {
    private static final String NAMESPACE_URI = "http://kennybastani.com/guides/customer-service";

    private OAuth2RestOperations restTemplate;

    @Autowired
    public CustomerEndpoint(OAuth2RestOperations oAuth2RestTemplate) {
        this.restTemplate = oAuth2RestTemplate;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCustomerRequest")
    @ResponsePayload
    public GetCustomerResponse getCustomer(@RequestPayload GetCustomerRequest request) {
        GetCustomerResponse response = new GetCustomerResponse();

        // Get customer object from profile microservice
        response.setCustomer(Optional.ofNullable(restTemplate.getForObject("http://profile-service/v1/profiles?username={username}",
                Customer.class, request.getUsername()))
                .map(c -> c).orElse(null));

        return response;
    }
}