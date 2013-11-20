package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.customer.Customer;
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
public class CustomerAdminBean implements Serializable {

    @Inject
    private CustomerBean customerBean;
    private CustomerService customerService = CustomerService.getInstance();
    private String emailToSearch;

    public String getEmailToSearch() {
        return emailToSearch;
    }

    public void setEmailToSearch(String emailToSearch) {
        this.emailToSearch = emailToSearch;
    }

    public void findCustomer() {
        try {
            customerBean.setCustomer(customerService.findCustomer(emailToSearch));
        } catch (CustomerNotFoundException ex) {
            // TODO: appropriate message handling
            customerBean.setCustomer(null);
            Logger.getLogger(CustomerAdminBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
