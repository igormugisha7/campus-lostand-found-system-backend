package com.mugisha.campuslostandfound.service;

import com.mugisha.campuslostandfound.entity.Student;
import com.mugisha.campuslostandfound.repository.CustodianRepository;
import com.mugisha.campuslostandfound.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CustodianRepository custodianRepository;

    public StudentService(StudentRepository studentRepository, CustodianRepository custodianRepository) {
        this.studentRepository = studentRepository;
        this.custodianRepository = custodianRepository;
    }

    public String save(Student student) {
        studentRepository.save(student);
        return "Student saved successfully";
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(UUID id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> update(UUID id, Student newStudent) {
        Optional<Student> result = studentRepository.findById(id);
        if (result.isPresent()) {
            Student student = result.get();
            student.setFirstName(newStudent.getFirstName());
            student.setLastName(newStudent.getLastName());
            student.setRegNumber(newStudent.getRegNumber());
            student.setEmail(newStudent.getEmail());
            student.setPhone(newStudent.getPhone());
            return Optional.of(studentRepository.save(student));
        }
        return Optional.empty();
    }

    public String delete(UUID id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return "Student deleted successfully";
        }
        return "The student with that id does not exist";
    }

    public List<Student> findByLastName(String lastName) {
        return studentRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public Optional<List<Student>> findStudentsOfCustodian(UUID custodianId) {
        if (custodianRepository.existsById(custodianId)) {
            return Optional.of(studentRepository.findStudentsOfCustodian(custodianId));
        }
        return Optional.empty();
    }
}
