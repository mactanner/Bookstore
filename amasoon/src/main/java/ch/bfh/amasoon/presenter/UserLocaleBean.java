package ch.bfh.amasoon.presenter;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author beeet
 */
@Named("userLocale")
@SessionScoped
public class UserLocaleBean implements Serializable {
    private static final FacesContext context = FacesContext.getCurrentInstance();
    private static final ExternalContext extContext = context.getExternalContext();
    private Locale locale;

    public String getLocale() {
        return Objects.firstNonNull(locale, fromHttpRequest()).getLanguage().toUpperCase();
    }

    private Locale fromHttpRequest() {
        return Objects.firstNonNull(extContext.getRequestLocale(), Locale.ENGLISH);
    }

    public void switchToEnglish() {
        locale = Locale.ENGLISH;
    }

    public void switchToGerman() {
        locale = Locale.GERMAN;
    }

}
