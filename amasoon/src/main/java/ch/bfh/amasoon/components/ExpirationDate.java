package ch.bfh.amasoon.components;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

@FacesComponent(ExpirationDate.COMPONENT_TYPE)
public class ExpirationDate extends UIInput {

	public static final String COMPONENT_TYPE = "ch.bfh.amasoon.components.ExpirationDate";

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
