package com.mvc.Grid;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.GridRenderers.MSIndexRenderer;
import com.mvc.GridRenderers.MSTableGridRenderer;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.Renderer;

public abstract class MSTableGrid<T, R extends JpaRepository<T, ?>> extends
		MSGrid<T, R> {

	private static final long serialVersionUID = -348284859139077351L;
	private MSIndexRenderer<T> indexRenderer;

	public MSTableGrid(Class<T> entityClass, R repository) {
		super(entityClass, repository);
	}

	public MSTableGrid(Class<T> entityClass) {
		super(entityClass);
	}

	@Override
	protected void initGridColumns() {
		this.setColumns();
		this.addIndexColumn();

		for (String columnName : this.getSimplePropertyNamesToShow()) {
			this.addColumn(columnName);
		}

		Map<String, Renderer<T>> renderersAsMap = this
				.propertyNamesToSpecificRenderers();
		Set<String> renderers = renderersAsMap.keySet();

		for (String renderer : renderers) {
			Column<T> column = this.addColumn(renderersAsMap.get(renderer));
			column.setKey(renderer);
		}

		Collection<MSTableGridRenderer<T>> additionalColumns = this
				.getAdditionalColumns();

		for (MSTableGridRenderer<T> columnRenderer : additionalColumns) {
			Column<?> c = this.addColumn(columnRenderer);
			c.setKey(columnRenderer.getColumnKey());
			c.setWidth("25px");
		}

		for (String propertyKeyToCenter : this.getPropertiesKeysToCenter()) {
			System.out.println("key: " + propertyKeyToCenter);

			Column<T> column = this.getColumnByKey(propertyKeyToCenter);
			column.setTextAlign(ColumnTextAlign.CENTER);
		}

		this.onBeforeInitColumnHeaders();
		this.initGridHeaders();

		this.setColumnReorderingAllowed(true);
	}

	protected void addIndexColumn() {
		this.indexRenderer = new MSIndexRenderer<>();

		Column<T> indexColumn = this.addColumn(this.indexRenderer);
		indexColumn.setKey(this.indexRenderer.getColumnKey());
		indexColumn.setWidth("25px");

		Paragraph headerLabel = new Paragraph();
		headerLabel.add("N°");

		HorizontalLayout headerComponent = new HorizontalLayout();
		headerComponent.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		headerComponent.setAlignItems(Alignment.CENTER);
		headerComponent.add(headerLabel);
		headerComponent.setWidthFull();

		indexColumn.setHeader(headerComponent);
	}

	protected void initGridHeaders() {
		Map<String, VaadinIcon> iconsTranslations = this
				.propertyNamesToColumnIconsTranslator();

		Set<Entry<String, String>> namesTranslations = this
				.propertyNamesToColumnHeadersTranslator().entrySet();

		for (Entry<String, String> entry : namesTranslations) {
			String propertyName = entry.getKey();
			String columnHeader = entry.getValue();
			VaadinIcon columnIcon = iconsTranslations.get(propertyName);

			Column<T> column = this.getColumnByKey(propertyName);
			column.setSortable(false);

			Paragraph headerLabel = new Paragraph();
			headerLabel.add(columnHeader);

			Icon icon = columnIcon.create();
			icon.addClassName("icon-24");

			HorizontalLayout headerComponent = new HorizontalLayout();
			headerComponent
					.setDefaultVerticalComponentAlignment(Alignment.CENTER);
			headerComponent.setAlignItems(Alignment.CENTER);
			headerComponent.add(icon, headerLabel);
			headerComponent.setWidthFull();

			column.setHeader(headerComponent);
		}
	}

	@Override
	protected CallbackDataProvider<T, Void> constructProvider() {
		return DataProvider.fromCallbacks(
				query -> {
					Pageable pageable = PageRequest.of(query.getOffset(),
							query.getLimit());

					this.indexRenderer.setIndex(0);

					return this.entityFetcher.apply(pageable).stream();
				}, query -> this.counter.get());
	}

	protected abstract Map<String, String> propertyNamesToColumnHeadersTranslator();

	protected abstract Map<String, VaadinIcon> propertyNamesToColumnIconsTranslator();

	protected abstract Map<String, Renderer<T>> propertyNamesToSpecificRenderers();

	protected abstract String[] getSimplePropertyNamesToShow();

	protected abstract String[] getPropertiesKeysToCenter();

	protected abstract Collection<MSTableGridRenderer<T>> getAdditionalColumns();

	protected void onBeforeInitColumnHeaders() {
	}
}
