package ua.in.boyaryn.practice.university.persistence;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.in.boyaryn.practice.university.domain.Professor;
import ua.in.boyaryn.practice.university.domain.Seminar;
import ua.in.boyaryn.practice.university.domain.Student;

@Repository
public class UniversityDao {
  @Autowired @Qualifier("entityManagerFactory")
  EntityManagerFactory entityManagerFactory;

  public boolean saveProfessor(Professor professor) {
	boolean result = false;
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction tran = manager.getTransaction();
	tran.begin();

	if (professor.getPersonId() == null) {
		manager.persist(professor);
	} else {
		manager.merge(professor);
		Query query = manager.createQuery("UPDATE Seminar s SET s.professor = null WHERE s.professor.personId = :id");
		query.setParameter("id", professor.getPersonId());
		query.executeUpdate();
	}

	if (professor.getSeminars() != null) {
	  for (Seminar seminar: professor.getSeminars()) {
		if (seminar != null) {
		  seminar.setProfessor(professor);
		  manager.merge(seminar);
		} else {
		  result = true;
		}
	  }
	}

	tran.commit();
	manager.close();
	return result;
  }

  public Professor findProfessor(long id) {
    Professor professor = null;
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("SELECT DISTINCT p FROM Professor p LEFT JOIN FETCH p.seminars WHERE p.personId = :id");
    query.setParameter("id", id);
    List<Professor> results = query.getResultList();
    if(!results.isEmpty()){
    	professor = results.get(0);
    }

    tran.commit();
    manager.close();
    return professor;
  }

  public List<Professor> listProfessors() {
	EntityManager manager = entityManagerFactory.createEntityManager();
	EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("select p from Professor p");
    List<Professor> list = query.getResultList();

    tran.commit();
    manager.close();
    return list;
  }

  public List<Professor> listProfessors(String name, String surname) {
	EntityManager manager = entityManagerFactory.createEntityManager();
	EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("select p from Professor p where p.name like :name and p.surname like :surname");
	query.setParameter("name", "%" + name + "%");
	query.setParameter("surname", "%" + surname + "%");
	List<Professor> list = query.getResultList();

	tran.commit();
	manager.close();
	return list;
  }

  public boolean deleteProfessor(long id) {
    boolean found;
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction tran = manager.getTransaction();
	tran.begin();
	   
    Professor professor = manager.find(Professor.class, new Long(id));
    found = professor != null;
    if (found) {
      Query query = manager.createQuery("UPDATE Seminar s SET s.professor = null WHERE s.professor.personId = :id");
      query.setParameter("id", professor.getPersonId());
      query.executeUpdate();
    	
      manager.remove(professor);
    };

    tran.commit();
    manager.close();
    return !found;
  }

  //Implementation-dependent code
  public boolean saveStudent(Student student) {
	boolean result = false;
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction tran = manager.getTransaction();
	tran.begin();

	if (student.getPersonId() == null) {
		manager.persist(student);
	} else {
		manager.merge(student);
		Query query = manager.createNativeQuery("DELETE FROM SEMINAR_STUDENT WHERE STUDENT_ID = :id");
		query.setParameter("id", student.getPersonId());
		query.executeUpdate();
	}

	if (student.getSeminars() != null) {
	  for (Seminar seminar: student.getSeminars()) {
		if (seminar != null) {
			Query query = manager.createNativeQuery("INSERT INTO SEMINAR_STUDENT"
					+ " (SEMINAR_ID, STUDENT_ID) VALUES (:seminarId, :studentId)");
			query.setParameter("seminarId", seminar.getId());
			query.setParameter("studentId", student.getPersonId());
			query.executeUpdate();
//			seminar.getStudents().add(student);
//			manager.merge(seminar);
		} else {
		  result = true;
		}
	  }
	}

	tran.commit();
	manager.close();
	return result;
  }

