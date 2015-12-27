package sample.dt.webcomponents.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DtStateParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long time;
	private int start;
	private int length;
	private List<DtStateOrder> orders = new ArrayList<>();
	private DtStateSearch search;
	private List<DtStateColumn> searches = new ArrayList<>();

	private List<String> colReorder = new ArrayList<>();

	public long getTime() {
		return time;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public DtStateSearch getSearch() {
		return search;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setSearch(DtStateSearch search) {
		this.search = search;
	}

	public List<DtStateColumn> getSearches() {
		return searches;
	}

	public void setSearches(List<DtStateColumn> searches) {
		this.searches = searches;
	}

	public List<DtStateOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<DtStateOrder> orders) {
		this.orders = orders;
	}

	public List<String> getColReorder() {
		return colReorder;
	}

	public void setColReorder(List<String> list) {
		this.colReorder = list;
	}

	public static class DtStateColumn implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DtStateColumn() {
		}

		public DtStateColumn(boolean visible, DtStateSearch search) {
			this.visible = visible;
			this.search = search;
		}

		private boolean visible;
		private DtStateSearch search;

		public boolean isVisible() {
			return visible;
		}

		public DtStateSearch getSearch() {
			return search;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}

		public void setSearch(DtStateSearch search) {
			this.search = search;
		}
	}

	public static class DtStateOrder implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DtStateOrder() {
		}

		public DtStateOrder(int index, String direction) {
			this.index = index;
			this.direction = direction;
		}

		public DtStateOrder(String index, String direction) {
			if (index != null) {
				this.index = Integer.parseInt(index);
			}
			this.direction = direction;
		}

		private int index;
		private String direction;

		public int getIndex() {
			return index;
		}

		public String getDirection() {
			return direction;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void setDirection(String direction) {
			this.direction = direction;
		}

	}

	public static class DtStateSearch implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String search;
		private boolean smart;
		private boolean caseInsensitive;
		private boolean regex;

		public DtStateSearch() {
		}

		public DtStateSearch(String search, boolean smart, boolean regex, boolean caseInsensitive) {
			this.search = search;
			this.smart = smart;
			this.caseInsensitive = caseInsensitive;
			this.regex = regex;
		}

		public boolean isRegex() {
			return regex;
		}

		public void setRegex(boolean regex) {
			this.regex = regex;
		}

		public String getSearch() {
			return search;
		}

		public boolean isSmart() {
			return smart;
		}

		public boolean isCaseInsensitive() {
			return caseInsensitive;
		}

		public void setSearch(String search) {
			this.search = search;
		}

		public void setSmart(boolean smart) {
			this.smart = smart;
		}

		public void setCaseInsensitive(boolean caseInsensitive) {
			this.caseInsensitive = caseInsensitive;
		}
	}

}
