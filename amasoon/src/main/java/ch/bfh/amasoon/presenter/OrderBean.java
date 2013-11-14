package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.commons.MessageFactory;
import ch.bfh.amasoon.model.catalog.Book;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.order.CreditCardExpiredException;
import ch.bfh.amasoon.model.order.LineItem;
import ch.bfh.amasoon.model.order.OrderService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class OrderBean implements Serializable {

    @Inject
    private UserAuthentificationBean userAuthentificationBean;
    private ConcurrentHashMap<Book, Integer> books = new ConcurrentHashMap<>();
    private OrderService orderService = OrderService.getInstance();
    private String orderNumber = "";

    private static final String PLACE_ORDER_FAILED = "ch.bfh.amasoon.presenter.OrderBean.PLACE_ORDER_FAILED";

    public void addToCart(Book book) {
        Integer count = books.get(book);
        if (count == null) {
            books.put(book, 1);
        } else {
            books.put(book, count + 1);
        }
    }

    public void removeFromCart(Book book) {
        books.remove(book);
    }

    public List<Book> getBooks() {
        return Collections.list(books.keys());
    }

    public Integer getQuantity(Book book) {
        if (books.containsKey(book)) {
            return books.get(book);
        } else {
            return 0;
        }
    }

    public void setQuantity(Book book) {

    }

    public void valueChanged(ValueChangeEvent e) {
        System.out.println();
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0.0);
        Iterator<Book> booksIterator = books.keySet().iterator();
        while (booksIterator.hasNext()) {
            Book book = booksIterator.next();
            totalPrice = totalPrice.add(getCartItemPrice(book));
        }
        return totalPrice;
    }

    public BigDecimal getCartItemPrice(Book book) {
        BigDecimal totalPrice = book.getPrice();
        return totalPrice.multiply(new BigDecimal(books.get(book)));
    }

    public int getTotalBooksAdded() {
        int count = 0;
        for (Integer amount : books.values()) {
            count += amount;
        }
        return count;
    }

    public String placeOrder() {
        try {
            orderNumber = orderService.placeOrder(userAuthentificationBean.getEmail(), createLineItems());
            books.clear();
            return "orderConfirmation";
        } catch (CustomerNotFoundException | CreditCardExpiredException ex) {
            MessageFactory.error(PLACE_ORDER_FAILED);
        }
        return null;
    }

    private List<LineItem> createLineItems() {
        List<LineItem> lineItems = new ArrayList();
        Iterator<Book> booksIterator = books.keySet().iterator();
        while (booksIterator.hasNext()) {
            Book book = booksIterator.next();
            lineItems.add(new LineItem(book, books.get(book)));
        }
        return lineItems;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

}
