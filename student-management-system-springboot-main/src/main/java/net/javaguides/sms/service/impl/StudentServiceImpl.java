package net.javaguides.sms.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.javaguides.sms.entity.Student;
import net.javaguides.sms.repository.StudentRepository;
import net.javaguides.sms.service.StudentService;

/**
 * Implementation of StudentService interface.
 * Provides business logic for student management operations.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	private final StudentRepository studentRepository;
	
	public StudentServiceImpl(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Student> getAllStudents() {
		logger.debug("Fetching all students from database");
		List<Student> students = studentRepository.findAll();
		logger.info("Retrieved {} students", students.size());
		return students;
	}

	@Override
	public Student saveStudent(Student student) {
		logger.debug("Saving new student: {}", student.getEmail());
		Student savedStudent = studentRepository.save(student);
		logger.info("Successfully saved student with id: {}", savedStudent.getId());
		return savedStudent;
	}

	@Override
	@Transactional(readOnly = true)
	public Student getStudentById(Long id) {
		logger.debug("Fetching student with id: {}", id);
		return studentRepository.findById(id)
				.orElseThrow(() -> {
					logger.error("Student not found with id: {}", id);
					return new RuntimeException("Student not found with id: " + id);
				});
	}

	@Override
	public Student updateStudent(Student student) {
		logger.debug("Updating student with id: {}", student.getId());
		Student updatedStudent = studentRepository.save(student);
		logger.info("Successfully updated student with id: {}", updatedStudent.getId());
		return updatedStudent;
	}

	@Override
	public void deleteStudentById(Long id) {
		logger.debug("Deleting student with id: {}", id);
		if (!studentRepository.existsById(id)) {
			logger.error("Cannot delete - Student not found with id: {}", id);
			throw new RuntimeException("Student not found with id: " + id);
		}
		studentRepository.deleteById(id);
		logger.info("Successfully deleted student with id: {}", id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Student> getActiveStudents() {
		logger.debug("Fetching all active students");
		List<Student> activeStudents = studentRepository.findByStatus(true);
		logger.info("Retrieved {} active students", activeStudents.size());
		return activeStudents;
	}
}
