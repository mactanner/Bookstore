package ch.bfh.amasoon.components;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(CreditCardConverter.CONVERTER_ID)
public class CreditCardConverter implements Converter {

    public static final String CONVERTER_ID = "ch.bfh.amasoon.components.CreditCardConverter";

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String creditCardNumber = value.toString();
        for (int i = 0; i < 12; i++) {
            creditCardNumber = creditCardNumber.replaceFirst("[0-9]", "*");
        }
        return creditCardNumber;
    }

}
