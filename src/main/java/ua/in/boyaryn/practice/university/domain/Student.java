package ua.in.boyaryn.practice.university.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

@Entity
@PrimaryKeyJoinColumn (name="PERSON_ID")
public class Student extends Person implements Serializable {
  private static final long serialVersionUID = -3637473456207740684L;
  @ManyToMany(mappedBy="students")
  private Set<Seminar> seminars;

  public Set<Seminar> getSeminars() {
	return seminars;
  }

  public void setSeminars(Set<Seminar> seminars) {
	this.seminars = seminars;
  }
}
