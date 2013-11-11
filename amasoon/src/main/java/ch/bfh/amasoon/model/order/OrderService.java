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
import ch.bfh.amasoon.model.catalog.Book;
import ch.bfh.amasoon.model.catalog.CatalogService;
import ch.bfh.amasoon.model.customer.CreditCard;
import ch.bfh.amasoon.model.customer.Customer;

public class OrderService {

    private static final long ORDER_PROCESS_TIME = 600000;
    private static final Logger logger = Logger.getLogger(CatalogService.class.getName());
    private static OrderService instance;
    private Map<String, Order> orders = new TreeMap<>();
    private List<Book> books = new ArrayList<>();

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public synchronized void addBook(Book book) {
        books.add(book);
    }

    public int getTotalBooksAdded() {
        return books.size();
    }

    public synchronized String placeOrder(Customer customer, List<LineItem> items) throws CreditCardExpiredException {
        logger.log(Level.INFO, "Placing order for customer with email {0}", customer.getEmail());
        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(clone(customer.getAddress()));
        CreditCard creditCard = clone(customer.getCreditCard());
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
        for (LineItem item : items) {
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            amount = amount.add(item.getBook().getPrice().multiply(quantity));
        }

        order.setNumber(String.valueOf(Math.random()).substring(2, 7));
        order.setDate(new Date());
        order.setAmount(amount);
        order.setStatus(Order.Status.open);

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

    public synchronized void cancelOrder(Integer number) throws OrderNotFoundException, OrderNotCancelableException {
        logger.log(Level.INFO, "Canceling order with number {0}", number);
        Order order = orders.get(number);
        if (order.getStatus() != Order.Status.open) {
            throw new OrderNotCancelableException();
        }
        order.setStatus(Order.Status.canceled);
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
