package ch.bfh.amasoon.model.order;

import java.io.Serializable;
import ch.bfh.amasoon.model.catalog.Book;

public class LineItem implements Serializable {

    private Book book;
    private Integer quantity;

    public LineItem() {
    }

    public LineItem(Book book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
