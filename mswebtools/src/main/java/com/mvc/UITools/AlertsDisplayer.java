package com.mvc.UITools;

import java.util.function.Consumer;

import com.mvc.Button.MSButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class AlertsDisplayer {

	public static <T extends Component> void displayAlert(String title,
			String header, T contentHolder, VaadinIcon headerIcon,
			boolean includeCancelButton, Consumer<T> onConfirmCallback) {

		if (contentHolder != null && contentHolder instanceof HasSize)
			((HasSize) contentHolder).setWidthFull();

		Dialog dialog = new Dialog();

		VerticalLayout container = new VerticalLayout();

		if (title != null && !title.equals("")) {
			H1 titleHolder = new H1(title);
			titleHolder.addClassName("mt-0");
			titleHolder.setWidthFull();

			container.add(titleHolder);
		}

		if (header != null && !header.equals("")) {
			H3 headerHolder = new H3(header);
			headerHolder.addClassName("mt-0");
			headerHolder.setWidthFull();

			container.add(headerHolder);
		}

		if (container != null && !container.equals("")) {
			container.add(contentHolder);
		}

		HorizontalLayout footerRow = new HorizontalLayout();

		if (includeCancelButton) {
			MSButton cancelButton = new MSButton("Annuler", "error",
					VaadinIcon.EXIT_O, event -> {
						dialog.close();
					});

			footerRow.add(cancelButton);
		}

		MSButton confirmButton = new MSButton("Confirmer", "success",
				VaadinIcon.CHECK_CIRCLE_O, event -> {
					onConfirmCallback.accept(contentHolder);
					dialog.close();
				});

		footerRow.add(confirmButton);

		VerticalLayout footer = new VerticalLayout();
		footer.setDefaultHorizontalComponentAlignment(Alignment.END);
		footer.setAlignItems(Alignment.END);
		footer.setWidthFull();
		footer.add(footerRow);

		container.add(footer);

		dialog.add(container);
		dialog.open();
	}

	private static void displayAlert(String title, String header,
			String content, VaadinIcon headerIcon, boolean includeCancelButton,
			Consumer<Label> onConfirmCallback) {

		Label contentHolder = new Label(content);

		AlertsDisplayer.displayAlert(title, header, contentHolder, headerIcon,
				includeCancelButton, onConfirmCallback);
	}

	public static void displayTextAlert(String title, String header,
			String content, boolean includeCancelButton,
			Consumer<TextField> onConfirmCallback) {

		TextField contentHolder = new TextField();
		contentHolder.setLabel(content);
		contentHolder.setWidthFull();

		AlertsDisplayer.displayAlert(title, header, contentHolder,
				VaadinIcon.QUESTION_CIRCLE_O, includeCancelButton,
				onConfirmCallback);
	}

	public static void displayInformationAlert(String title, String header,
			String content, Consumer<Label> onConfirmCallback) {

		AlertsDisplayer.displayAlert(title, header, content,
				VaadinIcon.EXCLAMATION_CIRCLE_O, false, onConfirmCallback);
	}

	public static void displayErrorAlert(String title, String header,
			String content, Consumer<Label> onConfirmCallback) {

		AlertsDisplayer.displayAlert(title, header, content, VaadinIcon.STOP,
				false, onConfirmCallback);
	}

	public static void displayConfirmationAlert(String title, String header,
			String content, Consumer<Label> onConfirmCallback) {

		AlertsDisplayer.displayAlert(title, header, content,
				VaadinIcon.QUESTION_CIRCLE_O, true, onConfirmCallback);
	}
}
