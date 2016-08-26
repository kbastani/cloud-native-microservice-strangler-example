package demo.customer;

import com.kennybastani.guides.customer_service.GetCustomerRequest;
import com.kennybastani.guides.customer_service.GetCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class CustomerClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(CustomerClient.class);

    private static final String GET_CUSTOMER_NAMESPACE =
            "http://kennybastani.com/guides/customer-service/getCustomerRequest";

    public GetCustomerResponse getCustomerResponse(String username) {

        GetCustomerRequest request = new GetCustomerRequest();
        request.setUsername(username);

        log.info("Requesting customer for " + username);

        return (GetCustomerResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                        String.format("%s/v1/customers", this.getDefaultUri()),
                        request,
                        new SoapActionCallback(GET_CUSTOMER_NAMESPACE));
    }
}
