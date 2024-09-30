package org.example.studinfo.services.impl;

import org.example.studinfo.DTO.StudentDTO;
import org.example.studinfo.dao.StudentDAO;
import org.example.studinfo.entities.Student;
import org.example.studinfo.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentDAO studentDAO;

    @Autowired
    public StudentServiceImpl(StudentDAO studentDAO){
        this.studentDAO = studentDAO;
    }

    public List<Student> findAll(){
        return studentDAO.findAll();
    }

    public Optional<Student> findById(Long id){
        return studentDAO.findById(id);
    }

    public List<Student> findAllByGroupNumber(String groupNumber){
        return studentDAO.findAllByGroupNumber(groupNumber);
    }

    public List<Student> findAllBySurname(String surname){
        return studentDAO.findAllBySurname(surname);
    }

    @Override
    public List<Student> findAllBySurnameAndGroupNumber(String surname, String number) {
        return studentDAO.findAllBySurnameAndAndGroupNumber(surname, number);
    }

    public void deleteById(Long id){
        studentDAO.deleteById(id);
    }

    public Student create(Student student) {
        if (studentDAO.existsById(Objects.requireNonNull(student.getId())))
            return null;
        else
            return studentDAO.save(student);
    }

    public Student update(Long id, Student student) {
        if(studentDAO.existsById(id))
            return studentDAO.save(student);
        else
            return null;
    }

    @Override
    public Student create(StudentDTO dto) {
        return studentDAO.save(Student.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .patronymic(dto.getPatronymic())
                .birthDate(dto.getBirthDate())
                .group(dto.getGroup())
                .build());
    }

    @Override
    public Student update(Long id, StudentDTO dto) {
        if(studentDAO.existsById(id))
            return studentDAO.save(Student.builder()
                    .id(id)
                    .name(dto.getName())
                    .surname(dto.getSurname())
                    .patronymic(dto.getPatronymic())
                    .birthDate(dto.getBirthDate())
                    .group(dto.getGroup())
                    .build());
        else
            return null;
    }
}