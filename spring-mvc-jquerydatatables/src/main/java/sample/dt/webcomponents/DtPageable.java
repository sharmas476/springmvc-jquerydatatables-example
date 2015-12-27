package sample.dt.webcomponents;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import sample.dt.webcomponents.search.DtSearchColumn;

public class DtPageable implements Pageable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DtPageable() {
	}

	private int start;
	private int length;
	private List<DtSearchColumn> columns;
	private List<DtSearchColumn.DtSearchOrder> orders;
	private DtSearchColumn.DtColSearchCondition globalSearch;
	private int draw = 1;

	public DtPageable(int start, int length, int draw, List<DtSearchColumn> columns, List<DtSearchColumn.DtSearchOrder> orders, DtSearchColumn.DtColSearchCondition globalSearch) {
		this.start = start;
		this.length = length < 0 ? Integer.MAX_VALUE : length;
		this.draw = draw;
		this.columns = columns;
		this.orders = orders;
		this.globalSearch = globalSearch;
	}

	private Pageable pageable;

	private synchronized Pageable asPageable() {
		if (pageable == null) {
			pageable = new PageRequest(this.start / this.length, this.length,
					new Sort(this.orders.stream().map(o -> new Sort.Order(o.getDirection(), o.getColumn().getName())).collect(Collectors.toList())));
		}
		return pageable;
	}

	@Override
	public Pageable first() {
		return asPageable().first();
	}

	@Override
	public int getOffset() {
		return asPageable().getOffset();
	}

	@Override
	public int getPageNumber() {
		return asPageable().getPageNumber();
	}

	@Override
	public int getPageSize() {
		return asPageable().getPageSize();
	}

	@Override
	public Sort getSort() {
		return asPageable().getSort();
	}

	@Override
	public boolean hasPrevious() {
		return asPageable().hasPrevious();
	}

	@Override
	public Pageable next() {
		return asPageable().next();
	}

	@Override
	public Pageable previousOrFirst() {
		return asPageable().previousOrFirst();
	}

	public List<DtSearchColumn> getColumns() {
		return columns;
	}

	public List<DtSearchColumn.DtSearchOrder> getOrders() {
		return orders;
	}

	public DtSearchColumn.DtColSearchCondition getGlobalSearch() {
		return globalSearch;
	}

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

}
