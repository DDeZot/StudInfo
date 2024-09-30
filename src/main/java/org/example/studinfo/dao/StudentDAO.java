package org.example.studinfo.dao;

import org.example.studinfo.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentDAO extends JpaRepository<Student, Long> {
    @Transactional(readOnly = true)
    @Query("select s from Student s where s.group.number=:number")
    public List<Student> findAllByGroupNumber(String number);

    @Transactional(readOnly = true)
    @Query("select s from Student s where s.surname=:surname")
    public List<Student> findAllBySurname(String surname);

    @Transactional(readOnly = true)
    @Query("select s from Student s where s.surname=:surname and  s.group.number=:number")
    public List<Student> findAllBySurnameAndAndGroupNumber(String surname, String number);
}
