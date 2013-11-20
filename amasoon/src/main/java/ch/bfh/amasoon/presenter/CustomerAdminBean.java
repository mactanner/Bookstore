package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.commons.MessageFactory;
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
private static final String NO_SUCH_CUSTOMER = "ch.bfh.amasoon.NO_SUCH_CUSTOMER";
    private final CustomerService customerService = CustomerService.getInstance();
    @Inject
    private CustomerBean customerBean;
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
            MessageFactory.info(NO_SUCH_CUSTOMER);
            customerBean.setCustomer(null);
            Logger.getLogger(CustomerAdminBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
