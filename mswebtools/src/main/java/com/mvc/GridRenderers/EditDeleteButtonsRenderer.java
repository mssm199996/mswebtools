package com.mvc.GridRenderers;

import java.util.function.Consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.Button.MSButton;
import com.mvc.Forms.MSDialogForm;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class EditDeleteButtonsRenderer<T> extends DeleteButtonRenderer<T> {

	private static final long serialVersionUID = 5191336668979615174L;

	private MSDialogForm<T> form;

	public EditDeleteButtonsRenderer(JpaRepository<T, ?> repository,
			MSDialogForm<T> form, Consumer<T> editSuccessfullCallback,
			Consumer<T> deleteSuccessfullCallback,
			Consumer<T> beforeDeleteCallback) {

		super(repository, deleteSuccessfullCallback, beforeDeleteCallback);

		this.form = form;
	}

	@Override
	public VerticalLayout createComponent(T item) {
		VerticalLayout layout = super.createComponent(item);

		Icon editIcon = VaadinIcon.EDIT.create();
		editIcon.setColor("orange");

		MSButton editButton = new MSButton("primary", VaadinIcon.EDIT);
		editButton.addClickListener(event -> {
			this.form.setUpdate(true);
			this.form.setBindedEntity(item);
			this.form.open();
		});

		HorizontalLayout subLayout = (HorizontalLayout) layout
				.getComponentAt(0);
		subLayout.addComponentAsFirst(editButton);

		return layout;
	}

	public MSDialogForm<T> getForm() {
		return form;
	}

	public void setForm(MSDialogForm<T> form) {
		this.form = form;
	}

	@Override
	public String getColumnKey() {
		return "deleteEditColumn";
	}
}