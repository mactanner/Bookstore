package ch.bfh.amasoon.model.catalog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import ch.bfh.amasoon.commons.Utils;

public class CatalogService {

    private static final String CATALOG = "/data/catalog.xml";
    private static final Logger logger = Logger.getLogger(CatalogService.class.getName());
    private static CatalogService instance;
    private Map<String, Book> books = new TreeMap<>();

    public static CatalogService getInstance() {
        if (instance == null) {
            instance = new CatalogService();
        }
        return instance;
    }

    private CatalogService() {
        try {
            JAXBContext context = JAXBContext.newInstance(Catalog.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream stream = getClass().getResourceAsStream(CATALOG);
            Catalog catalog = (Catalog) unmarshaller.unmarshal(stream);
            for (Book book : catalog.getBooks()) {
                books.put(book.getIsbn(), book);
            }
        } catch (JAXBException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void addBook(Book book) throws BookAlreadyExistsException {
        logger.log(Level.INFO, "Adding book with isbn {0}", book.getIsbn());
        if (books.containsKey(book.getIsbn())) {
            throw new BookAlreadyExistsException();
        }
        books.put(book.getIsbn(), Utils.clone(book));
    }

    public synchronized Book findBook(String isbn) throws BookNotFoundException {
        logger.log(Level.INFO, "Finding book with isbn {0}", isbn);
        Book book = books.get(isbn);
        if (book == null) {
            throw new BookNotFoundException();
        }
        return Utils.clone(book);
    }

    public synchronized List<Book> searchBooks(String... keywords) {
        logger.log(Level.INFO, "Searching books by keywords");
        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = keywords[i].toLowerCase();
        }
        List<Book> results = new ArrayList<>();
        loop:
        for (Book book : books.values()) {
            for (String keyword : keywords) {
                if (!book.getTitle().toLowerCase().contains(keyword)
                        && !book.getAuthors().toLowerCase().contains(keyword)
                        && !book.getPublisher().toLowerCase().contains(keyword)) {
                    continue loop;
                }
            }
            results.add(Utils.clone(book));
        }
        return results;
    }

    public synchronized void updateBook(Book book) {
        logger.log(Level.INFO, "Updating book with isbn {0}", book.getIsbn());
        books.put(book.getIsbn(), Utils.clone(book));
    }

    public synchronized void removeBook(String isbn) {
        logger.log(Level.INFO, "Removing book with isbn {0}", isbn);
        books.remove(isbn);
    }
}
