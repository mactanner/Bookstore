package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.commons.MessageFactory;
import ch.bfh.amasoon.model.customer.AuthenticationException;
import ch.bfh.amasoon.model.customer.CreditCard;
import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerAlreadyExistsException;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.customer.CustomerService;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
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
        Preconditions.checkNotNull(customer);
        try {
            customerService.addCustomer(customer);
            return navigate();
        } catch (CustomerAlreadyExistsException ex) {
            MessageFactory.info(CUSTOMER_ALREADY_EXISTS);
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String updateCustomer() {
        Preconditions.checkNotNull(customer);
        customerService.updateCustomer(customer);
        return navigate();
    }

    public String navigate() {
        return isUserLoggedIn() && !isCartEmpty() ? "orderSummary" : "catalogSearch";
    }

    public String login() {
        try {
            setCustomer(customerService.authenticateCustomer(email, password));
            return isCartEmpty() ? "search" : "order";
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
        return "search";
    }

    public boolean isUserLoggedIn() {
        return null != customer && !Strings.isNullOrEmpty(customer.getEmail());
    }

    private boolean isCartEmpty() {
        return orderBean.isCartEmpty();
    }
}
