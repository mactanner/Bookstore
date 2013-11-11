package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.catalog.Book;
import ch.bfh.amasoon.model.order.OrderService;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class OrderBean implements Serializable {

    private OrderService orderService = OrderService.getInstance();


    public void addToCart(Book book) {
        orderService.addBook(book);
    }
    
    public int getTotalBooksAdded(){
        return orderService.getTotalBooksAdded();
    }
}
