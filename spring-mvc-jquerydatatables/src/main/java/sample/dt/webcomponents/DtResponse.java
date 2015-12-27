package sample.dt.webcomponents;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

public class DtResponse<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private T[] data;

	private long recordsTotal;

	private long recordsFiltered;
	
	private int draw;

	public DtResponse() {
	}

	public T[] getData() {
		return data;
	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setData(T[] data) {
		this.data = data;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public static <T> DtResponse<T> of(List<T> total) {
		DtResponse<T> that = new DtResponse<T>();

		@SuppressWarnings("unchecked")
		T[] data = (T[]) total.toArray();
		that.data = data;

		that.recordsFiltered = total.size();
		that.recordsTotal = total.size();

		return that;
	}

	public static <T> DtResponse<T> of(Page<T> page, DtPageable pageable) {

		DtResponse<T> that = new DtResponse<T>();

		@SuppressWarnings("unchecked")
		T[] data = (T[]) page.getContent().toArray();
		that.data = data;

		that.recordsFiltered = page.getTotalElements();//TODO what mean filtered???
		that.recordsTotal = page.getTotalElements();
		that.draw = pageable.getDraw();

		return that;
	}

	public int getDraw() {
		return draw;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}
	
	
	

}
