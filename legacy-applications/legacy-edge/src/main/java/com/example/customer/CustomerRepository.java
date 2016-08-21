package com.example.customer;

import com.kennybastani.guides.customer_service.Customer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerRepository {
    private static final List<Customer> customers = new ArrayList<Customer>();

    @PostConstruct
    public void initData() {
        Customer johnDoe = new Customer();
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setEmail("john.doe@example.com");
        johnDoe.setUsername("user");
        customers.add(johnDoe);
    }

    public Customer findCustomer(String username) {
        Assert.notNull(username);

        Customer result = null;

        for (Customer country : customers) {
            if (username.equals(country.getUsername())) {
                result = country;
            }
        }

        return result;
    }
}
