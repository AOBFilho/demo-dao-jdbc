package com.bbsystem.models.dao;

import com.bbsystem.models.entities.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentDao {

    void insert(Department obj);
    void update(Department obj);
    void deleteById(Integer id);
    Optional<Department> findById(Integer id);
    Optional<List<Department>> findAll();

}
