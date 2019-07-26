package com.mvc.Forms;

import java.util.function.Consumer;

import com.vaadin.flow.component.dialog.Dialog;

public class MSDialogForm<T> extends Dialog {

	private static final long serialVersionUID = 6238088969942017393L;

	protected MSCancelableForm<T> formLayout;

	public MSDialogForm(MSCancelableForm<T> msCancelableForm) {
		this.formLayout = msCancelableForm;
		this.formLayout.setOnCancelCallback(event -> {
			this.close();
		});

		this.addOpenedChangeListener(event -> {
			this.formLayout.getBinder().setBean(
					this.formLayout.getBindedEntity());
			this.formLayout.getBinder().readBean(
					this.formLayout.getBinder().getBean());

			if (this.formLayout.getOnResetFocusableComponent() != null)
				this.formLayout.getOnResetFocusableComponent().focus();
		});

		this.add(this.formLayout);
	}

	public void setUpdate(Boolean update) {
		this.formLayout.setUpdate(update);
	}

	public void setBindedEntity(T entity) {
		this.formLayout.setBindedEntity(entity);
	}

	public void setOnInsertSuccessfullCallback(
			Consumer<T> onInsertSuccessfullCallback) {
		this.formLayout
				.setOnInsertSuccessfullCallback(onInsertSuccessfullCallback);
	}

	public void setOnUpdateSuccessfullCallback(
			Consumer<T> onUpdateSuccessfullCallback) {
		this.formLayout
				.setOnUpdateSuccessfullCallback(onUpdateSuccessfullCallback);
	}

	public T instanciateEntity() {
		return this.formLayout.instanciateEntity();
	}
}
