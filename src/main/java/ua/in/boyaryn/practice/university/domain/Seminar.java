package ua.in.boyaryn.practice.university.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table (name="SEMINARS")
public class Seminar implements Serializable {
  private static final long serialVersionUID = -5119119376751110049L;
	@Id
	@GeneratedValue (strategy=GenerationType.AUTO)
	@Column (name="ID")
	private Long id;
	@Size(min = 2, max = 30, message = "Size must be between 2 and 30.")
	@Pattern(regexp="[a-zA-Z]+", message = "String must consist of letters.")
	@Column (name="NAME")
	private String name;
	@Min(1)
	@Max(9999)
	@Column (name="COST")
	private Long cost;
	@Column (name="ACTIVE")
	private Boolean active;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn (name="PERSON_ID")
	private Professor professor;
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name="SEMINAR_STUDENT", joinColumns=@JoinColumn(name="SEMINAR_ID"), inverseJoinColumns=@JoinColumn(name="STUDENT_ID"))
	private Set<Student> students;

	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public Long getCost() {
		return cost;
	}
	public Boolean getActive() {
		return active;
	}
	public Professor getProfessor() {
		return professor;
	}
	public Set<Student> getStudents() {
		return students;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCost(Long cost) {
		this.cost = cost;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	  public void addS(Student student) {
		  students.add(student);
	  }
	public String toString() {
		return getId().toString();
	}
}