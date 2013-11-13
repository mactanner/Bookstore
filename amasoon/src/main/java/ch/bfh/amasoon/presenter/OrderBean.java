package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.catalog.Book;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

@Named
@SessionScoped
public class OrderBean implements Serializable {

    private ConcurrentHashMap<Book, Integer> books = new ConcurrentHashMap<>();

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

    public int getTotalBooksAdded() {
        int count = 0;
        for (Integer amount : books.values()) {
            count += amount;
        }
        return count;
    }
}
