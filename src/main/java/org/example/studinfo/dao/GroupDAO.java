package org.example.studinfo.dao;

import org.example.studinfo.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupDAO extends JpaRepository<Group, Long> {
    @Query("select g from Group g where g.number like :number")
    @Transactional(readOnly = true)
    public List<Group> findAllByNumber(String number);

    @Query("select g from Group g where g.number like :name")
    @Transactional(readOnly = true)
    public List<Group> findAllByName(String name);

    @Query(value = "delete from GROUPS where NUMBER=:number", nativeQuery = true)
    @Modifying
    @Transactional
    public void deleteByNumber(String number);
}
