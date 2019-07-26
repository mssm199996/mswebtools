package com.mvc.Authentication;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginI18n.ErrorMessage;
import com.vaadin.flow.component.login.LoginOverlay;

@HtmlImport("styles/authentification/ms_login_form.html")
public abstract class MSLoginForm extends LoginOverlay {

	private static final long serialVersionUID = 7031302605042464540L;

	public MSLoginForm(MessageSource messageSource) {
		super();

		LoginI18n loginUi = this.constructLoginI18n(messageSource);

		this.setAction("loginService");
		this.setTitle(this.getApplicationName());
		this.setDescription(this.getApplicationDescription());
		this.setForgotPasswordButtonVisible(false);
		this.onEnabledStateChanged(true);
		this.setOpened(true);
		this.setI18n(loginUi);
	}

	public LoginI18n constructLoginI18n(MessageSource messageSource) {
		String errorTitle = messageSource.getMessage(
				"authentication_failed_error_title", null, Locale.getDefault());

		String errorMessageContent = messageSource.getMessage(
				"authentication_failed_error_content", null,
				Locale.getDefault());

		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setTitle(errorTitle);
		errorMessage.setMessage(errorMessageContent);

		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setErrorMessage(errorMessage);
		i18n.setAdditionalInformation("@" + this.getApplicationName()
				+ " Copyright");

		return i18n;
	}

	public abstract String getApplicationName();

	public abstract String getApplicationDescription();
}