  public Student findStudent(long id) {
	Student student = null;
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.seminars WHERE s.personId = :id");
    query.setParameter("id", id);
    List<Student> results = query.getResultList();
    if(!results.isEmpty()){
    	student = results.get(0);
    }

    tran.commit();
    manager.close();
    return student;
  }

  public long findStudentFees(long id) {
	long fees = 0;
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.seminars WHERE s.personId = :id");
    query.setParameter("id", id);
    List<Student> results = query.getResultList();
    if(!results.isEmpty()){
    	Student student = results.get(0);
    	Set<Seminar> seminars = student.getSeminars();
    	if (seminars != null) {
    		int count = 0;
    		for (Seminar seminar: seminars) {
    			count += 1;
    			fees += seminar.getCost();
    		}
    		if (count >= 5) {
    			fees *= 0.9;
    		}
    	}
    }

    tran.commit();
    manager.close();
    return fees;
  }

  public List<Student> listStudents() {
	EntityManager manager = entityManagerFactory.createEntityManager();
	EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("SELECT s FROM Student s");
    List<Student> list = query.getResultList();

    tran.commit();
    manager.close();
    return list;
  }

  public List<Student> listStudents(String name, String surname) {
	EntityManager manager = entityManagerFactory.createEntityManager();
	EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("select s from Student s where s.name like :name and s.surname like :surname");
	query.setParameter("name", "%" + name + "%");
	query.setParameter("surname", "%" + surname + "%");
	List<Student> list = query.getResultList();

	tran.commit();
	manager.close();
	return list;
  }

  //Implementation-dependent code
  public boolean deleteStudent(long id) {
    boolean found;
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction tran = manager.getTransaction();
	tran.begin();
	   
	Student student = manager.find(Student.class, new Long(id));
    found = student != null;
    if (found) {
    	Query query = manager.createNativeQuery("DELETE FROM SEMINAR_STUDENT WHERE STUDENT_ID = :id");
    	query.setParameter("id", student.getPersonId());
    	query.executeUpdate();

    	manager.remove(student);
    };

    tran.commit();
    manager.close();
    return !found;
  }

  public void saveSeminar(Seminar seminar) {
	EntityManager manager = entityManagerFactory.createEntityManager();
	EntityTransaction tran = manager.getTransaction();
	tran.begin();

	manager.merge(seminar); // a newly assigned professor or seminar might be deleted after assignment - okay?

	tran.commit();
	manager.close();
  }

  public Seminar findSeminar(long id) {
	EntityManager manager = entityManagerFactory.createEntityManager();
	EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("SELECT DISTINCT s FROM Seminar s LEFT JOIN FETCH s.professor LEFT JOIN FETCH s.students WHERE s.id = :id");
	query.setParameter("id", id);
	List<Seminar> results = query.getResultList();
	Seminar seminar = null;
	if(!results.isEmpty()){
	  seminar = results.get(0);
	}

	tran.commit();
	manager.close();
	return seminar;
  }

  public List<Seminar> listSeminars() {
	EntityManager manager = entityManagerFactory.createEntityManager();
	EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("select s from Seminar s");
    List<Seminar> list = query.getResultList();

    tran.commit();
    manager.close();
    return list;
  }

  public List<Seminar> listSeminars(String name) {
	EntityManager manager = entityManagerFactory.createEntityManager();
	EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Query query = manager.createQuery("select s from Seminar s where s.name like :name");
    query.setParameter("name", "%" + name + "%");
    List<Seminar> list = query.getResultList();

    tran.commit();
    manager.close();
    return list;
  }

  public boolean deleteSeminar(long id) {
    boolean found;
    EntityManager manager = entityManagerFactory.createEntityManager();
    EntityTransaction tran = manager.getTransaction();
	tran.begin();

    Seminar seminar = manager.find(Seminar.class, new Long(id));
    found = seminar != null;
    if (found) {
       manager.remove(seminar);
    };

    tran.commit();
    manager.close();
    return !found;
  }
}