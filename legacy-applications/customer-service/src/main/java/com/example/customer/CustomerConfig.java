package com.example.customer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class CustomerConfig {

    @Value("${services.customer.uri}")
    private String customerUri;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.kennybastani.guides.customer_service");
        return marshaller;
    }

    @Bean
    public CustomerClient weatherClient(Jaxb2Marshaller marshaller) {
        CustomerClient client = new CustomerClient();
        client.setDefaultUri(String.format("%s/v1/customers", customerUri));
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}