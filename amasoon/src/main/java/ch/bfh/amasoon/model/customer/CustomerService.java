package ch.bfh.amasoon.model.customer;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class CustomerService {

    private static final String CUSTOMER_DATA = "/data/customers.xml";
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
        try {
            JAXBContext context = JAXBContext.newInstance(CustomerData.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream stream = getClass().getResourceAsStream(CUSTOMER_DATA);
            CustomerData customerData = (CustomerData) unmarshaller.unmarshal(stream);
            for (Customer customer : customerData.getCustomers()) {
                try {
                    addCustomer(customer);
                } catch (CustomerAlreadyExistsException ex) {
                }
            }
        } catch (JAXBException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void addCustomer(Customer customer) throws CustomerAlreadyExistsException {
        logger.log(Level.INFO, "Adding customer with email {0}", customer.getEmail());
        if (customers.containsKey(customer.getEmail())) {
            throw new CustomerAlreadyExistsException();
        }
        customer.setPassword(getDigest(customer.getPassword()));
        customers.put(customer.getEmail(), customer);
    }

    public synchronized Customer findCustomer(String email) throws CustomerNotFoundException {
        logger.log(Level.INFO, "Finding customer with email {0}", email);
        Customer customer = customers.get(email);
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        return customer;
    }

    public synchronized Customer authenticateCustomer(String email, String password) throws AuthenticationException {
        logger.log(Level.INFO, "Authenticating customer with email {0}", email);
        Customer customer = customers.get(email);
        if (customer == null || !customer.getPassword().equals(getDigest(password))) {
            throw new AuthenticationException();
        }
        return customer;
    }

    public synchronized List<Customer> searchCustomers(String name) {
        logger.log(Level.INFO, "Searching customers by name {0}", name);
        name = name.toLowerCase();
        List<Customer> results = new ArrayList<>();
        for (Customer customer : customers.values()) {
            if (!customer.getName().toLowerCase().contains(name)) {
                results.add(customer);
            }
        }
        return results;
    }

    public synchronized void updateCustomer(Customer customer) {
        logger.log(Level.INFO, "Updating customer with email {0}", customer.getEmail());
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
