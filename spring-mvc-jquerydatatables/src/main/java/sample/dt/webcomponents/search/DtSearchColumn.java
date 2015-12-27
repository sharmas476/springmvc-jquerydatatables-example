package sample.dt.webcomponents.search;

import java.io.Serializable;

import org.springframework.data.domain.Sort.Direction;

public class DtSearchColumn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int index;
	private boolean orderable = true;
	private boolean searchable = false;
	private String data;
	private String name;
	private DtColSearchCondition condition;

	public DtSearchColumn() {
	}

	public DtSearchColumn(int index, String name, DtColSearchCondition condition) {
		this.index = index;
		this.data = name;
		this.name = name;
		this.condition = condition;
	}

	public DtSearchColumn(int index, String name, boolean orderable, boolean searchable, DtColSearchCondition condition) {
		this(index, name, condition);
		this.orderable = orderable;
		this.searchable = searchable;
	}

	public int getIndex() {
		return index;
	}

	public boolean isOrderable() {
		return orderable;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public String getData() {
		return data;
	}

	public String getName() {
		return name;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DtColSearchCondition getCondition() {
		return condition;
	}

	public void setCondition(DtColSearchCondition condition) {
		this.condition = condition;
	}
	
	
	public static class DtColSearchCondition implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private String value;
		private boolean regex = false;

		public DtColSearchCondition() {
		}

		public DtColSearchCondition(String value) {
			this.value = value;
		}

		public DtColSearchCondition(String value, boolean regex) {
			this(value);
			this.regex = regex;
		}

		public String getValue() {
			return value;
		}

		public boolean isRegex() {
			return regex;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void setRegex(boolean regex) {
			this.regex = regex;
		}

	}

	public static class DtSearchOrder implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int index;

		private Direction direction;

		private DtSearchColumn column;

		public DtSearchOrder() {
		}

		public DtSearchOrder(int index, Direction direction, DtSearchColumn column) {
			this.index = index;
			this.direction = direction;
			this.column = column;
		}

		public int getIndex() {
			return index;
		}

		public Direction getDirection() {
			return direction;
		}

		public DtSearchColumn getColumn() {
			return column;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void setDirection(Direction direction) {
			this.direction = direction;
		}

		public void setColumn(DtSearchColumn column) {
			this.column = column;
		}

	}


}
