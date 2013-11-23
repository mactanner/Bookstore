package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.commons.MessageFactory;
import ch.bfh.amasoon.model.catalog.Book;
import ch.bfh.amasoon.model.customer.Customer;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.order.CreditCardExpiredException;
import ch.bfh.amasoon.model.order.LineItem;
import ch.bfh.amasoon.model.order.OrderNotCancelableException;
import ch.bfh.amasoon.model.order.OrderNotFoundException;
import ch.bfh.amasoon.model.order.OrderService;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
public class OrderBean implements Serializable {
    private static final String PLACE_ORDER_FAILED = "ch.bfh.amasoon.presenter.OrderBean.PLACE_ORDER_FAILED";
    private static final String NO_BOOK_FOUND = "ch.bfh.amasoon.NO_BOOK_FOUND";
    private static final String ORDER_NOT_CANCELABLE = "ch.bfh.amasoon.ORDER_NOT_CANCELABLE";
    private final OrderService orderService = OrderService.getInstance();
    @Inject
    private CustomerBean customerBean;
    private List<LineItem> lineItems = new ArrayList<>();
    private String orderNumber = "";

    public void addToCart(Book book) {
        boolean isAdditionalBook = true;
        for (LineItem lineItem : lineItems) {
            if (book.getIsbn().equals(lineItem.getBook().getIsbn())) {
                lineItem.setQuantity(lineItem.getQuantity() + 1);
                isAdditionalBook = false;
            }
        }
        if (isAdditionalBook) {
            lineItems.add(new LineItem(book, 1));
        }
    }

    public void removeFromCart(LineItem lineItemToRemove) {
        for (LineItem lineItem : lineItems) {
            if (lineItem.equals(lineItemToRemove)) {
                lineItems.remove(lineItem);
                break;
            }
        }
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public BigDecimal getLineItemPrice(LineItem lineItem) {
        return lineItem.getBook().getPrice().multiply(new BigDecimal(lineItem.getQuantity()));
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0.0);
        for (LineItem lineItem : lineItems) {
            totalPrice = totalPrice.add(getLineItemPrice(lineItem));
        }
        return totalPrice;
    }

    public int getTotalBooksAdded() {
        int numberOfBooks = 0;
        for (LineItem lineItem : lineItems) {
            numberOfBooks += lineItem.getQuantity();
        }
        return numberOfBooks;
    }

    public boolean isCartEmpty() {
        return lineItems.isEmpty();
    }

    public String placeOrder() {
        try {
            String email = customerBean.getEmail();
            if (Strings.isNullOrEmpty(email)) {
                Customer customer = customerBean.getCustomer();
                email = customer.getEmail();
            }
            orderNumber = orderService.placeOrder(email, lineItems);
            lineItems.clear();
            return "orderConfirmation";
        } catch (CustomerNotFoundException | CreditCardExpiredException ex) {
            MessageFactory.error(PLACE_ORDER_FAILED);
        }
        return null;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void cancelOrder(String orderNumber) {
        try {
            orderService.cancelOrder(orderNumber);
        } catch (OrderNotFoundException ex) {
            MessageFactory.error(NO_BOOK_FOUND, orderNumber);
        } catch (OrderNotCancelableException ex) {
            MessageFactory.error(ORDER_NOT_CANCELABLE, orderNumber);
        }
    }

}
