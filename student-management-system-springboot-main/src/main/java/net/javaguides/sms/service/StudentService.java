package net.javaguides.sms.service;

import java.util.List;

import net.javaguides.sms.entity.Student;

/**
 * Service interface for Student management operations.
 * Defines the contract for student-related business logic.
 */
public interface StudentService {
	
	/**
	 * Retrieves all students from the database.
	 * 
	 * @return list of all students
	 */
	List<Student> getAllStudents();
	
	/**
	 * Saves a new student to the database.
	 * 
	 * @param student the student to save
	 * @return the saved student with generated ID
	 */
	Student saveStudent(Student student);
	
	/**
	 * Retrieves a student by their ID.
	 * 
	 * @param id the student ID
	 * @return the student if found
	 * @throws RuntimeException if student not found
	 */
	Student getStudentById(Long id);
	
	/**
	 * Updates an existing student.
	 * 
	 * @param student the student with updated information
	 * @return the updated student
	 */
	Student updateStudent(Student student);
	
	/**
	 * Deletes a student by their ID.
	 * 
	 * @param id the student ID to delete
	 * @throws RuntimeException if student not found
	 */
	void deleteStudentById(Long id);
	
	/**
	 * Retrieves all active students (status = true).
	 * 
	 * @return list of active students
	 */
	List<Student> getActiveStudents();
}
