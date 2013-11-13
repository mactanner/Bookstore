package ch.bfh.amasoon.model.order;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.bfh.amasoon.model.catalog.CatalogService;
import ch.bfh.amasoon.model.customer.CreditCard;
import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.customer.CustomerService;

public class OrderService {

    private static final long ORDER_PROCESS_TIME = 600000;
    private static final Logger logger = Logger.getLogger(CatalogService.class.getName());
    private static OrderService instance;
    private CustomerService customerService = CustomerService.getInstance();
    private Map<String, Order> orders = new TreeMap<>();

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public synchronized String placeOrder(String email, List<LineItem> items) throws CustomerNotFoundException, CreditCardExpiredException {
        logger.log(Level.INFO, "Placing order for customer with email {0}", email);
        Customer customer = customerService.findCustomer(email);
        checkCreditCard(customer.getCreditCard());

        Order order = new Order();
        order.setNumber(String.valueOf(Math.random()).substring(2, 7));
        order.setDate(new Date());
        order.setStatus(Order.Status.open);

        order.setCustomer(customer);
        order.setAddress(clone(customer.getAddress()));
        CreditCard creditCard = clone(customer.getCreditCard());
        for (int i = 0; i < 12; i++) {
            creditCard.setNumber(creditCard.getNumber().replaceFirst("[0-9]", "*"));
        }
        order.setCreditCard(creditCard);

        order.setItems(clone(items));
        BigDecimal amount = BigDecimal.ZERO;
        for (LineItem item : items) {
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            amount = amount.add(item.getBook().getPrice().multiply(quantity));
        }
        order.setAmount(amount);

        orders.put(order.getNumber(), order);
        customer.getOrders().add(order);
        processOrder(order);
        return order.getNumber();
    }

    public synchronized Order findOrder(String number) throws OrderNotFoundException {
        logger.log(Level.INFO, "Finding order with number {0}", number);
        Order order = orders.get(number);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }

    public synchronized List<Order> searchOrders(Date dateFrom, Date dateTo) throws InvalidTimePeriodException {
        logger.log(Level.INFO, "Searching orders between {0} and {1}", new Object[]{dateFrom, dateTo});
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String beginDate = df.format(dateFrom);
        String endDate = df.format(dateTo);
        if (beginDate.compareTo(endDate) > 0) {
            throw new InvalidTimePeriodException();
        }
        List<Order> results = new ArrayList<>();
        for (Order order : orders.values()) {
            String orderDate = df.format(order.getDate());
            if (orderDate.compareTo(beginDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
                results.add(order);
            }
        }
        return results;
    }

    public synchronized void cancelOrder(String number) throws OrderNotFoundException, OrderNotCancelableException {
        logger.log(Level.INFO, "Canceling order with number {0}", number);
        Order order = findOrder(number);
        if (order.getStatus() != Order.Status.open) {
            throw new OrderNotCancelableException();
        }
        order.setStatus(Order.Status.canceled);
    }

    private void checkCreditCard(CreditCard creditCard) throws CreditCardExpiredException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (df.format(creditCard.getExpirationDate()).compareTo(df.format(new Date())) < 0) {
            throw new CreditCardExpiredException();
        }
    }

    private void processOrder(final Order order) {
        logger.log(Level.INFO, "Processing order with number {0}", order.getNumber());
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (order.getStatus() == Order.Status.open) {
                    order.setStatus(Order.Status.closed);
                }
            }
        };
        new Timer().schedule(task, ORDER_PROCESS_TIME);
    }

    @SuppressWarnings("unchecked")
    private <T> T clone(T object) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            new ObjectOutputStream(os).writeObject(object);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            return (T) new ObjectInputStream(is).readObject();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
