package com.mvc.Button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;

public class MSButton extends Button {

	private static final long serialVersionUID = -6776799898149605680L;

	public MSButton(String caption, String colorClass, VaadinIcon vaadinIcon,
			ComponentEventListener<ClickEvent<Button>> clickListener) {

		this.addClassNames(colorClass, "box", "xs", "shadow");

		if (caption != null)
			this.setText(caption);

		if (clickListener != null)
			this.addClickListener(clickListener);

		if (vaadinIcon != null && caption != null)
			this.addToPrefix(vaadinIcon.create());
		else if (vaadinIcon != null)
			this.setIcon(vaadinIcon.create());
	}

	public MSButton(String caption, String colorClass, VaadinIcon vaadinIcon) {
		this(caption, colorClass, vaadinIcon, null);
	}

	public MSButton(String colorClass, VaadinIcon vaadinIcon,
			ComponentEventListener<ClickEvent<Button>> clickListener) {
		this(null, colorClass, vaadinIcon, clickListener);
	}

	public MSButton(String colorClass, VaadinIcon vaadinIcon) {
		this(null, colorClass, vaadinIcon, null);
	}
}
