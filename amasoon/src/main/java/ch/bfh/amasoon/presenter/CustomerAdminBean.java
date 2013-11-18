package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.customer.CustomerService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class CustomerAdminBean implements Serializable {

    private CustomerService customerService = CustomerService.getInstance();
    private String emailToSearch;
    private Customer customer;

    public String getEmailToSearch() {
        return emailToSearch;
    }

    public void setEmailToSearch(String emailToSearch) {
        this.emailToSearch = emailToSearch;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void findCustomer() {
        try {
            customer = customerService.findCustomer(emailToSearch);
        } catch (CustomerNotFoundException ex) {
            // TODO: appropriate message handling
            customer = null;
            Logger.getLogger(CustomerAdminBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
