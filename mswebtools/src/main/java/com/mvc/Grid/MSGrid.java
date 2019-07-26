package com.mvc.Grid;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

public abstract class MSGrid<T, R extends JpaRepository<T, ?>> extends Grid<T> {

	private static final long serialVersionUID = -8396187303852328617L;

	protected R repository;
	protected Function<Pageable, List<T>> entityFetcher;
	protected Supplier<Integer> counter;

	public MSGrid(Class<T> entityClass, R repository) {
		super(entityClass);

		this.initGridColumns();
		this.setDataProvider(this.constructProvider());
		this.setRepository(repository);
	}

	public MSGrid(Class<T> entityClass) {
		this(entityClass, null);
	}

	protected CallbackDataProvider<T, Void> constructProvider() {
		return DataProvider.fromCallbacks(
				query -> {
					Pageable pageable = PageRequest.of(query.getOffset(),
							query.getLimit());

					return this.entityFetcher.apply(pageable).stream();
				}, query -> this.counter.get());
	}

	public Function<Pageable, List<T>> getEntityFetcher() {
		return entityFetcher;
	}

	public void setEntityFetcher(Function<Pageable, List<T>> entityFetcher) {
		this.entityFetcher = entityFetcher;
	}

	public Supplier<Integer> getCounter() {
		return counter;
	}

	public void setCounter(Supplier<Integer> counter) {
		this.counter = counter;
	}

	public R getRepository() {
		return this.repository;
	}

	public void setRepository(R repository) {
		this.repository = repository;
	}

	protected abstract void initGridColumns();
}