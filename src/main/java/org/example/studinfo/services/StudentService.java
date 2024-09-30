package org.example.studinfo.services;

import org.example.studinfo.DTO.StudentDTO;
import org.example.studinfo.entities.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    public List<Student> findAll();

    public Optional<Student> findById(Long id);

    public List<Student> findAllByGroupNumber(String groupNumber);

    public List<Student> findAllBySurname(String surname);

    public List<Student> findAllBySurnameAndGroupNumber(String surname, String number);

    public void deleteById(Long id);

    public Student create(Student student);

    public Student update(Long id, Student student);

    public Student create(StudentDTO dto);

    public Student update(Long id, StudentDTO dto);
}
