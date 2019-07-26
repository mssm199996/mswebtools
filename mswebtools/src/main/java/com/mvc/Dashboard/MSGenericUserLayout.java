package com.mvc.Dashboard;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

import com.mvc.Forms.MSSimpleForm;
import com.persistance.DomainModel.Account;
import com.persistance.DomainModel.GenericResponse;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;

public abstract class MSGenericUserLayout<A extends Account> extends
		MSSimpleForm<A> {

	private static final long serialVersionUID = 8336662881479215571L;

	protected TextField username;
	protected TextField email;

	@SuppressWarnings("unchecked")
	public MSGenericUserLayout(MessageSource messageSource) {
		super(messageSource);

		this.setUpdate(true);
		this.setBindedEntity((A) this.getMainAccount());
		this.readBean();

		((HasStyle) this.username.getParent().get()).addClassName("mt-30");
	}

	@Override
	public Map<String, Component> beanFieldsToDisplayedUIFields() {
		Map<String, Component> result = new HashMap<>();
		result.put("username", this.username);
		result.put("email", this.email);

		return result;
	}

	@Override
	public Map<String, AbstractSinglePropertyField<?, ?>> beanFieldsToUIFields() {
		Map<String, AbstractSinglePropertyField<?, ?>> result = new HashMap<>();
		result.put("username", this.username);
		result.put("email", this.email);

		return result;
	}

	@Override
	public Map<String, VaadinIcon> beanFieldsToIcons() {
		Map<String, VaadinIcon> result = new HashMap<>();
		result.put("username", VaadinIcon.USER);
		result.put("email", VaadinIcon.MAILBOX);

		return result;
	}

	@Override
	public Map<String, String> beanFieldsToLabels() {
		String usernameLabel = this.messageSource.getMessage("username_label",
				null, Locale.getDefault());

		Map<String, String> result = new HashMap<>();
		result.put("username", usernameLabel);
		result.put("email", "E-mail");

		return result;
	}

	@Override
	public A instanciateEntity() {
		return null;
	}

	@Override
	protected Focusable<?> getOnResetFocusableComponent() {
		return null;
	}

	@Override
	protected void constructFormFields() {
		this.username = new TextField();
		this.email = new TextField();
	}

	@Override
	protected GenericResponse checkBeforeValidate(A entity) {
		return new GenericResponse(this.messageSource, "user_update_successful");
	}
}
