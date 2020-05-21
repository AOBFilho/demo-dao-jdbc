package com.bbsystem.models.dao.impl;

import com.bbsystem.models.dao.DepartmentDao;
import com.bbsystem.models.entities.Department;

import java.util.List;
import java.util.Optional;

public class DepartmentDaoJDBC implements DepartmentDao {
    @Override
    public void insert(Department obj) {

    }

    @Override
    public void update(Department obj) {

    }

    @Override
    public void delete(Department obj) {

    }

    @Override
    public Optional<Department> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Department>> findAll() {
        return Optional.empty();
    }
}
