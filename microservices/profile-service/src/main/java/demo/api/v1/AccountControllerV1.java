package demo.api.v1;

import demo.account.Account;
import demo.customer.CustomerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1")
public class AccountControllerV1 {

    private AccountServiceV1 accountService;
    private CustomerClient customerClient;

    @Autowired
    public AccountControllerV1(AccountServiceV1 accountService, CustomerClient customerClient) {
        this.accountService = accountService;
        this.customerClient = customerClient;
    }

    @RequestMapping(path = "/accounts")
    public ResponseEntity getUserAccount() throws Exception {
        return Optional.ofNullable(accountService.getUserAccounts())
                .map(a -> new ResponseEntity<List<Account>>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("Accounts for user do not exist"));
    }

    @RequestMapping(path = "/customers")
    public ResponseEntity getCustomer(@RequestParam("username") String username) throws Exception {
        return Optional.ofNullable(customerClient.getCustomerResponse(username).getCustomer())
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("Accounts for user do not exist"));
    }
}
