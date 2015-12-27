package sample.dt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class DtState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue private Long id;
	
	private String tableId;
	
	@Lob
	private byte[] tableSettings;

	public Long getId() {
		return id;
	}

	public String getTableId() {
		return tableId;
	}

	public byte[] getTableSettings() {
		return tableSettings;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public void setTableSettings(byte[] tableSettings) {
		this.tableSettings = tableSettings;
	}
	
	
}
