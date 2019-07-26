package com.mvc.GridRenderers;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class MSRenderer<T> extends ComponentRenderer<VerticalLayout, T> {

	private static final long serialVersionUID = -2644059608683361516L;

	@Override
	public VerticalLayout createComponent(T item) {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidthFull();

		return layout;
	}
}
