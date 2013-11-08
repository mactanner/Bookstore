package ch.bfh.amasoon.model.customer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import ch.bfh.amasoon.util.BookHandler;

public class CustomerService {

    private static final Logger logger = Logger.getLogger(CustomerService.class.getName());
    private static CustomerService instance;
    private Map<String, Customer> customers = new TreeMap<>();
    private MessageDigest messageDigest;

    public static CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    private CustomerService() {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void addCustomer(Customer customer) throws CustomerAlreadyExistsException {
        logger.log(Level.INFO, "Adding customer with email {0}", customer.getEmail());
        if (customers.containsKey(customer.getEmail())) {
            throw new CustomerAlreadyExistsException();
        }
        customer = BookHandler.clone(customer);
        customer.setPassword(getDigest(customer.getPassword()));
        customers.put(customer.getEmail(), customer);
    }

    public synchronized Customer findCustomer(String email) throws CustomerNotFoundException {
        logger.log(Level.INFO, "Finding customer with email {0}", email);
        Customer customer = customers.get(email);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        return BookHandler.clone(customer);
    }

    public synchronized List<Customer> searchCustomers(String name) {
        logger.log(Level.INFO, "Searching customers by name {0}", name);
        name = name.toLowerCase();
        List<Customer> results = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (!customer.getName().toLowerCase().contains(name)) {
                results.add(BookHandler.clone(customer));
            }
        }
        return results;
    }

    public synchronized Customer authenticateCustomer(String email, String password) throws CustomerNotFoundException, AuthenticationException {
        logger.log(Level.INFO, "Finding customer with email {0}", email);
        Customer customer = customers.get(email);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        if (!customer.getPassword().equals(getDigest(password))) {
            throw new AuthenticationException();
        }
        return BookHandler.clone(customer);
    }

    public synchronized void updateCustomer(Customer customer) {
        logger.log(Level.INFO, "Updating customer with email {0}", customer.getEmail());
        customer = BookHandler.clone(customer);
        customer.setPassword(getDigest(customer.getPassword()));
        customers.put(customer.getEmail(), customer);
    }

    public synchronized void removeCustomer(String email) {
        logger.log(Level.INFO, "Removing customer with email {0}", email);
        customers.remove(email);
    }

    private String getDigest(String password) {
        return DatatypeConverter.printHexBinary(messageDigest.digest(password.getBytes()));
    }
}
