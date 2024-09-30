package org.example.studinfo.services.impl;

import org.example.studinfo.DTO.GroupDTO;
import org.example.studinfo.dao.GroupDAO;
import org.example.studinfo.entities.Group;
import org.example.studinfo.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupDAO groupDAO;

    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO){
        this.groupDAO = groupDAO;
    }

    @Override
    public List<Group> findAll() {
        return groupDAO.findAll();
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groupDAO.findById(id);
    }

    @Override
    public List<Group> findAllByNumber(String number) {
        return groupDAO.findAllByNumber(number);
    }

    @Override
    public List<Group> findAllByName(String name) {
        return groupDAO.findAllByName(name);
    }

    @Override
    public void deleteById(Long id) {
        groupDAO.deleteById(id);
    }

    @Override
    public void deleteByNumber(String number) {
        groupDAO.deleteByNumber(number);
    }

    @Override
    public Group create(Group group) {
        if (groupDAO.existsById(Objects.requireNonNull(group.getId())))
            return null;
        else
            return groupDAO.save(group);
    }

    @Override
    public Group update(Long id, Group group) {
       if(groupDAO.existsById(id))
           return groupDAO.save(group);
       else
           return null;
    }

    @Override
    public Group create(GroupDTO dto) {
        return groupDAO.save(Group.builder()
                .number(dto.getNumber())
                .name(dto.getName())
                .build());
    }

    @Override
    public Group update(Long id, GroupDTO dto) {
        if(groupDAO.existsById(id))
            return groupDAO.save(Group.builder()
                    .id(id)
                    .number(dto.getNumber())
                    .name(dto.getName())
                    .build());
        else
            return null;
    }
}
