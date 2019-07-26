package com.mvc.Forms;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mvc.Button.MSButton;
import com.persistance.DomainModel.Account;
import com.persistance.DomainModel.GenericResponse;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.HasPrefixAndSuffix;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public abstract class MSSimpleForm<T> extends VerticalLayout {

	private static final long serialVersionUID = 560843195653003796L;
	protected Consumer<T> onInsertSuccessfullCallback;
	protected Consumer<T> onUpdateSuccessfullCallback;
	protected Consumer<T> onCancelCallback;
	protected Consumer<T> onBeforePersistCallback;
	protected Binder<T> binder;
	protected T bindedEntity;
	protected boolean update;
	protected MessageSource messageSource;
	protected FormLayout form;

	public MSSimpleForm(MessageSource messageSource) {
		this.messageSource = messageSource;

		this.constructHeader();
		this.constructForm();
		this.onBeforeConstructingFooter();
		this.constructFooter();

		this.setWidthFull();
		this.addClassNames("pr-20", "pl-20");
	}

	public void readBean() {
		this.binder.setBean(this.getBindedEntity());
		this.binder.readBean(this.binder.getBean());

		this.focusMainInput();
	}

	private void constructHeader() {
		HtmlContainer title = this.constructHeaderComponent();
		title.setText(this.getFormTitle());
		title.setWidthFull();
		title.addClassName("centered-text");

		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidthFull();
		layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		layout.setAlignItems(Alignment.CENTER);

		VerticalLayout subLayout = new VerticalLayout();
		subLayout.setWidthFull();
		subLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		subLayout.setAlignItems(Alignment.CENTER);
		subLayout.add(title);
		subLayout.addClassName("p-0");

		layout.add(subLayout);
		this.add(layout);
	}

	protected void constructForm() {
		this.binder = new Binder<T>(this.getEntityClassForBinder());

		this.constructFormFields();

		Map<String, String> beanFieldsToLabels = this.beanFieldsToLabels();
		Map<String, VaadinIcon> beansFieldsToIcons = this.beanFieldsToIcons();

		Map<String, Component> beanFieldsToDisplayedUIFields = this
				.beanFieldsToDisplayedUIFields();

		Set<String> beanFields = beanFieldsToDisplayedUIFields.keySet();

		if (this.isAddElementAutomatically()) {
			this.form = new FormLayout();
			this.add(this.form);
		}

		for (String fieldName : beanFields) {
			String label = beanFieldsToLabels.get(fieldName);
			VaadinIcon vaadinIcon = beansFieldsToIcons.get(fieldName);
			Component component = beanFieldsToDisplayedUIFields.get(fieldName);

			if (vaadinIcon != null && component instanceof HasPrefixAndSuffix) {
				Icon icon = vaadinIcon.create();
				icon.setColor("BLUE");

				((HasPrefixAndSuffix) component).setPrefixComponent(icon);
			}

			if (component instanceof HasSize)
				((HasSize) component).setWidthFull();

			if (component instanceof AbstractSinglePropertyField<?, ?>) {
				component.getElement().setProperty("label", label);
			}

			if (this.isAddElementAutomatically()) {
				this.form.add(component);
			}
		}

		Map<String, AbstractSinglePropertyField<?, ?>> beanFieldsToUIFields = this
				.beanFieldsToUIFields();

		beanFields = beanFieldsToUIFields.keySet();

		for (String fieldName : beanFields) {
			AbstractSinglePropertyField<?, ?> uiField = beanFieldsToUIFields
					.get(fieldName);

			String placeHolder = beanFieldsToLabels.get(fieldName);
			uiField.getElement().setProperty("label", placeHolder);

			this.binder.bind(uiField, fieldName);
		}
	}

	protected void constructFooter() {
		String confirmLabel = this.messageSource.getMessage("confirm_label",
				null, Locale.getDefault());

		MSButton confirm = new MSButton(
				confirmLabel,
				"success",
				VaadinIcon.CHECK_CIRCLE_O,
				event -> {
					try {
						T entity = this.getBinder().getBean();

						GenericResponse response = this
								.checkBeforeValidate(entity);

						if (!response.isError()) {
							this.getBinder().writeBean(entity);

							if (this.getOnBeforePersistCallback() != null)
								this.getOnBeforePersistCallback()
										.accept(entity);

							this.getRepository().save(entity);

							if (this.isUpdate()
									&& this.getOnUpdateSuccessfullCallback() != null)
								this.getOnUpdateSuccessfullCallback().accept(
										entity);

							if (!this.isUpdate()) {
								this.getBinder().setBean(
										this.instanciateEntity());
								this.getOnResetFocusableComponent().focus();

								if (this.getOnInsertSuccessfullCallback() != null)
									this.getOnInsertSuccessfullCallback()
											.accept(entity);
							}
						}

						Notification.show(response.getMessage(), 3000,
								Position.TOP_END);
					} catch (ValidationException e) {
						Notification.show(e.getMessage(), 3000,
								Position.TOP_END);
					}
				});

		confirm.addClickShortcut(Key.ENTER);
		confirm.setWidthFull();

		HorizontalLayout wrapper = new HorizontalLayout();
		wrapper.setWidth("100%");
		wrapper.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		wrapper.setAlignItems(Alignment.CENTER);

		if (this.isCancelable()) {
			String cancelLabel = this.messageSource.getMessage("cancel_label",
					null, Locale.getDefault());

			MSButton cancel = new MSButton(cancelLabel, "error",
					VaadinIcon.EXIT_O, event -> {
						if (this.getOnCancelCallback() != null) {
							T entity = this.getBinder().getBean();

							this.getOnCancelCallback().accept(entity);
						}
					});

			cancel.addClickShortcut(Key.ESCAPE);
			cancel.setWidthFull();

			wrapper.add(cancel);
		}

		wrapper.add(confirm);

		VerticalLayout layout = new VerticalLayout();
		layout.add(wrapper);
		layout.setWidth("100%");
		layout.setAlignItems(Alignment.END);
		layout.addClassNames("p-0");

		this.add(layout);
	}

	public abstract String getFormTitle();

	public abstract Map<String, Component> beanFieldsToDisplayedUIFields();

	public abstract Map<String, AbstractSinglePropertyField<?, ?>> beanFieldsToUIFields();

	public abstract Map<String, VaadinIcon> beanFieldsToIcons();

	public abstract Map<String, String> beanFieldsToLabels();

	public abstract Class<T> getEntityClassForBinder();

	public abstract T instanciateEntity();

	protected abstract Focusable<?> getOnResetFocusableComponent();

	protected abstract void constructFormFields();

	protected abstract JpaRepository<T, ?> getRepository();

	protected GenericResponse checkBeforeValidate(T entity) {
		return new GenericResponse(this.messageSource);
	}

	public Consumer<T> getOnInsertSuccessfullCallback() {
		return onInsertSuccessfullCallback;
	}

	public void setOnInsertSuccessfullCallback(
			Consumer<T> onInsertSuccessfullCallback) {
		this.onInsertSuccessfullCallback = onInsertSuccessfullCallback;
	}

	public Consumer<T> getOnUpdateSuccessfullCallback() {
		return onUpdateSuccessfullCallback;
	}

	public void setOnUpdateSuccessfullCallback(
			Consumer<T> onUpdateSuccessfullCallback) {
		this.onUpdateSuccessfullCallback = onUpdateSuccessfullCallback;
	}

	public Consumer<T> getOnCancelCallback() {
		return onCancelCallback;
	}

	public void setOnCancelCallback(Consumer<T> onCancelCallback) {
		this.onCancelCallback = onCancelCallback;
	}

	public Consumer<T> getOnBeforePersistCallback() {
		return onBeforePersistCallback;
	}

	public void setOnBeforePersistCallback(Consumer<T> onBeforePersistCallback) {
		this.onBeforePersistCallback = onBeforePersistCallback;
	}

	public Binder<T> getBinder() {
		return binder;
	}

	public void setBinder(Binder<T> binder) {
		this.binder = binder;
	}

	public T getBindedEntity() {
		return bindedEntity;
	}

	public void setBindedEntity(T bindedEntity) {
		this.bindedEntity = bindedEntity;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void focusMainInput() {
		if (this.getOnResetFocusableComponent() != null)
			this.getOnResetFocusableComponent().focus();
	}

	public Account getMainAccount() {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		if (principal instanceof Account)
			return (Account) principal;

		return null;
	}

	public boolean isCancelable() {
		return false;
	}

	public HtmlContainer constructHeaderComponent() {
		return new H1();
	}

	public Boolean isAddElementAutomatically() {
		return true;
	}

	public void onBeforeConstructingFooter() {

	}
}
