package com.example.customer;

import com.kennybastani.guides.customer_service.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class CustomerRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Customer findCustomer(String username) {
        Assert.notNull(username);

        Customer result;

        result = jdbcTemplate
                .query("SELECT id, first_name, last_name, email, username FROM customer WHERE username = ?",
                        new Object[]{username},
                        (rs, rowNum) -> {
                            Customer customer = new Customer();
                            customer.setFirstName(rs.getString("first_name"));
                            customer.setLastName(rs.getString("last_name"));
                            customer.setEmail(rs.getString("email"));
                            customer.setUsername(rs.getString("username"));
                            return customer;
                        }).stream().findFirst().orElse(null);

        return result;
    }
}
