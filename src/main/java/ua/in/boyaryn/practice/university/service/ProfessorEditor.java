package ua.in.boyaryn.practice.university.service;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;

import ua.in.boyaryn.practice.university.domain.Professor;
import ua.in.boyaryn.practice.university.persistence.UniversityDao;

public class ProfessorEditor extends PropertyEditorSupport {
  UniversityDao universityDao;

  @Autowired
  public ProfessorEditor(UniversityDao universityDao) {
    this.universityDao = universityDao;
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException {
    long id = Long.parseLong(text);
    Professor professor = universityDao.findProfessor(id);
      setValue(professor);
  }
  
  @Override
  public String getAsText() {
    Object value = getValue();
    String result = "0";
    if ((value != null) && (value instanceof Professor)) {
      Professor professor = ((Professor) value);
      Long id = professor.getPersonId();
      result = id != null ? id.toString() : "0";
    }
    return result;
  }
}
