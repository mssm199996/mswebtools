package com.mvc.GridRenderers;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MSIndexRenderer<T> extends MSTableGridRenderer<T> {

	private static final long serialVersionUID = 7035015246440969267L;

	private int index = 0;

	@Override
	public VerticalLayout createComponent(T item) {
		VerticalLayout layout = super.createComponent(item);
		layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		layout.setWidthFull();

		HorizontalLayout subLayout = new HorizontalLayout();
		subLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		subLayout.setWidthFull();

		Label label = new Label(++this.index + "");

		subLayout.add(label);
		layout.add(subLayout);

		return layout;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String getColumnKey() {
		return "rowIndex";
	}
}
