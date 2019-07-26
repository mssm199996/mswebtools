package com.mvc.Authentication;

import org.springframework.context.MessageSource;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@Theme(value = Material.class)
@StyleSheet("styles/authentification/ms_login_page.css")
public abstract class MSLoginPage extends HorizontalLayout implements
		AfterNavigationObserver {

	private static final long serialVersionUID = 8673461297922218502L;

	private MSLoginForm loginForm;
	private VerticalLayout container;

	public MSLoginPage(MessageSource messageSource) {
		super();

		this.initComponents(messageSource);
	}

	private void initComponents(MessageSource messageSource) {
		this.loginForm = this.createLoginForm(messageSource);

		this.container = new VerticalLayout();
		this.container.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		this.container.add(this.loginForm);

		this.addClassName("login-page-container");
		this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		this.setHeightFull();
		this.add(this.container);
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		boolean isError = event.getLocation().getQueryParameters()
				.getParameters().containsKey("error");
		this.loginForm.setError(isError);
	}

	public abstract MSLoginForm createLoginForm(MessageSource messageSource);
}
