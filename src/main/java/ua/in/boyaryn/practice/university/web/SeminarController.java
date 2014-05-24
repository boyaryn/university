package ua.in.boyaryn.practice.university.web;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import ua.in.boyaryn.practice.university.domain.Seminar;
import ua.in.boyaryn.practice.university.persistence.UniversityDao;

@Controller
@RequestMapping("/seminar")
@SessionAttributes("seminar")
public class SeminarController {
  private static Validator validator;
  UniversityDao universityDao;

  @Autowired
  public SeminarController(UniversityDao universityDao) {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
    this.universityDao = universityDao;
  }

  @RequestMapping(value = "/create", method=RequestMethod.GET)
  public String createSeminar(Model model) {
    model.addAttribute("title", "Create Seminar");
    model.addAttribute("seminar", new Seminar());
    model.addAttribute("allProfessors", universityDao.listProfessors());
    model.addAttribute("allStudents", universityDao.listStudents());
    model.addAttribute("submit_button", "Create");
    return "seminar/edit";
  }
    
  @RequestMapping(value = "/create", method=RequestMethod.POST)
  public String createSeminar(@ModelAttribute("seminar") Seminar seminar, BindingResult result,
  Model model, SessionStatus status) {
    Set<ConstraintViolation<Seminar>> violations = validator.validate(seminar);
    for (ConstraintViolation<Seminar> violation : violations) {
      String propertyPath = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      result.addError(new FieldError("member", propertyPath,
          "Invalid "+ propertyPath + " (" + message + ")"));
    }
    if (result.hasErrors()) {
      model.addAttribute("title", "Create Seminar");
      model.addAttribute("allProfessors", universityDao.listProfessors());
      model.addAttribute("allStudents", universityDao.listStudents());
      model.addAttribute("submit_button", "Create");
      return "seminar/edit";
    } else {
      universityDao.saveSeminar(seminar);
      model.addAttribute("message", "Seminar has been created.");
      status.setComplete();
      return "message";
    }
  }
  
  @RequestMapping(value = "/edit/{id}", method=RequestMethod.GET)
  public String editSeminar(@PathVariable("id") String stringWithId, Model model) {
    try {  
      long id = Long.parseLong(stringWithId); 
      model.addAttribute("title", "Edit Seminar");
      Seminar seminar = universityDao.findSeminar((long)id);
      if (seminar != null) {
        model.addAttribute("seminar", seminar);
        model.addAttribute("allProfessors", universityDao.listProfessors());
        model.addAttribute("allStudents", universityDao.listStudents());
        model.addAttribute("submit_button", "Save");
        return "seminar/edit";
      } else {
        model.addAttribute("message", "Sorry, we haven't found this seminar.");
          return "message";
      }
    } catch(NumberFormatException nfe) {
      model.addAttribute("message", "Sorry, we haven't found this seminar.");
      return "message";
    }  
  }

  @RequestMapping(value = "/edit/{id}", method=RequestMethod.POST)
  public String editSeminar(@PathVariable("id") String stringWithId,
  @ModelAttribute("seminar") Seminar seminar, BindingResult result, Model model, SessionStatus status) {
    try {  
      long id = Long.parseLong(stringWithId); 
      Set<ConstraintViolation<Seminar>> violations = validator.validate(seminar);
      for (ConstraintViolation<Seminar> violation : violations) {
        String propertyPath = violation.getPropertyPath().toString();
        String message = violation.getMessage();
        result.addError(new FieldError("member", propertyPath,
            "Invalid "+ propertyPath + " (" + message + ")"));
      }
      if (seminar.getActive() && (seminar.getStudents() == null)) {
    	  String propertyPath = "active";
          String message = "At least 1 student is required for setting the seminar as active";
          result.addError(new FieldError("member", propertyPath,
              "Invalid "+ propertyPath + " (" + message + ")"));
      }
      if (result.hasErrors()) {
        model.addAttribute("title", "Edit Seminar");
        model.addAttribute("allProfessors", universityDao.listProfessors());
        model.addAttribute("allStudents", universityDao.listStudents());
        model.addAttribute("submit_button", "Save");
        return "seminar/edit";
      } else {
        universityDao.saveSeminar(seminar);
        model.addAttribute("message", "Seminar has been updated.");
        status.setComplete();
        return "message";
      }
    } catch(NumberFormatException nfe) { 
      model.addAttribute("message", "Sorry, we haven't found this seminar.");
        return "message"; //when
    }  
  }

  @RequestMapping(value = "/list", method=RequestMethod.GET)
  public String listSeminars(Model model) {
    model.addAttribute("title", "Seminars");
    model.addAttribute("submit_button", "Filter");
    Seminar seminar = new Seminar();
    model.addAttribute("seminar", seminar);
    List<Seminar> seminars = universityDao.listSeminars();
    model.addAttribute("seminars", seminars);
    return "seminar/list";
  }
  
  @RequestMapping(value = "/delete/{id}")
  public String deleteSeminar(@PathVariable("id") String stringWithId, Model model, SessionStatus status) {
    try {  
      long id = Long.parseLong(stringWithId);
      if (universityDao.deleteSeminar(id)) {
        model.addAttribute("message", "Sorry, we haven't found this seminar.");
      } else {
        model.addAttribute("message", "Seminar has been deleted.");
        status.setComplete();
      }
      return "message";
    } catch(NumberFormatException nfe) {  
      model.addAttribute("message", "Sorry, we haven't found this seminar.");
      return "message";
    }  
  }
  
  @RequestMapping(value = "/list", method=RequestMethod.POST)
  public String filterSeminars(@ModelAttribute("seminar") Seminar seminar, Model model) {
    model.addAttribute("title", "Seminars");
    model.addAttribute("seminar", seminar);
    model.addAttribute("submit_button", "Filter");
    List<Seminar> seminars = universityDao.listSeminars(seminar.getName());
    model.addAttribute("seminars", seminars);
    return "seminar/list";
  }
}