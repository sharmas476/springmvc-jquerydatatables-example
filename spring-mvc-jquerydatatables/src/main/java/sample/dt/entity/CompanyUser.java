package sample.dt.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="COMPANY_USER")
public class CompanyUser implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String uname;

	private String urole;

	private String city;

	private Integer age;

	private Date hdate;

	private Integer salary;

	public String getUname() {
		return uname;
	}

	public String getUrole() {
		return urole;
	}

	public String getCity() {
		return city;
	}

	public Integer getAge() {
		return age;
	}

	public Date getHdate() {
		return hdate;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public void setUrole(String urole) {
		this.urole = urole;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public void setHdate(Date hdate) {
		this.hdate = hdate;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
