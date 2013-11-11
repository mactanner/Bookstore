package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.customer.Customer;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class UserAuthentificationBean implements Serializable {

  //  private CustomerService customerService = CustomerService.getInstance(); TODO SB
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
//        try {
            if (retry < 3) {
                //TODO SB: customer = customerService.authenticateCustomer(email, password);
                return "orderSummaryPage";
            } else {
                return "toomanyretries";
            }
//        } catch (AuthenticationException ex) {
//            retry++;
//            Logger.getLogger(UserAuthentificationBean.class.getName()).log(Level.SEVERE, null, ex);
//            MessageFactory.info("org.books.Bookstore.RETRYCOUNT", retry);
//            return null;
//        }
    }

    public synchronized String logout() {
        customer = null;
        retry = 0;
        return "customSearch";
    }

    public synchronized boolean isLoggedIn() {
        return null != customer;
    }

}
