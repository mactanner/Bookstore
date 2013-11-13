package ch.bfh.amasoon.components;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;

/**
 * The class NumberSelector implements an input component to select a number
 * within a range.
 */
@FacesComponent(NumberSelector.COMPONENT_TYPE)
public class NumberSelector extends UIInput {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final String COMPONENT_TYPE = "ch.bfh.amasoon.components.NumberSelector";

    public enum PropertyKeys {

        min, max
    }

    public int getMin() {
        return (Integer) getStateHelper().eval(PropertyKeys.min);
    }

    public void setMin(int min) {
        getStateHelper().put(PropertyKeys.min, min);
    }

    public int getMax() {
        return (Integer) getStateHelper().eval(PropertyKeys.max);
    }

    public void setMax(int max) {
        getStateHelper().put(PropertyKeys.max, max);
    }
}
