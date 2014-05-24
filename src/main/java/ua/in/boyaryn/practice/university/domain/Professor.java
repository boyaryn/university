package ua.in.boyaryn.practice.university.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Professor extends Person implements Serializable {
  private static final long serialVersionUID = 8510682776718466795L;
  @OneToMany(mappedBy="professor")
  private Set<Seminar> seminars;

  public Set<Seminar> getSeminars() {
	return seminars;
  }

  public void setSeminars(Set<Seminar> seminars) {
	this.seminars = seminars;
  } 
}
