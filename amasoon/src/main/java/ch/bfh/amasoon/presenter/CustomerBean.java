package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerAlreadyExistsException;
import ch.bfh.amasoon.model.customer.CustomerService;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class CustomerBean implements Serializable {

  //  private CustomerService customerService = CustomerService.getInstance(); TODO SB
    private Customer customer;

    public Customer getCustomer() {
        if (customer==null){
            customer = new Customer();
        }
                
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void addCustomer() {
//        try {
//            customerService.addCustomer(customer);
//        } catch (CustomerAlreadyExistsException ex) {
//            Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
