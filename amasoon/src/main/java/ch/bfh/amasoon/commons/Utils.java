package ch.bfh.amasoon.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.bfh.amasoon.model.catalog.CatalogService;

public class Utils {

    private static final Logger logger = Logger.getLogger(CatalogService.class.getName());

    @SuppressWarnings("unchecked")
    public static <T> T clone(T object) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            new ObjectOutputStream(os).writeObject(object);
            ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
            return (T) new ObjectInputStream(is).readObject();
        } catch (IOException | ClassNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
