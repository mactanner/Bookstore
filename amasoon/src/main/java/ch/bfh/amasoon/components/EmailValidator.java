package ch.bfh.amasoon.components;

import ch.bfh.amasoon.commons.MessageFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(EmailValidator.VALIDATOR_ID)
public class EmailValidator implements Validator {
    public static final String VALIDATOR_ID = "ch.bfh.amasoon.components.EmailValidator";

    public static final String EMAIL_NOT_VALID = "ch.bfh.amasoon.EMAIL_NOT_VALID";
    private static final Pattern PATTERN = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+(?:[A-Z]{2}|com|org|net|edu|gov|biz|info|jobs|museum)\\b");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        String email = ((String) value);
        Matcher matcher = PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new ValidatorException(MessageFactory.getMessage(
                    FacesMessage.SEVERITY_INFO, EMAIL_NOT_VALID));
        }

    }

}
