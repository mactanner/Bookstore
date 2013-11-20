package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.customer.CustomerService;
import ch.bfh.amasoon.model.order.Order;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class CustomerAdminBean implements Serializable {

    private CustomerService customerService = CustomerService.getInstance();
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
            customer = customerService.findCustomer(emailToSearch);
        } catch (CustomerNotFoundException ex) {
            customer = null;
            // TODO: appropriate message handling
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