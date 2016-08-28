package com.example.customer;


import com.kennybastani.guides.customer_service.GetCustomerRequest;
import com.kennybastani.guides.customer_service.GetCustomerResponse;
import com.kennybastani.guides.customer_service.UpdateCustomerRequest;
import com.kennybastani.guides.customer_service.UpdateCustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.soap.SOAPException;

@Endpoint
public class CustomerEndpoint {
    private static final String NAMESPACE_URI = "http://kennybastani.com/guides/customer-service";

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerEndpoint(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCustomerRequest")
    @ResponsePayload
    public GetCustomerResponse getCustomer(@RequestPayload GetCustomerRequest request) {
        GetCustomerResponse response = new GetCustomerResponse();
        response.setCustomer(customerRepository.findCustomer(request.getUsername()));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateCustomerRequest")
    @ResponsePayload
    public UpdateCustomerResponse updateCustomer(@RequestPayload UpdateCustomerRequest request)
            throws SOAPException {
        UpdateCustomerResponse response = new UpdateCustomerResponse();
        response.setSuccess(customerRepository.updateCustomer(request.getCustomer()) > 0);
        return response;
    }
}