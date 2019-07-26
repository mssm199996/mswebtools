package com.mvc.Dashboard;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mvc.Forms.MSSimpleForm;
import com.persistance.DomainModel.Account;
import com.persistance.DomainModel.GenericResponse;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.PasswordField;

public abstract class MSGenericPasswordLayout<A extends Account> extends
		MSSimpleForm<A> {

	private static final long serialVersionUID = -8200786078310719339L;

	private PasswordField oldPassword;
	private PasswordField newPassword;
	private PasswordField confirmPassword;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@SuppressWarnings("unchecked")
	public MSGenericPasswordLayout(MessageSource messageSource) {
		super(messageSource);

		this.setUpdate(true);
		this.setBindedEntity((A) this.getMainAccount());
		this.readBean();
	}

	@Override
	public String getFormTitle() {
		return this.messageSource.getMessage("password_update_header", null,
				Locale.getDefault());
	}

	@Override
	public Map<String, Component> beanFieldsToDisplayedUIFields() {
		Map<String, Component> result = new LinkedHashMap<>();
		result.put("oldPassword", this.oldPassword);
		result.put("newPassword", this.newPassword);
		result.put("confirmPassword", this.confirmPassword);

		return result;
	}

	@Override
	public Map<String, AbstractSinglePropertyField<?, ?>> beanFieldsToUIFields() {
		Map<String, AbstractSinglePropertyField<?, ?>> result = new LinkedHashMap<>();

		return result;
	}

	@Override
	public Map<String, VaadinIcon> beanFieldsToIcons() {
		HashMap<String, VaadinIcon> result = new LinkedHashMap<>();
		result.put("oldPassword", VaadinIcon.QUESTION_CIRCLE);
		result.put("newPassword", VaadinIcon.PASSWORD);
		result.put("confirmPassword", VaadinIcon.KEY);

		return result;
	}

	@Override
	public Map<String, String> beanFieldsToLabels() {
		String oldPasswordLabel = this.messageSource.getMessage(
				"old_password_label", null, Locale.getDefault());

		String passwordLabel = this.messageSource.getMessage(
				"new_password_label", null, Locale.getDefault());

		String confirmPasswordLabel = this.messageSource.getMessage(
				"confirm_password_label", null, Locale.getDefault());

		HashMap<String, String> result = new LinkedHashMap<>();
		result.put("oldPassword", oldPasswordLabel);
		result.put("newPassword", passwordLabel);
		result.put("confirmPassword", confirmPasswordLabel);

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
		this.oldPassword = new PasswordField();
		this.newPassword = new PasswordField();
		this.confirmPassword = new PasswordField();
	}

	@Override
	protected GenericResponse checkBeforeValidate(A entity) {
		String newPassword = this.newPassword.getValue();
		String confirmedPassword = this.confirmPassword.getValue();

		if (newPassword.length() >= 6) {
			if (newPassword.equals(confirmedPassword)) {
				String oldPassword = this.oldPassword.getValue();

				boolean isSameAsOldPassword = this.passwordEncoder.matches(
						oldPassword, entity.getPassword());

				if (isSameAsOldPassword) {
					entity.setPassword(this.passwordEncoder.encode(newPassword));

					this.oldPassword.setValue("");
					this.newPassword.setValue("");
					this.confirmPassword.setValue("");

					return new GenericResponse(this.messageSource,
							"password_update_successful");
				} else
					return new GenericResponse(this.messageSource,
							"not_right_old_password", true);
			} else
				return new GenericResponse(this.messageSource,
						"not_well_confirmed_new_password", true);
		} else
			return new GenericResponse(this.messageSource,
					"password_too_short_error", true);
	}

	@Override
	public HtmlContainer constructHeaderComponent() {
		return new H2();
	}
}
