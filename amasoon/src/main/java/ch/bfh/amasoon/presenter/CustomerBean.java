package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.commons.MessageFactory;
import ch.bfh.amasoon.model.customer.AuthenticationException;
import ch.bfh.amasoon.model.customer.CreditCard;
import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerAlreadyExistsException;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.customer.CustomerService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class CustomerBean implements Serializable {

    private static final String LOGIN_FAILED = "ch.bfh.amasoon.LOGIN_FAILED";
    private static final String NO_SUCH_CUSTOMER = "ch.bfh.amasoon.NO_SUCH_CUSTOMER";
    private static final String CUSTOMER_ALREADY_EXISTS = "ch.bfh.amasoon.CUSTOMER_ALREADY_EXISTS";
    private final CustomerService customerService = CustomerService.getInstance();
    @Inject
    private OrderBean orderBean;
    private Customer customer;
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

    public CreditCard.Type[] getCardTypes() {
        return CreditCard.Type.values();
    }

    public Customer getCustomer() {
        if (null == customer) {
            customer = new Customer();
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String addCustomer() {
        if (customer == null) {
            login(false);
        }
        try {
            customerService.addCustomer(customer);
            if (isUserLoggedIn() && !orderBean.isCartEmpty()) {
                return "orderSummary";
            } else {
                return "catalogSearch";
            }
        } catch (CustomerAlreadyExistsException ex) {
            MessageFactory.info(CUSTOMER_ALREADY_EXISTS);
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String updateCustomer() {
        if (customer == null) {
            login(false);
        }
        customerService.updateCustomer(customer);
        if (isUserLoggedIn() && !orderBean.isCartEmpty()) {
            return "orderSummary";
        } else {
            return "catalogSearch";
        }
    }

    public String login(boolean isCartEmpty) {//TODO SB remove param
        try {
            setCustomer(customerService.authenticateCustomer(email, password));
            if (isCartEmpty) {
                return "catalogSearch";
            } else {
                return "orderSummary";

            }
        } catch (AuthenticationException ex) {
            try {
                customerService.findCustomer(email);
                MessageFactory.info(LOGIN_FAILED);
                Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (CustomerNotFoundException ex1) {
                MessageFactory.info(NO_SUCH_CUSTOMER);
                Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex1);
                return null;
            }
        }
    }

    public synchronized String logout() {
        setCustomer(null);
        return "catalogSearch";
    }

    public boolean isUserLoggedIn() {
        return null != customer;
    }
}
