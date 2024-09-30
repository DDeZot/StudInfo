package org.example.studinfo.services;

import org.example.studinfo.DTO.GroupDTO;
import org.example.studinfo.entities.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    public List<Group> findAll();

    public Optional<Group> findById(Long id);

    public List<Group> findAllByNumber(String number);

    public List<Group> findAllByName(String name);

    public void deleteById(Long id);

    public void deleteByNumber(String number);

    public Group create(Group group);

    public Group update(Long id, Group group);

    public Group create(GroupDTO dto);

    public Group update(Long id, GroupDTO dto);
}
