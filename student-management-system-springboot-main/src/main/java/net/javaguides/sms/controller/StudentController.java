package net.javaguides.sms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import net.javaguides.sms.entity.Student;
import net.javaguides.sms.service.StudentService;

/**
 * Controller class for handling student-related HTTP requests.
 * Provides endpoints for CRUD operations on students.
 */
@Controller
@RequestMapping("/students")
public class StudentController {
	
	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
	
	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}
	
	/**
	 * Displays the list of all active students.
	 * 
	 * @param model the Spring MVC model
	 * @return the view name for the students list page
	 */
	@GetMapping
	public String listStudents(Model model) {
		logger.debug("Fetching list of active students");
		List<Student> activeStudents = studentService.getActiveStudents();
		model.addAttribute("students", activeStudents);
		logger.info("Displaying {} active students", activeStudents.size());
		return "students";
	}
	
	/**
	 * Displays the form for creating a new student.
	 * 
	 * @param model the Spring MVC model
	 * @return the view name for the create student form
	 */
	@GetMapping("/new")
	public String createStudentForm(Model model) {
		logger.debug("Displaying create student form");
		Student student = new Student();
		model.addAttribute("student", student);
		return "create_student";
	}
	
	/**
	 * Handles the submission of a new student.
	 * 
	 * @param student the student data from the form
	 * @param result the binding result for validation errors
	 * @param redirectAttributes for flash messages
	 * @return redirect to students list or back to form if validation fails
	 */
	@PostMapping
	public String saveStudent(@Valid @ModelAttribute("student") Student student,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Validation errors while creating student: {}", result.getAllErrors());
			return "create_student";
		}
		studentService.saveStudent(student);
		logger.info("Successfully created new student: {}", student.getEmail());
		redirectAttributes.addFlashAttribute("successMessage", "Student created successfully!");
		return "redirect:/students";
	}
	
	/**
	 * Displays the form for editing an existing student.
	 * 
	 * @param id the student ID
	 * @param model the Spring MVC model
	 * @return the view name for the edit student form
	 */
	@GetMapping("/edit/{id}")
	public String editStudentForm(@PathVariable Long id, Model model) {
		logger.debug("Displaying edit form for student id: {}", id);
		model.addAttribute("student", studentService.getStudentById(id));
		return "edit_student";
	}

	/**
	 * Handles the update of an existing student.
	 * 
	 * @param id the student ID
	 * @param student the updated student data
	 * @param result the binding result for validation errors
	 * @param redirectAttributes for flash messages
	 * @return redirect to students list or back to form if validation fails
	 */
	@PostMapping("/{id}")
	public String updateStudent(@PathVariable Long id,
			@Valid @ModelAttribute("student") Student student,
			BindingResult result,
			RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			logger.warn("Validation errors while updating student id {}: {}", id, result.getAllErrors());
			return "edit_student";
		}
		
		Student existingStudent = studentService.getStudentById(id);
		existingStudent.setFirstName(student.getFirstName());
		existingStudent.setLastName(student.getLastName());
		existingStudent.setEmail(student.getEmail());
		
		studentService.updateStudent(existingStudent);
		logger.info("Successfully updated student id: {}", id);
		redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
		return "redirect:/students";
	}
	
	/**
	 * Handles soft delete of a student by setting status to false.
	 * 
	 * @param id the student ID to delete
	 * @param redirectAttributes for flash messages
	 * @return redirect to students list
	 */
	@GetMapping("/delete/{id}")
	public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		logger.debug("Soft deleting student with id: {}", id);
		Student student = studentService.getStudentById(id);
		student.setStatus(false);
		studentService.updateStudent(student);
		logger.info("Successfully soft deleted student id: {}", id);
		redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
		return "redirect:/students";
	}
}
