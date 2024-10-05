package com.nacosfunaabpay.paymentplatform.service;

import com.nacosfunaabpay.paymentplatform.dtos.PaymentFormDTO;
import com.nacosfunaabpay.paymentplatform.exceptions.StudentNotFoundException;
import com.nacosfunaabpay.paymentplatform.model.AcademicYear;
import com.nacosfunaabpay.paymentplatform.model.Level;
import com.nacosfunaabpay.paymentplatform.model.Program;
import com.nacosfunaabpay.paymentplatform.model.Student;
import com.nacosfunaabpay.paymentplatform.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProgramService programService;

    @Autowired
    private LevelService levelService;

    @Autowired
    private AcademicYearService academicYearService;

    @Override
    @Transactional
    public Student createOrUpdateStudent(Student student) {

//        todo: work logic to update student values on user re-submit with the same values...
        if (student.getId() == null) {
            student.setCreatedAt(LocalDateTime.now());
        }
        student.setUpdatedAt(LocalDateTime.now());
        return studentRepository.save(student);
    }

    @Override
    public Student findStudentByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with email: " + email));
    }

    @Override
    public Student findStudentByRegistrationNumber(String registrationNumber) {
        return studentRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with registration number: " + registrationNumber));
    }

    @Override
    @Transactional
    public Student createStudent(PaymentFormDTO paymentForm) {
        Student student = new Student();
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

        return createOrUpdateStudent(student);
    }
}
