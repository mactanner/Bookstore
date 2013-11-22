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

@FacesValidator(ZipCodeValidator.VALIDATOR_ID)
public class ZipCodeValidator implements Validator {
    public static final String VALIDATOR_ID = "ch.bfh.amasoon.components.ZipCodeValidator";

    public static final String NOT_MATCHED = "javax.faces.validator.RegexValidator.NOT_MATCHED";
    private static final Pattern PATTERN = Pattern.compile("^[1-9][0-9]{3}$|^[1-9][0-9]{4}$");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        String zip = ((String) value);
        Matcher matcher = PATTERN.matcher(zip);
        if (!matcher.matches()) {
            throw new ValidatorException(MessageFactory.getMessage(
                    FacesMessage.SEVERITY_INFO, NOT_MATCHED));
        }

    }

}
