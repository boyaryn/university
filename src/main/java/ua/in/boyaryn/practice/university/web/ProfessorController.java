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

import ua.in.boyaryn.practice.university.domain.Professor;
import ua.in.boyaryn.practice.university.domain.Seminar;
import ua.in.boyaryn.practice.university.persistence.UniversityDao;

@Controller
@RequestMapping("/professor")
@SessionAttributes("professor")
public class ProfessorController {
	private static Validator validator;
	UniversityDao universityDao;

	@Autowired
	public ProfessorController(UniversityDao universityDao) {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		this.universityDao = universityDao;
	}

  @RequestMapping(value = "/create", method=RequestMethod.GET)
  public String createProfessor(Model model) {
	  model.addAttribute("title", "Create Professor");
	  model.addAttribute("professor", new Professor());
	  List<Seminar> allSeminars = universityDao.listSeminars();
	  model.addAttribute("allSeminars", allSeminars);
	  model.addAttribute("submit_button", "Create");
	  return "professor/edit";
  }

  @RequestMapping(value = "/create", method=RequestMethod.POST)
  public String createProfessor(@ModelAttribute("professor") Professor professor, BindingResult result,
	Model model, SessionStatus status) {
	  Set<ConstraintViolation<Professor>> violations = validator.validate(professor);
	  for (ConstraintViolation<Professor> violation : violations) {
		  String propertyPath = violation.getPropertyPath().toString();
		  String message = violation.getMessage();
		  result.addError(new FieldError("member", propertyPath, 
				  "Invalid "+ propertyPath + " (" + message + ")"));
	  }
	  if (result.hasErrors()) {
		  model.addAttribute("title", "Create Professor");
		  List<Seminar> allSeminars = universityDao.listSeminars();
		  model.addAttribute("allSeminars", allSeminars);
		  model.addAttribute("submit_button", "Create");
		  return "professor/edit";
	  } else {
		  if (!universityDao.saveProfessor(professor)) {
			  model.addAttribute("message", "Professor has been updated.");
		  } else {
			  model.addAttribute("message", "Someone has deleted one of the seminars you assigned.");
		  }
		  status.setComplete();
		  return "message";
	  }
  }

  @RequestMapping(value = "/edit/{id}", method=RequestMethod.GET)
  public String editProfessor(@PathVariable("id") String stringWithId, Model model) {
	  try {
		  long id = Long.parseLong(stringWithId);
		  model.addAttribute("title", "Edit Professor");
		  Professor professor = universityDao.findProfessor((long)id);
		  if (professor != null) {
			  model.addAttribute("professor", professor);
			  List<Seminar> allSeminars = universityDao.listSeminars();
			  model.addAttribute("allSeminars", allSeminars);
			  model.addAttribute("submit_button", "Save");
			  return "professor/edit";
		  } else {
			  model.addAttribute("message", "Sorry, we haven't found this professor.");
			  return "message";
		  }
	  } catch(NumberFormatException nfe) {
		  model.addAttribute("message", "Sorry, we haven't found this professor.");
		  return "message";
	  }  
  }

  @RequestMapping(value = "/edit/{id}", method=RequestMethod.POST)
  public String editProfessor(@PathVariable("id") String stringWithId,
	@ModelAttribute("professor") Professor professor, BindingResult result, Model model, SessionStatus status) {
	  try {  
		  long id = Long.parseLong(stringWithId); 
		  Set<ConstraintViolation<Professor>> violations = validator.validate(professor);
		  for (ConstraintViolation<Professor> violation : violations) {
			  String propertyPath = violation.getPropertyPath().toString();
			  String message = violation.getMessage();
			  result.addError(new FieldError("member", propertyPath,
					  "Invalid "+ propertyPath + " (" + message + ")"));
		  }
		  if (result.hasErrors()) {
			  model.addAttribute("title", "Edit Professor");
			  List<Seminar> allSeminars = universityDao.listSeminars();
			  model.addAttribute("allSeminars", allSeminars);
			  model.addAttribute("submit_button", "Save");
			  return "professor/edit";
		  } else {
			  if (!universityDao.saveProfessor(professor)) {
				  model.addAttribute("message", "Professor has been updated.");
			  } else {
				  model.addAttribute("message", "Someone has deleted one of the seminars you assigned.");
			  }
			  status.setComplete();
			  return "message";
		  }
	  } catch(NumberFormatException nfe) { 
		  model.addAttribute("message", "Sorry, we haven't found this professor.");
		  return "message"; //when
	  }  
  }

  @RequestMapping(value = "/list", method=RequestMethod.GET)
  public String listProfessors(Model model) {
	  model.addAttribute("title", "Professors");
	  model.addAttribute("person_type", "professor");
	  model.addAttribute("submit_button", "Filter");
	  Professor professor = new Professor();
	  model.addAttribute("professor", professor);
	  List<Professor> professors = universityDao.listProfessors();
	  model.addAttribute("people", professors);
	  return "professor/list";
  }

  @RequestMapping(value = "/delete/{id}")
  public String deleteProfessor(@PathVariable("id") String stringWithId, Model model, SessionStatus status) {
	  try {  
		  long id = Long.parseLong(stringWithId);
		  if (universityDao.deleteProfessor(id)) {
			  model.addAttribute("message", "Sorry, we haven't found this professor.");
		  } else {
			  model.addAttribute("message", "Professor has been deleted.");
			  status.setComplete();
		  }
		  return "message";
	  } catch(NumberFormatException nfe) {  
		  model.addAttribute("message", "Sorry, we haven't found this professor.");
		  return "message";
	  }
  }

  @RequestMapping(value = "/list", method=RequestMethod.POST)
  public String filterProfessors(@ModelAttribute("professor") Professor professor, Model model) {
	  model.addAttribute("title", "Professors");
	  model.addAttribute("person_type", "professor");
	  model.addAttribute("submit_button", "Filter");
	  model.addAttribute("professor", professor);
	  List<Professor> professors = universityDao.listProfessors(professor.getName(), professor.getSurname());
	  model.addAttribute("people", professors);
	  return "professor/list";
  }
}