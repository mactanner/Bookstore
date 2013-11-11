package ch.bfh.amasoon.commons;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * The class MessageFactory is used to create faces messages.
 */
public class MessageFactory {

	public static FacesMessage getMessage(
			FacesMessage.Severity severity, String key, Object... params) {
		String summary = "???" + key + "???";
		String detail = null;
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			String name = context.getApplication().getMessageBundle();
			if (name == null) {
				name = FacesMessage.FACES_MESSAGES;
			}
			Locale locale = context.getViewRoot().getLocale();
			ResourceBundle bundle = ResourceBundle.getBundle(name, locale);
			summary = MessageFormat.format(bundle.getString(key), params);
			detail = MessageFormat.format(bundle.getString(key + "_detail"),
					params);
		} catch (MissingResourceException e) {
		}
		return new FacesMessage(severity, summary, detail);
	}

	public static void info(String key, Object... params) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, getMessage(FacesMessage.SEVERITY_INFO, key, params));
	}

	public static void error(String key, Object... params) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, getMessage(FacesMessage.SEVERITY_ERROR, key, params));
	}
}
