package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.commons.MessageFactory;
import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.customer.CustomerService;
import ch.bfh.amasoon.model.order.Order;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class CustomerAdminBean implements Serializable {

    private static final String NO_SUCH_CUSTOMER = "ch.bfh.amasoon.NO_SUCH_CUSTOMER";
    private final CustomerService customerService = CustomerService.getInstance();
    private String emailToSearch;
    private Customer customer;
    private Order selectedOrder;

    public String getEmailToSearch() {
        return emailToSearch;
    }

    public void setEmailToSearch(String emailToSearch) {
        this.emailToSearch = emailToSearch;
    }

    public void findCustomer() {
        try {
            if (Strings.isNullOrEmpty(emailToSearch)) {
                customer = null;
            } else {
                customer = customerService.findCustomer(emailToSearch);

            }
        } catch (CustomerNotFoundException ex) {
            customer = null;
            MessageFactory.info(NO_SUCH_CUSTOMER);
            Logger.getLogger(CustomerAdminBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    public String displayOrder(Order order) {
        selectedOrder = order;
        return "/admin/orderDetails";
    }

}
