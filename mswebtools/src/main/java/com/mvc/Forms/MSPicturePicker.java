package com.mvc.Forms;

import java.io.ByteArrayInputStream;

import org.apache.commons.io.IOUtils;

import com.github.appreciated.card.ClickableCard;
import com.mvc.MSUpload.MSUpload;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;

public abstract class MSPicturePicker extends ClickableCard {

	private static final long serialVersionUID = 8894386732605992019L;

	private Image image;
	private MemoryBuffer buffer;
	private MSUpload upload;
	private byte[] value;

	public MSPicturePicker() {
		this.image = new Image();
		this.image.setWidthFull();
		this.image.setMaxHeight("225px");

		this.buffer = new MemoryBuffer();

		this.upload = new MSUpload(this.buffer);
		this.upload.setWidth("93%");
		this.upload.addSucceededListener(event -> {
			Notification.show(this.onPictureLoadedNotification());

			this.image.setSrc(this.generateImage());
		});
		this.upload.addAnotherAbortListener(event -> {
			this.image.setSrc("");
		});

		this.add(this.image, this.upload);

		this.setWidthFull();
	}

	public StreamResource generateImage() {
		StreamResource streamResource = new StreamResource("image", () -> {
			try {
				this.value = IOUtils.toByteArray(this.buffer.getInputStream());

				return new ByteArrayInputStream(value);
			} catch (Exception e) {
				e.printStackTrace();

				return null;
			}
		});

		return streamResource;
	}

	public abstract String onPictureLoadedNotification();

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public MemoryBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(MemoryBuffer buffer) {
		this.buffer = buffer;
	}

	public MSUpload getUpload() {
		return this.upload;
	}

	public void setPhoto(MSUpload upload) {
		this.upload = upload;
	}

	public byte[] getValue() {
		return this.value;
	}

	public void setImage(AbstractStreamResource resource) {
		this.image.setSrc(resource);
	}

	public void setImage(String resource) {
		this.image.setSrc(resource);
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public void reloadImage() {
		this.setImage(this.generateImage());
	}
}
