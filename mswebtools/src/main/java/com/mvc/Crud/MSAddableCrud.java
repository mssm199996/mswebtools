package com.mvc.Crud;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.Button.MSButton;
import com.mvc.Forms.MSCancelableForm;
import com.mvc.Forms.MSDialogForm;
import com.mvc.Grid.MSEditableDeletableGrid;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;

public abstract class MSAddableCrud<T, F extends MSCancelableForm<T>, R extends JpaRepository<T, ?>>
		extends MSCrud<T, R, MSEditableDeletableGrid<T, R>> {

	private static final long serialVersionUID = 5212473071566643464L;
	protected MSDialogForm<T> dialogForm;

	@Override
	protected void initComponents() {
		super.initComponents();

		this.initForm();
	}

	private void initForm() {
		this.dialogForm = new MSDialogForm<>(this.getForm());
		this.dialogForm.setOnUpdateSuccessfullCallback(entity -> {
			this.dialogForm.close();
			this.grid.getDataProvider().refreshItem(entity);
		});

		this.dialogForm.setOnInsertSuccessfullCallback(entity -> {
			this.grid.getDataProvider().refreshAll();
		});

		this.grid.getLastColumnRenderer().setForm(this.dialogForm);
	}

	@Override
	protected Set<Component> getRightSideToolBarComponents() {
		MSButton insertButton = new MSButton(this.addButtonLabel(), "success",
				VaadinIcon.PLUS_CIRCLE_O);
		insertButton.setWidthFull();
		insertButton
				.addClickListener(event -> {
					this.dialogForm.setBindedEntity(this.getForm()
							.instanciateEntity());
					this.dialogForm.setUpdate(false);
					this.dialogForm.open();
				});

		Set<Component> result = super.getRightSideToolBarComponents();
		result.add(insertButton);

		return result;
	}

	protected abstract String addButtonLabel();

	protected abstract F getForm();
}
