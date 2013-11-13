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

    public String login() {
        try {
            if (retry < 3) {
                customer = customerService.authenticateCustomer(email, password);
                retry = 0;
                return "orderSummaryPage";
            } else {
                return "toomanyretries";
            }
        } catch (AuthenticationException ex) {
            retry++;
            Logger.getLogger(UserAuthentificationBean.class.getName()).log(Level.SEVERE, null, ex);
            MessageFactory.info("org.books.Bookstore.RETRYCOUNT", retry);
            return null;
        }
    }

    public synchronized String logout() {
        customer = null;
        retry = 0;
        return "catalogSearch";
    }

    public synchronized boolean isLoggedIn() {
        return null != customer;
    }

    public synchronized boolean isLoggedOut() {
        return !isLoggedIn();
    }

}