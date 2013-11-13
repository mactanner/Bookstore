package ch.bfh.amasoon.presenter;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

@FacesComponent(ExpirationDateInput.COMPONENT_TYPE)
public class ExpirationDateInput extends UIInput {

	public static final String COMPONENT_TYPE = "ch.amasoon.ExpirationDateInput";

	public enum PropertyKeys {

		years
	}

	public Integer getYears() {
		Integer years = (Integer) getStateHelper().eval(PropertyKeys.years);
		if (years == null) {
			years = 1;
		}
		return years;
	}

	public void setYears(Integer years) {
		getStateHelper().put(PropertyKeys.years, years);
	}
}
