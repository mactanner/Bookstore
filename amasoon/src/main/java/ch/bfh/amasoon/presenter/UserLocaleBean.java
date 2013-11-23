package ch.bfh.amasoon.presenter;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Locale;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("userLocale")
@SessionScoped
public class UserLocaleBean implements Serializable {
    private Locale locale;

    public String getLocale() {
        if (null == locale) {
            locale = fromHttpRequest();
        }
        return locale.getLanguage().toUpperCase();
    }

    private Locale fromHttpRequest() {
        locale = Objects.firstNonNull(FacesContext.getCurrentInstance().getExternalContext().getRequestLocale(), FacesContext.getCurrentInstance().getViewRoot().getLocale());
        return locale;
    }

    public void switchToEnglish() {
        locale = Locale.ENGLISH;
    }

    public void switchToGerman() {
        locale = Locale.GERMAN;
    }

    public boolean localeIsEnglish() {
        return "EN".equals(getLocale());
    }

    public boolean localeIsGerman() {
        return "DE".equals(getLocale());
    }

}
