package ua.in.boyaryn.practice.university.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import ua.in.boyaryn.practice.university.domain.Professor;
import ua.in.boyaryn.practice.university.persistence.UniversityDao;

public class UniversityBindingInitializer implements WebBindingInitializer {
  UniversityDao universityDao;
  
  @Autowired
  public UniversityBindingInitializer(UniversityDao universityDao) {
    this.universityDao = universityDao;
  }

  public void initBinder(WebDataBinder binder, WebRequest request) {
    binder.registerCustomEditor(Professor.class, new ProfessorEditor(universityDao));
    binder.registerCustomEditor(Set.class, "students", new CustomCollectionEditor(Set.class) {
      protected Object convertElement(Object element) {
          long id = 0;
          if (element instanceof Long) {
            id = (Long)element;
          } else if (element instanceof String) {
            id = Integer.parseInt((String) element);
          }
          return id != 0 ? universityDao.findStudent(id) : null;
      }
    });
    binder.registerCustomEditor(Set.class, "seminars", new CustomCollectionEditor(Set.class) {
      protected Object convertElement(Object element) {
          long id = 0;
          if (element instanceof Long) {
            id = (Long)element;
          } else if (element instanceof String) {
            id = Integer.parseInt((String) element);
          }
          return id != 0 ? universityDao.findSeminar(id) : null;
      }
    });
  }
}
