package com.mvc.Grid;

import java.util.function.Consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.GridRenderers.EditDeleteButtonsRenderer;

public abstract class MSEditableDeletableGrid<T, R extends JpaRepository<T, ?>>
		extends MSDeletableGrid<T, R, EditDeleteButtonsRenderer<T>> {

	private static final long serialVersionUID = -3184172229971364484L;

	public MSEditableDeletableGrid(Class<T> entityClass, R repository) {
		super(entityClass, repository);
	}

	@Override
	protected EditDeleteButtonsRenderer<T> constructLastColumnRenderer() {
		return new EditDeleteButtonsRenderer<T>(this.getRepository(), null,
				this.onEditedSuccessfullCallback(),
				this.onDeletedSuccessfullCallback(),
				this.beforeDeleteCallback());
	}

	public Consumer<T> onEditedSuccessfullCallback() {
		return (entity -> {
			this.getDataProvider().refreshItem(entity);
		});
	}
}
