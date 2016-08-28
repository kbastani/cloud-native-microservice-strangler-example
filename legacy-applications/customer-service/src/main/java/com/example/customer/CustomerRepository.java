package com.example.customer;

import com.kennybastani.guides.customer_service.Customer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.xml.soap.SOAPException;

@Component
public class CustomerRepository {
    private Logger log = Logger.getLogger(CustomerRepository.class);
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

    public Integer updateCustomer(Customer customer) throws SOAPException {

        Assert.notNull(customer);

        int result;

        try {
            result = jdbcTemplate
                    .update("UPDATE customer SET first_name = ?, last_name = ?, email = ?, username = ? WHERE username = ?",
                            customer.getFirstName(), customer.getLastName(),
                            customer.getEmail(), customer.getUsername(), customer.getUsername());
        } catch (DataAccessException ex) {
            log.error("Could not update customer", ex);
            throw new SOAPException(ex);
        }

        return result;
    }
}
