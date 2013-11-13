package ch.bfh.amasoon.presenter;

import ch.bfh.amasoon.commons.MessageFactory;
import ch.bfh.amasoon.model.customer.CreditCard;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(CreditCardValidator.VALIDATOR_ID)
public class CreditCardValidator implements Validator, StateHolder {

	public static final String VALIDATOR_ID = "ch.amasoon.CreditCardValidator";
	public static final String INVALID_FORMAT_MESSAGE_ID = "ch.amasoon.CreditCardValidator.INVALID_FORMAT";
	public static final String INVALID_CHECK_DIGIT_MESSAGE_ID = "ch.amasoon.CreditCardValidator.INVALID_CHECK_DIGIT";
	public static final String INVALID_ISSUER_ID_MESSAGE_ID = "ch.amasoon.CreditCardValidator.INVALID_ISSUER_ID";
	private static final Pattern PATTERN = Pattern.compile("([0-9]{4} ?){4}");
	private static final String EXAMPLE = "1111 2222 3333 4444";

	private String cardTypeId;
	private boolean transientValue;

	public String getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) {
		String number = ((String) value).trim();
		checkFormat(number);
		number = number.replaceAll(" ", "");
		checkLuhnDigit(number);
		CreditCard.Type type = getCardType(component);
		if (type != null) {
			checkIssuerId(number, type);
		}
	}

	private void checkFormat(String number) {
		Matcher matcher = PATTERN.matcher(number);
		if (!matcher.matches()) {
			throw new ValidatorException(MessageFactory.getMessage(
					FacesMessage.SEVERITY_ERROR, INVALID_FORMAT_MESSAGE_ID, number, EXAMPLE));
		}
	}

	private void checkLuhnDigit(String number) {
		int sum = 0;
		for (int i = 0; i < number.length(); i++) {
			int d = Character.digit(number.charAt(i), 10);
			if (i % 2 == number.length() % 2) {
				d += d < 5 ? d : (d - 9);
			}
			sum += d;
		}
		if (sum % 10 != 0) {
			throw new ValidatorException(MessageFactory.getMessage(
					FacesMessage.SEVERITY_ERROR, INVALID_CHECK_DIGIT_MESSAGE_ID, number));
		}
	}

	private CreditCard.Type getCardType(UIComponent component) {
		CreditCard.Type type = null;
		UIInput cardTypeInput = (UIInput) component.findComponent(cardTypeId);
		if (cardTypeInput != null) {
			if (cardTypeInput.getSubmittedValue() != null) {
				try {
					type = CreditCard.Type.valueOf((String) cardTypeInput.getSubmittedValue());
				} catch (IllegalArgumentException ex) {
				}
			} else {
				type = (CreditCard.Type) cardTypeInput.getValue();
			}
		}
		return type;
	}

	private void checkIssuerId(String number, CreditCard.Type type) {
		boolean valid = true;
		switch (type) {
			case MasterCard:
				valid = number.startsWith("51") || number.startsWith("52")
						|| number.startsWith("53") || number.startsWith("54")
						|| number.startsWith("55");
				break;
			case Visa:
				valid = number.startsWith("4");
				break;
		}
		if (!valid) {
			throw new ValidatorException(MessageFactory.getMessage(
					FacesMessage.SEVERITY_ERROR, INVALID_ISSUER_ID_MESSAGE_ID, number));
		}
	}

	@Override
	public boolean isTransient() {
		return transientValue;
	}

	@Override
	public void setTransient(boolean transientValue) {
		this.transientValue = transientValue;
	}

	@Override
	public Object saveState(FacesContext context) {
		return cardTypeId;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		cardTypeId = (String) state;
	}
}
