package ua.in.boyaryn.practice.university.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table (name="PEOPLE")
@DiscriminatorColumn(name="PERSON_TYPE")
@Inheritance (strategy=InheritanceType.SINGLE_TABLE)
public abstract class Person {
	@Id
	@GeneratedValue (strategy=GenerationType.AUTO)
	@Column (name="PERSON_ID")
	private Long personId;
	@Size(min = 2, max = 15, message = "Size must be between 2 and 15.")
	@Pattern(regexp="[a-zA-Z]+", message = "String must consist of letters.")
	@Column (name="NAME")
	private String name;
	@Size(min = 2, max = 15, message = "Size must be between 2 and 15.")
	@Pattern(regexp="[a-zA-Z]+", message = "String must consist of letters.")
	@Column (name="SURNAME") 
  private String surname;
	
    public Long getPersonId() {
        return personId;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
}