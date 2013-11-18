package ch.bfh.amasoon.components;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

@FacesRenderer(componentFamily = UIInput.COMPONENT_FAMILY, rendererType = ExpirationDateRenderer.RENDERER_TYPE)
public class ExpirationDateRenderer extends Renderer {

	public static final String RENDERER_TYPE = "ch.bfh.amasoon.components.ExpirationDateRenderer";

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ExpirationDate dateInput = (ExpirationDate) component;
		ResponseWriter writer = context.getResponseWriter();

		// get selected date
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		int currentYear = calendar.get(Calendar.YEAR);
		int selectedYear = calendar.get(Calendar.YEAR);
		int selectedMonth = calendar.get(Calendar.MONTH) + 1;
		if (dateInput.getSubmittedValue() != null) {
			try {
				String[] tokens = ((String) dateInput.getSubmittedValue()).split("-");
				selectedYear = Integer.parseInt(tokens[0]);
				selectedMonth = Integer.parseInt(tokens[1]);
			} catch (NumberFormatException ex) {
			}
		} else if (dateInput.getValue() != null) {
			calendar.setTime((Date) dateInput.getValue());
			selectedYear = calendar.get(Calendar.YEAR);
			selectedMonth = calendar.get(Calendar.MONTH) + 1;
		}

		// render month selection
		writer.startElement("select", dateInput);
		writer.writeAttribute("name", dateInput.getClientId() + ":month", null);
                writer.writeAttribute("class", "input-mini", null);
		for (int month = 1; month <= 12; month++) {
			writer.startElement("option", dateInput);
			writer.writeAttribute("value", month, null);
			if (month == selectedMonth) {
				writer.writeAttribute("selected", "true", null);
			}
			writer.writeText(String.format("%02d", month), null);
			writer.endElement("option");
		}
		writer.endElement("select");

		// render year selection
		writer.startElement("select", dateInput);
		writer.writeAttribute("name", dateInput.getClientId() + ":year", null);
                writer.writeAttribute("class", "input-small", null);
		for (int year = currentYear; year < currentYear + dateInput.getYears(); year++) {
			writer.startElement("option", dateInput);
			writer.writeAttribute("value", year, null);
			if (year == selectedYear) {
				writer.writeAttribute("selected", "true", null);
			}
			writer.writeText(String.format("%04d", year), null);
			writer.endElement("option");
		}
		writer.endElement("select");
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		ExpirationDate dateInput = (ExpirationDate) component;
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String year = params.get(dateInput.getClientId() + ":year");
		String month = params.get(dateInput.getClientId() + ":month");
		dateInput.setSubmittedValue(year + "-" + month);
	}

	@Override
	public Object getConvertedValue(FacesContext context, UIComponent dateInput, Object submittedValue) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return df.parse((String) submittedValue);
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
	}
}
