/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.catalog.Book;
import ch.bfh.amasoon.model.catalog.BookNotFoundException;
import ch.bfh.amasoon.model.customer.CustomerNotFoundException;
import ch.bfh.amasoon.model.order.CreditCardExpiredException;
import ch.bfh.amasoon.model.order.OrderService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class OrderBean implements Serializable {

    private OrderService orderService = OrderService.getInstance();
    private String email = "tester@amasoon.com";

    /**
     * add Book to cart if not yet logged in
     * @param book 
     */
    public void placeOrder(Book book){
        Map<String, Integer> item = new HashMap<>();
        item.put(book.getIsbn(), 1);
        try {
            orderService.placeOrder(email, item);
        } catch (CustomerNotFoundException ex) {
            Logger.getLogger(OrderBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CreditCardExpiredException ex) {
            Logger.getLogger(OrderBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BookNotFoundException ex) {
            Logger.getLogger(OrderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
