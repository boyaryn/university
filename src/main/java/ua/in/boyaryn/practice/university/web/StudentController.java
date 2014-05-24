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
import ua.in.boyaryn.practice.university.domain.Student;
import ua.in.boyaryn.practice.university.persistence.UniversityDao;

@Controller
@RequestMapping("/student")
@SessionAttributes("student")
public class StudentController {
	private static Validator validator;
	UniversityDao universityDao;

	@Autowired
	public StudentController(UniversityDao universityDao) {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		this.universityDao = universityDao;
	}

  @RequestMapping(value = "/create", method=RequestMethod.GET)
  public String createStudent(Model model) {
	  model.addAttribute("title", "Create Student");
	  model.addAttribute("student", new Student());
	  List<Seminar> allSeminars = universityDao.listSeminars();
	  model.addAttribute("allSeminars", allSeminars);
	  model.addAttribute("submit_button", "Create");
	  return "student/edit";
  }

  @RequestMapping(value = "/create", method=RequestMethod.POST)
  public String createStudent(@ModelAttribute("student") Student student, BindingResult result,
	Model model, SessionStatus status) {
	  Set<ConstraintViolation<Student>> violations = validator.validate(student);
	  for (ConstraintViolation<Student> violation : violations) {
		  String propertyPath = violation.getPropertyPath().toString();
		  String message = violation.getMessage();
		  result.addError(new FieldError("member", propertyPath, 
				  "Invalid "+ propertyPath + " (" + message + ")"));
	  }
	  if (result.hasErrors()) {
		  model.addAttribute("title", "Create Student");
		  List<Seminar> allSeminars = universityDao.listSeminars();
		  model.addAttribute("allSeminars", allSeminars);
		  model.addAttribute("submit_button", "Create");
		  return "student/edit";
	  } else {
		  if (!universityDao.saveStudent(student)) {
			  model.addAttribute("message", "Student has been updated.");
		  } else {
			  model.addAttribute("message", "Someone has deleted one of the seminars you assigned.");
		  }
		  status.setComplete();
		  return "message";
	  }
  }

  @RequestMapping(value = "/edit/{id}", method=RequestMethod.GET)
  public String editStudent(@PathVariable("id") String stringWithId, Model model) {
	  try {
		  long id = Long.parseLong(stringWithId);
		  model.addAttribute("title", "Edit Student");
		  Student student = universityDao.findStudent((long)id);
		  if (student != null) {
			  model.addAttribute("student", student);
			  model.addAttribute("fees", universityDao.findStudentFees(id));
			  List<Seminar> allSeminars = universityDao.listSeminars();
			  model.addAttribute("allSeminars", allSeminars);
			  model.addAttribute("submit_button", "Save");
			  return "student/edit";
		  } else {
			  model.addAttribute("message", "Sorry, we haven't found this student.");
			  return "message";
		  }
	  } catch(NumberFormatException nfe) {
		  model.addAttribute("message", "Sorry, we haven't found this student.");
		  return "message";
	  }  
  }

  @RequestMapping(value = "/edit/{id}", method=RequestMethod.POST)
  public String editStudent(@PathVariable("id") String stringWithId,
	@ModelAttribute("student") Student student, BindingResult result, Model model, SessionStatus status) {
	  try {  
		  long id = Long.parseLong(stringWithId); 
		  Set<ConstraintViolation<Student>> violations = validator.validate(student);
		  for (ConstraintViolation<Student> violation : violations) {
			  String propertyPath = violation.getPropertyPath().toString();
			  String message = violation.getMessage();
			  result.addError(new FieldError("member", propertyPath,
					  "Invalid "+ propertyPath + " (" + message + ")"));
		  }
		  if (result.hasErrors()) {
			  model.addAttribute("title", "Edit Student");
			  List<Seminar> allSeminars = universityDao.listSeminars();
			  model.addAttribute("allSeminars", allSeminars);
			  model.addAttribute("submit_button", "Save");
			  return "student/edit";
		  } else {
			  if (!universityDao.saveStudent(student)) {
				  model.addAttribute("message", "Student has been updated.");
			  } else {
				  model.addAttribute("message", "Someone has deleted one of the seminars you assigned.");
			  }
			  status.setComplete();
			  return "message";
		  }
	  } catch(NumberFormatException nfe) { 
		  model.addAttribute("message", "Sorry, we haven't found this student.");
		  return "message"; //when
	  }  
  }

  @RequestMapping(value = "/list", method=RequestMethod.GET)
  public String listStudents(Model model) {
	  model.addAttribute("title", "Students");
	  model.addAttribute("person_type", "student");
	  model.addAttribute("submit_button", "Filter");
	  Student student = new Student();
	  model.addAttribute("student", student);
	  List<Student> students = universityDao.listStudents();
	  model.addAttribute("people", students);
	  return "student/list";
  }

  @RequestMapping(value = "/delete/{id}")
  public String deleteStudent(@PathVariable("id") String stringWithId, Model model, SessionStatus status) {
	  try {  
		  long id = Long.parseLong(stringWithId);
		  if (universityDao.deleteStudent(id)) {
			  model.addAttribute("message", "Sorry, we haven't found this student.");
		  } else {
			  model.addAttribute("message", "Student has been deleted.");
			  status.setComplete();
		  }
		  return "message";
	  } catch(NumberFormatException nfe) {  
		  model.addAttribute("message", "Sorry, we haven't found this student.");
		  return "message";
	  }
  }

  @RequestMapping(value = "/list", method=RequestMethod.POST)
  public String filterStudents(@ModelAttribute("student") Student student, Model model) {
	  model.addAttribute("title", "Students");
	  model.addAttribute("person_type", "student");
	  model.addAttribute("submit_button", "Filter");
	  model.addAttribute("student", student);
	  List<Student> students = universityDao.listStudents(student.getName(), student.getSurname());
	  model.addAttribute("people", students);
	  return "student/list";
  }
}