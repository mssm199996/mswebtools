package com.mvc.GridRenderers;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class MSTableGridRenderer<T> extends MSRenderer<T> {

	private static final long serialVersionUID = 7983456595156222977L;

	@Override
	public VerticalLayout createComponent(T item) {
		VerticalLayout layout = super.createComponent(item);
		layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		layout.setWidthFull();
		layout.addClassName("p-0");
		
		return layout;
	}

	public abstract String getColumnKey();
}
