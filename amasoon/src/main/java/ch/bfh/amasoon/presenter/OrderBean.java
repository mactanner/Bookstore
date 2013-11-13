package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.catalog.Book;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class OrderBean implements Serializable {

    private ConcurrentHashMap<Book, Integer> books = new ConcurrentHashMap<Book, Integer>();

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

    public int getQuantity(Book book) {
        return books.get(book);
    }

    public int getTotalBooksAdded() {
        int count = 0;
        for (Integer amount : books.values()) {
            count += amount;
        }
        return count;
    }
}
