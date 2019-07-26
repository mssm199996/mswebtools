package com.mvc.GridRenderers;

import java.util.function.Consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.Button.MSButton;
import com.mvc.UITools.AlertsDisplayer;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DeleteButtonRenderer<T> extends MSTableGridRenderer<T> {

	private static final long serialVersionUID = 3552892161403624345L;

	protected JpaRepository<T, ?> repository;
	private Consumer<T> deleteSuccessfullCallback;
	private Consumer<T> beforeDeleteCallback;

	public DeleteButtonRenderer(JpaRepository<T, ?> repository,
			Consumer<T> deleteSuccessfullCallback,
			Consumer<T> beforeDeleteCallback) {

		this.repository = repository;
		this.deleteSuccessfullCallback = deleteSuccessfullCallback;
		this.beforeDeleteCallback = beforeDeleteCallback;
	}

	@Override
	public VerticalLayout createComponent(T item) {
		VerticalLayout layout = super.createComponent(item);
		layout.setDefaultHorizontalComponentAlignment(Alignment.END);

		HorizontalLayout subLayout = new HorizontalLayout();
		subLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

		MSButton deleteButton = new MSButton("error", VaadinIcon.TRASH);
		deleteButton.addClickListener(event -> {
			AlertsDisplayer.displayConfirmationAlert("",
					"Confirmation de suppression",
					"Voulez vous vraiment supprimer l'élément selectionné ?", (
							label) -> {

						this.beforeDeleteCallback.accept(item);
						this.repository.delete(item);
						this.deleteSuccessfullCallback.accept(item);
					});

		});

		subLayout.add(deleteButton);
		layout.add(subLayout);

		return layout;
	}

	@Override
	public String getColumnKey() {
		return "deleteColumn";
	}

	public JpaRepository<T, ?> getRepository() {
		return repository;
	}

	public void setRepository(JpaRepository<T, ?> repository) {
		this.repository = repository;
	}

	public Consumer<T> getDeleteSuccessfullCallback() {
		return deleteSuccessfullCallback;
	}

	public void setDeleteSuccessfullCallback(
			Consumer<T> deleteSuccessfullCallback) {
		this.deleteSuccessfullCallback = deleteSuccessfullCallback;
	}

	public Consumer<T> getBeforeDeleteCallback() {
		return beforeDeleteCallback;
	}

	public void setBeforeDeleteCallback(Consumer<T> beforeDeleteCallback) {
		this.beforeDeleteCallback = beforeDeleteCallback;
	}
}
