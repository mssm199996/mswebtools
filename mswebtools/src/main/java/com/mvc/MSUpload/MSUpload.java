package com.mvc.MSUpload;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

public class MSUpload extends Upload {

	private static final long serialVersionUID = -1399191830437461085L;

	public MSUpload(MemoryBuffer memoryBuffer) {
		super(memoryBuffer);
	}

	public void addAnotherAbortListener(
			ComponentEventListener<UploadAbortEvent<Upload>> listener) {
		super.addUploadAbortListener(listener);
	}
}
