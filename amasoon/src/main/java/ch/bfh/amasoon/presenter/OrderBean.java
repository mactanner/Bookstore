package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.catalog.Book;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class OrderBean implements Serializable {

    private List<Book> books = new ArrayList<>();

    public void addToCart(Book book) {
        books.add(book);
    }

    public int getTotalBooksAdded() {
        return books.size();
    }
}
