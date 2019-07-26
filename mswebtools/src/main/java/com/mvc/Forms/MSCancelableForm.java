package com.mvc.Forms;

import org.springframework.context.MessageSource;

public abstract class MSCancelableForm<T> extends MSSimpleForm<T> {

	private static final long serialVersionUID = -2770552299788692009L;

	public MSCancelableForm(MessageSource messageSource) {
		super(messageSource);
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}
