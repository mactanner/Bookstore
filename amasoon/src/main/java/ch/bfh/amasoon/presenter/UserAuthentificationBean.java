package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.commons.MessageFactory;
import ch.bfh.amasoon.model.customer.AuthenticationException;
import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class UserAuthentificationBean implements Serializable {

    private CustomerService customerService = CustomerService.getInstance();
    private Customer customer;

    private int retry = 0;
    private String email;
    private String password;

    public Customer getCustomer() {
        return customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login(boolean isCartEmpty) {
        try {
            customer = customerService.authenticateCustomer(email, password);
            retry = 0;
            if (isCartEmpty) {
                return "catalogSearch";
            } else {
                return "orderSummary";

            }
        } catch (AuthenticationException ex) {
            Logger.getLogger(UserAuthentificationBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public synchronized String logout() {
        customer = null;
        return "catalogSearch";
    }

    public boolean isUserLoggedIn() {
        return customer != null;
    }

}
