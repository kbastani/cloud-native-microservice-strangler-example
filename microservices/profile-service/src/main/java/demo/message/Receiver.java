package demo.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kennybastani.guides.customer_service.UpdateCustomerResponse;
import demo.customer.CustomerClient;
import demo.profile.Profile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.rmi.UnexpectedException;


/**
 * This class is the crawler that receives messages from three different queues and performs
 * work serially to import the graph of users received from the Twitter API. If the rate limit
 * of the Twitter API is succeeded, the message will be re-inserted back into the queue and
 * the operation will retry once the rate limit has been reset.
 *
 * @author kbastani
 */
@Component
public class Receiver {

    private final Log log = LogFactory.getLog(Receiver.class);
    private final ObjectMapper objectMapper;
    private final CustomerClient customerClient;

    @Autowired
    public Receiver(ObjectMapper objectMapper, CustomerClient customerClient) {
        this.objectMapper = objectMapper;
        this.customerClient = customerClient;
    }

    @RabbitListener(queues = {"customer.update"})
    public void updateCustomer(String message) throws InterruptedException, IOException {
        Profile profile = objectMapper.readValue(message, Profile.class);

        try {
            // Update the customer service for the profile
            UpdateCustomerResponse response =
                    customerClient.updateCustomerResponse(profile);

            if (!response.isSuccess()) {
                String errorMsg =
                        String.format("Could not update customer from profile for %s",
                                profile.getUsername());
                log.error(errorMsg);
                throw new UnexpectedException(errorMsg);
            }
        } catch (Exception ex) {
            // Throw AMQP exception and redeliver the message
            throw new AmqpIllegalStateException("Customer service update failed", ex);
        }
    }

}
