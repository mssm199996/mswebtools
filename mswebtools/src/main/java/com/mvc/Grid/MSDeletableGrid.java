package com.mvc.Grid;

import java.util.function.Consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.GridRenderers.DeleteButtonRenderer;

public abstract class MSDeletableGrid<T, R extends JpaRepository<T, ?>, RENDERER extends DeleteButtonRenderer<T>>
		extends MSTableGrid<T, R> {

	private static final long serialVersionUID = -2958244131317173063L;

	protected RENDERER lastColumnRenderer;

	public MSDeletableGrid(Class<T> entityClass, R repository) {
		super(entityClass, repository);
	}

	@Override
	protected void onBeforeInitColumnHeaders() {
		this.addLastColumn();
	}

	protected void addLastColumn() {
		this.lastColumnRenderer = this.constructLastColumnRenderer();

		this.addColumn(this.lastColumnRenderer);
	}

	@SuppressWarnings("unchecked")
	protected RENDERER constructLastColumnRenderer() {
		return (RENDERER) new DeleteButtonRenderer<T>(this.getRepository(),
				this.onDeletedSuccessfullCallback(),
				this.beforeDeleteCallback());
	}

	public RENDERER getLastColumnRenderer() {
		return this.lastColumnRenderer;
	}

	@Override
	public void setRepository(R repository) {
		super.setRepository(repository);
		
		this.lastColumnRenderer.setRepository(repository);
	}

	public Consumer<T> onDeletedSuccessfullCallback() {
		return (event -> {
			this.getDataProvider().refreshAll();
		});
	}

	public Consumer<T> beforeDeleteCallback() {
		return (event -> {
		});
	}
}
