package ch.bfh.amasoon.components;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;

/**
 * The class NumberSelectorTagHandler implements a tag handler for a number
 * selector.
 */
public class NumberSelectorTagHandler extends ComponentHandler {

	public NumberSelectorTagHandler(ComponentConfig config) {
		super(config);
		getRequiredAttribute(NumberSelector.PropertyKeys.min.name());
		getRequiredAttribute(NumberSelector.PropertyKeys.max.name());
	}
}
