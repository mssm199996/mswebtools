package com.mvc.Dashboard;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.context.MessageSource;

import com.github.appreciated.app.layout.component.menu.RoundImage;
import com.github.appreciated.card.AbstractCard;
import com.github.appreciated.card.Card;
import com.github.appreciated.card.RippleClickableCard;
import com.github.appreciated.card.content.IconItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public abstract class MSGenericProfilLayout extends VerticalLayout {

	private static final long serialVersionUID = -8084821804367207831L;

	private MessageSource messageSource;

	public MSGenericProfilLayout(MessageSource messageSource) {
		super();

		this.messageSource = messageSource;
	}

	@PostConstruct
	public void addProfilLayoutContents() {
		this.add(this.initFirstLineResponsiveRow());
	}

	protected HorizontalLayout initFirstLineResponsiveRow() {
		HorizontalLayout result = new HorizontalLayout();
		result.add(this.getIdentityCard());
		result.add(this.getAccountCard());
		result.setWidthFull();

		return result;
	}

	protected AbstractCard<?> getAccountCard() {

		Map<Tab, Component> tabsToComponents = this.getAccountComponents();

		Tabs tabs = new Tabs();
		for (Tab tab : tabsToComponents.keySet())
			tabs.add(tab);

		Component firstComponent = tabsToComponents.get(tabs.getComponentAt(0));

		Set<Component> pagesShown = new LinkedHashSet<>();
		pagesShown.add(firstComponent);

		Div pages = new Div();
		pages.setWidthFull();

		for (Component component : tabsToComponents.values()) {
			component.setVisible(false);

			pages.add(component);
		}

		firstComponent.setVisible(true);

		tabs.addSelectedChangeListener(event -> {
			pagesShown.forEach(page -> page.setVisible(false));
			pagesShown.clear();

			Component selectedPage = tabsToComponents.get(tabs.getSelectedTab());
			selectedPage.setVisible(true);

			pagesShown.add(selectedPage);
		});

		Card result = new Card(tabs);
		result.setWidth("50%");
		result.setHeight("450px");
		result.add(tabs);
		result.add(pages);

		return result;
	}

	protected AbstractCard<?> getIdentityCard() {
		RoundImage indentityImage = new RoundImage(
				this.getIdentityCardProfilImage(), "50px", "50px");

		Image backgroundImage = new Image(
				this.getIdentityCardBackgroundImage(), "");
		backgroundImage.setWidth("100%");
		backgroundImage.setHeight("100%");

		RippleClickableCard card = new RippleClickableCard(event -> {
			Notification.show(this.getOnIndentityCardClickedNotification());
		}, new IconItem(indentityImage, this.getIdentityCardTitle(),
				this.getIdentityCardDescription()));
		card.setWidth("50%");
		card.setHeight("350px");
		card.add(backgroundImage);

		return card;
	}

	protected Map<Tab, Component> getAccountComponents() {
		String userLabel = this.messageSource.getMessage("user_label", null,
				Locale.getDefault());
		String passwordLabel = this.messageSource.getMessage("password_label",
				null, Locale.getDefault());

		Tab userTab = new Tab(VaadinIcon.USER_CARD.create(), new Label(
				userLabel));
		Tab passwordTab = new Tab(VaadinIcon.PASSWORD.create(), new Label(
				passwordLabel));

		Map<Tab, Component> result = new LinkedHashMap<>();
		result.put(userTab, this.getUserLayout());
		result.put(passwordTab, this.getPasswordLayout());

		return result;
	}

	protected abstract MSGenericUserLayout<?> getUserLayout();

	protected abstract MSGenericPasswordLayout<?> getPasswordLayout();

	public abstract String getIdentityCardBackgroundImage();

	public abstract String getIdentityCardProfilImage();

	public abstract String getIdentityCardTitle();

	public abstract String getIdentityCardDescription();

	public abstract String getOnIndentityCardClickedNotification();
}
