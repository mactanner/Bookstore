package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.customer.CreditCard;
import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerAlreadyExistsException;
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

    @Inject
    private UserAuthentificationBean userAuthentificationBean;
    private CustomerService customerService = CustomerService.getInstance();
    private Customer customer;

    public CreditCard.Type[] getCardTypes() {
        return CreditCard.Type.values();
    }

    public Customer getCustomer() {
        if (userAuthentificationBean.isUserLoggedIn()) {
            customer = userAuthentificationBean.getCustomer();
        } else if (customer == null || customer != null && customer.getEmail() != null) {
            // either new session user or user just logged out
            customer = new Customer();
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * TODO SB conditionale navigation auf customerDetailPage
     *
     * @return
     */
    public String addCustomer() {
        try {
            customerService.addCustomer(customer);
            return "orderSummary";
        } catch (CustomerAlreadyExistsException ex) {
            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
            //TODO message
            return null;
        }
    }
}
