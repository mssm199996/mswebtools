package com.mvc.Dashboard;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.github.appreciated.app.layout.behaviour.Behaviour;
import com.github.appreciated.app.layout.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftHeaderItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.entity.Section;
import com.github.appreciated.app.layout.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.notification.entitiy.DefaultNotification;
import com.github.appreciated.app.layout.router.AppLayoutRouterLayout;
import com.mvc.Button.MSButton;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;

@Push
@HtmlImport("/frontend/styles/custom_themes/ms-custom-theme.html")
public abstract class MSGenericDashboard extends AppLayoutRouterLayout {

	private static final long serialVersionUID = 569384463483453615L;

	private DefaultNotificationHolder notifications;
	private MessageSource messageSource;

	public MSGenericDashboard(MessageSource messageSource) {
		super();

		this.messageSource = messageSource;
		this.notifications = new DefaultNotificationHolder(newStatus -> {
		});

		this.init(this.getGenericAppLayout().build());
	}

	protected AppLayoutBuilder getGenericAppLayout() {
		return AppLayoutBuilder.get(Behaviour.LEFT_RESPONSIVE_HYBRID)
				.withTitle(this.getApplicationName())
				.withAppBar(this.getGenericAppBar().build())
				.withAppMenu(this.getGenericAppMenu().build());
	}

	protected LeftAppMenuBuilder getGenericAppMenu() {
		LeftClickableItem disconnectionLeftNavigationItem = new LeftClickableItem(
				this.messageSource.getMessage("disconnection_label", null,
						Locale.getDefault()), VaadinIcon.EXIT.create(),
				event -> {
					this.logout();
				});

		LeftAppMenuBuilder appMenuBuilder = LeftAppMenuBuilder.get();

		appMenuBuilder.addToSection(
				new LeftHeaderItem(this.getApplicationName(), this
						.getApplicationDescription(), this
						.getLeftSideNavigationRoundImage()), Section.HEADER);

		appMenuBuilder.add(new LeftNavigationItem(this.messageSource
				.getMessage("my_profil_label", null, Locale.getDefault()),
				VaadinIcon.USER.create(), this.getProfilNavigationLayout()));

		appMenuBuilder.addToSection(disconnectionLeftNavigationItem, Section.FOOTER);

		return appMenuBuilder;
	}

	protected AppBarBuilder getGenericAppBar() {
		MSButton disconnectionButton = new MSButton("error", VaadinIcon.EXIT,
				event -> {
					this.logout();
				});

		AppBarBuilder appBarBuilder = AppBarBuilder.get();
		appBarBuilder.add(disconnectionButton);

		return appBarBuilder;
	}

	public void addNotification(DefaultNotification notification) {
		this.notifications.addNotification(notification);
	}

	protected void logout() {
		String js = "window.location.href='/logout'";

		UI.getCurrent().getPage().executeJavaScript(js);
		UI.getCurrent().getSession().close();
	}

	protected abstract String getApplicationName();

	protected abstract String getApplicationDescription();

	protected abstract String getLeftSideNavigationRoundImage();

	protected abstract Class<? extends MSGenericProfilLayout> getProfilNavigationLayout();
}
