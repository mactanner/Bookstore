package ch.bfh.amasoon.components;

import ch.bfh.amasoon.commons.MessageFactory;
import java.io.IOException;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

/**
 * The class NumberSelectorRenderer implements a renderer for a number selector.
 */
@FacesRenderer(componentFamily = UIInput.COMPONENT_FAMILY, rendererType = NumberSelectorRenderer.RENDERER_TYPE)
public class NumberSelectorRenderer extends Renderer {

    public static final String RENDERER_TYPE = "ch.bfh.amasoon.components.NumberSelectorRenderer";
    public static final String MISSING_VALUE_MESSAGE_ID = "ch.bfh.amasoon.NumberSelectorRenderer.MISSING_VALUE";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        NumberSelector selector = (NumberSelector) component;
        Object value = selector.getSubmittedValue();
        if (value == null) {
            value = selector.getValue();
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("select", selector);
        writer.writeAttribute("name", selector.getClientId(), null);
        writer.writeAttribute("onchange", "submit()", null);
        writer.writeAttribute("class", "input-mini", null);
        writer.startElement("option", selector);
        writer.writeAttribute("value", "", null);
        writer.writeText("", null);
        writer.endElement("option");
        for (Integer val = selector.getMin(); val <= selector.getMax(); val++) {
            writer.startElement("option", selector);
            writer.writeAttribute("value", val, null);
            if (val.equals(value)) {
                writer.writeAttribute("selected", "true", null);
            }
            writer.writeText(val, null);
            writer.endElement("option");
        }
        writer.endElement("select");
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String value = params.get(component.getClientId());
        ((UIInput) component).setSubmittedValue(value);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) {
        try {
            return new Integer((String) submittedValue);
        } catch (NumberFormatException e) {
            throw new ConverterException(
                    MessageFactory.getMessage(FacesMessage.SEVERITY_ERROR, MISSING_VALUE_MESSAGE_ID));
        }
    }
}
