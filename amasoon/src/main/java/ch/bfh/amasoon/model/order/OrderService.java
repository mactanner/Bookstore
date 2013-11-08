package ch.bfh.amasoon.model.order;

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
import ch.bfh.amasoon.model.catalog.Book;
import ch.bfh.amasoon.model.catalog.BookNotFoundException;
import ch.bfh.amasoon.model.catalog.CatalogService;
import ch.bfh.amasoon.util.BookHandler;
import ch.bfh.amasoon.model.customer.CreditCard;
import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.customer.CustomerService;

public class OrderService {

    private static final long ORDER_PROCESS_TIME = 600000;
    private static final Logger logger = Logger.getLogger(CatalogService.class.getName());
    private static OrderService instance;
    private CustomerService customerService = CustomerService.getInstance();
    private CatalogService catalogService = CatalogService.getInstance();
    private Map<Integer, Order> orders = new TreeMap<>();
    private int lastOrderNumber = 1000;

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public synchronized Integer placeOrder(String email, Map<String, Integer> items)
            throws CustomerNotFoundException, CreditCardExpiredException, BookNotFoundException {
        logger.log(Level.INFO, "Placing order for customer with email {0}", email);
        Order order = new Order();
        Customer customer = customerService.findCustomer(email);
        order.setCustomer(customer);
        order.setAddress(BookHandler.clone(customer.getAddress()));
        CreditCard creditCard = BookHandler.clone(customer.getCreditCard());
        for (int i = 0; i < 12; i++) {
            creditCard.setNumber(creditCard.getNumber().replaceFirst("[0-9]", "*"));
        }
        order.setCreditCard(creditCard);

        Date expDate = order.getCreditCard().getExpirationDate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (df.format(expDate).compareTo(df.format(new Date())) < 0) {
            throw new CreditCardExpiredException();
        }

        BigDecimal amount = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Book book = catalogService.findBook(entry.getKey());
            BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
            amount = amount.add(book.getPrice().multiply(quantity));
            order.getItems().add(new LineItem(book, entry.getValue()));
        }

        order.setNumber(++lastOrderNumber);
        order.setDate(new Date());
        order.setAmount(amount);
        order.setStatus(Order.Status.open);

        orders.put(order.getNumber(), order);
        customer.getOrders().add(order);
        processOrder(order);
        return order.getNumber();
    }

    public synchronized Order findOrder(Integer number) throws OrderNotFoundException {
        logger.log(Level.INFO, "Finding order with number {0}", number);
        Order order = orders.get(number);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return BookHandler.clone(order);
    }

    public synchronized List<Order> searchOrders(Date dateFrom, Date dateTo) throws InvalidTimePeriodException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String beginDate = df.format(dateFrom);
        String endDate = df.format(dateTo);
        logger.log(Level.INFO, "Searching orders between {0} and {1}", new Object[]{beginDate, endDate});
        if (beginDate.compareTo(endDate) > 0) {
            throw new InvalidTimePeriodException();
        }
        List<Order> results = new ArrayList<>();
        for (Order order : orders.values()) {
            String orderDate = df.format(order.getDate());
            if (orderDate.compareTo(beginDate) >= 0 && orderDate.compareTo(endDate) <= 0) {
                results.add(BookHandler.clone(order));
            }
        }
        return results;
    }

    public synchronized void cancelOrder(Integer number) throws OrderNotFoundException, OrderNotCancelableException {
        logger.info("Canceling order with number " + number);
        Order order = orders.get(number);
        if (order.getStatus() != Order.Status.open) {
            throw new OrderNotCancelableException();
        }
        order.setStatus(Order.Status.canceled);
    }

    private void processOrder(final Order order) {
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
}
