package ch.bfh.amasoon.components;

import ch.bfh.amasoon.commons.MessageFactory;
import com.google.common.base.Strings;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(PasswordValidator.VALIDATOR_ID)
public class PasswordValidator implements Validator {
    public static final String VALIDATOR_ID = "ch.bfh.amasoon.components.PasswordValidator";
    public static final String PASSWORD_NOT_EMPTY = "ch.bfh.amasoon.PASSWORD_NOT_EMPTY";
    public static final String PASSWORD_NO_BLANKS = "ch.bfh.amasoon.PASSWORD_NO_BLANKS";
    public static final String PASSWORD_6_CHAR = "ch.bfh.amasoon.PASSWORD_6_CHAR";
    public static final String PASSWORD_CONTAIN_DIGIST ="ch.bfh.amasoon.PASSWORD_CONTAIN_DIGIST";

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        String pwd = ((String) value);
        if (Strings.isNullOrEmpty(pwd)) {
            throw new ValidatorException(MessageFactory.getMessage(
                    FacesMessage.SEVERITY_INFO, PASSWORD_NOT_EMPTY));
        } else if (pwd.contains(" ")) {
            throw new ValidatorException(MessageFactory.getMessage(
                    FacesMessage.SEVERITY_INFO, PASSWORD_NO_BLANKS));
        } else if (pwd.length() < 6) {
            throw new ValidatorException(MessageFactory.getMessage(
                    FacesMessage.SEVERITY_INFO, PASSWORD_6_CHAR));
        } else if (!containsDigits(pwd)) {
            throw new ValidatorException(MessageFactory.getMessage(
                    FacesMessage.SEVERITY_INFO, PASSWORD_CONTAIN_DIGIST));
        }

    }

    private boolean containsDigits(String pwd) {
        boolean hasDigist = false;
        char[] chars = pwd.toCharArray();
        for (int i = 0; i < pwd.length(); i++) {
            if (Character.isDigit(chars[i])) {
                return true;
            }
        }
        return hasDigist;
    }

}
