package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.exceptions.StudentNotFoundException;
import com.nacosfunaabpay.paymentplatform.model.AcademicYear;
import com.nacosfunaabpay.paymentplatform.model.Level;
import com.nacosfunaabpay.paymentplatform.model.Program;
import com.nacosfunaabpay.paymentplatform.model.Student;
import com.nacosfunaabpay.paymentplatform.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProgramService programService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private AcademicYearService academicYearService;

    @Transactional
    public Student handleStudentRegistration(PaymentFormDTO paymentForm) {
        log.info("Processing registration for student number: {}", paymentForm.getRegistrationNumber());

        try {
            Optional<Student> existingStudent = findStudentByRegistrationNumber(paymentForm.getRegistrationNumber());

            if (existingStudent.isPresent()) {
                Student updatedStudent = updateExistingStudent(existingStudent.get(), paymentForm);
                log.info("Updated existing student registration: {}", updatedStudent.getRegistrationNumber());
                return updatedStudent;
            } else {
                Student newStudent = createNewStudent(paymentForm);
                log.info("Created new student registration: {}", newStudent.getRegistrationNumber());
                return newStudent;
            }
        } catch (Exception e) {
            log.error("Error processing student registration: {}", paymentForm.getRegistrationNumber(), e);
            throw new RuntimeException("Failed to process student registration", e);
        }
    }

    private Optional<Student> findStudentByRegistrationNumber(String registrationNumber) {
        log.debug("Finding student by registration number: {}", registrationNumber);
        return studentRepository.findByRegistrationNumber(registrationNumber);
    }

    private Student createNewStudent(PaymentFormDTO paymentForm) {
        log.debug("Creating new student with registration number: {}", paymentForm.getRegistrationNumber());
        Student student = new Student();
        updateStudentFromForm(student, paymentForm);
        return studentRepository.save(student);
    }

    private Student updateExistingStudent(Student student, PaymentFormDTO paymentForm) {
        log.debug("Updating existing student: {}", student.getRegistrationNumber());
        updateStudentFromForm(student, paymentForm);
        return studentRepository.save(student);
    }

    private void updateStudentFromForm(Student student, PaymentFormDTO paymentForm) {
        student.setName(paymentForm.getFirstName() + " " + paymentForm.getLastName());
        student.setEmail(paymentForm.getEmail());
        student.setPhoneNumber(paymentForm.getPhoneNumber());
        student.setRegistrationNumber(paymentForm.getRegistrationNumber());

        Program program = programService.findProgram(paymentForm.getProgram());
        Level level = levelService.findLevel(paymentForm.getLevel());
        AcademicYear academicYear = academicYearService.getCurrentAcademicYear();

        student.setProgram(program);
        student.setLevel(level);
        student.setAcademicYear(academicYear);
    }
}
