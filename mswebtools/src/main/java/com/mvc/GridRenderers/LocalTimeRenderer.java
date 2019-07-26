package com.mvc.GridRenderers;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

public class LocalTimeRenderer<SOURCE> extends BasicRenderer<SOURCE, LocalTime> {

	private static final long serialVersionUID = -6920383165174652660L;

	private DateTimeFormatter formatter;
	private String nullRepresentation;

	public LocalTimeRenderer(ValueProvider<SOURCE, LocalTime> valueProvider,
			String formatPattern) {
		this(valueProvider, formatPattern, Locale.getDefault());
	}

	public LocalTimeRenderer(ValueProvider<SOURCE, LocalTime> valueProvider,
			String formatPattern, Locale locale) {
		this(valueProvider, formatPattern, locale, "");
	}

	public LocalTimeRenderer(ValueProvider<SOURCE, LocalTime> valueProvider,
			String formatPattern, Locale locale, String nullRepresentation) {
		super(valueProvider);

		if (formatPattern == null) {
			throw new IllegalArgumentException("format pattern may not be null");
		}

		if (locale == null) {
			throw new IllegalArgumentException("locale may not be null");
		}

		formatter = DateTimeFormatter.ofPattern(formatPattern, locale);
		this.nullRepresentation = nullRepresentation;
	}

	public LocalTimeRenderer(ValueProvider<SOURCE, LocalTime> valueProvider,
			DateTimeFormatter formatter) {
		this(valueProvider, formatter, "");
	}

	public LocalTimeRenderer(ValueProvider<SOURCE, LocalTime> valueProvider,
			DateTimeFormatter formatter, String nullRepresentation) {
		super(valueProvider);

		if (formatter == null) {
			throw new IllegalArgumentException("formatter may not be null");
		}

		this.formatter = formatter;
	}

	@Override
	protected String getFormattedValue(LocalTime localTime) {
		try {
			return localTime == null ? nullRepresentation : formatter.format(localTime);
		} catch (Exception e) {
			throw new IllegalStateException("Could not format input date '"
					+ localTime + "' using formatter '" + formatter + "'", e);
		}
	}
}
