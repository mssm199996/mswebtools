package com.mvc.Crud;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.Grid.MSGrid;
import com.vaadin.flow.component.AbstractSinglePropertyField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class MSCrud<T, R extends JpaRepository<T, ?>, G extends MSGrid<T, R>>
		extends VerticalLayout {

	private static final long serialVersionUID = -5523025201513447123L;

	protected HorizontalLayout filtersContainer;
	protected VerticalLayout rightSideToolBar;

	protected G grid;

	public MSCrud() {
		super();

		this.setHeightFull();
	}

	@PostConstruct
	protected void initComponents() {
		this.initGrid();
		this.initFiltersContainer();
		this.addComponentsToInterface();
	}

	private void addComponentsToInterface() {
		this.add(this.filtersContainer);
		this.add(this.grid);
	}

	private void initGrid() {
		this.grid = this.constructGrid();
		this.grid.setEntityFetcher(pageable -> {
			return this.fetchEntitiesFromDatabase(pageable);
		});

		this.grid.setCounter(() -> {
			return this.countExpectedFetch();
		});
	}

	protected void initFiltersContainer() {
		Map<AbstractSinglePropertyField<?, ?>, String> filtersAsMap = this
				.filters();
		Set<AbstractSinglePropertyField<?, ?>> filters = filtersAsMap.keySet();

		this.filtersContainer = new HorizontalLayout();
		this.filtersContainer.setWidthFull();

		for (AbstractSinglePropertyField<?, ?> filter : filters) {
			VerticalLayout filterContainer = new VerticalLayout();
			filterContainer.setWidthFull();
			filterContainer
					.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

			filter.getElement().setProperty("label", filtersAsMap.get(filter));
			filter.addValueChangeListener(event -> {
				this.grid.getDataProvider().refreshAll();
				this.grid.getSelectionModel().deselectAll();
			});

			if (filter instanceof HasSize) {
				((HasSize) (filter)).setWidthFull();
			}

			filterContainer.add(filter);

			this.filtersContainer.add(filterContainer);
		}

		this.rightSideToolBar = new VerticalLayout();

		Set<Component> rightSideToolBarComponents = getRightSideToolBarComponents();

		for (Component component : rightSideToolBarComponents)
			this.rightSideToolBar.add(component);

		this.filtersContainer.add(this.rightSideToolBar);
		this.filtersContainer
				.setAlignSelf(Alignment.END, this.rightSideToolBar);
	}

	protected abstract G constructGrid();

	protected abstract Map<AbstractSinglePropertyField<?, ?>, String> filters();

	protected abstract List<T> fetchEntitiesFromDatabase(Pageable pageable);

	protected abstract Integer countExpectedFetch();

	protected Set<Component> getRightSideToolBarComponents() {
		return new LinkedHashSet<>();
	}
}
