/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.model.catalog.Book;
import ch.bfh.amasoon.model.catalog.CatalogService;
import ch.bfh.amasoon.util.MessageFactory;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class CatalogBean implements Serializable {

    private CatalogService catalogService = CatalogService.getInstance();
    private String keywords;
    private List<Book> books;
    private Book selectedBook;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<Book> getBooks() {
        return books;
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void searchBooks() {
        if (keywords.isEmpty()) {
            books.clear();
        } else {
            books = catalogService.searchBooks(keywords.split("\\s+"));
            if (books.isEmpty()) {
                MessageFactory.info("org.books.Bookstore.NO_BOOKS_FOUND");
            }
        }
    }

    public String selectBook(Book book) {
        selectedBook = book;
        return "bookDetails";
    }
}
