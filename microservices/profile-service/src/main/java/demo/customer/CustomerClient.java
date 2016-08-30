package demo.customer;

import com.kennybastani.guides.customer_service.*;
import demo.profile.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class CustomerClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(CustomerClient.class);

    private static final String ROOT_NAMESPACE = "http://kennybastani.com/guides/customer-service/";
    private static final String GET_CUSTOMER_NAMESPACE = "getCustomerRequest";
    private static final String UPDATE_CUSTOMER_NAMESPACE = "updateCustomerRequest";

    public GetCustomerResponse getCustomerResponse(String username) {

        GetCustomerRequest request = new GetCustomerRequest();
        request.setUsername(username);

        log.info("Requesting customer for " + username);

        return (GetCustomerResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                        String.format("%s/v1/customers", this.getDefaultUri()),
                        request,
                        new SoapActionCallback(ROOT_NAMESPACE + GET_CUSTOMER_NAMESPACE));
    }

    public UpdateCustomerResponse updateCustomerResponse(Profile profile) {

        UpdateCustomerRequest request = new UpdateCustomerRequest();

        Customer customer = new Customer();
        customer.setFirstName(profile.getFirstName());
        customer.setLastName(profile.getLastName());
        customer.setEmail(profile.getEmail());
        customer.setUsername(profile.getUsername());

        request.setCustomer(customer);

        log.info("Updating customer for " + customer.getUsername());

        return (UpdateCustomerResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                        String.format("%s/v1/customers", this.getDefaultUri()),
                        request,
                        new SoapActionCallback(ROOT_NAMESPACE + UPDATE_CUSTOMER_NAMESPACE));
    }
}
