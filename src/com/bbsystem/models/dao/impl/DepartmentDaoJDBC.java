package com.bbsystem.models.dao.impl;

import com.bbsystem.db.DB;
import com.bbsystem.db.exceptions.DBException;
import com.bbsystem.models.dao.DepartmentDao;
import com.bbsystem.models.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        String sql = "INSERT INTO department (Name) VALUES (?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1,obj.getName());
            DB.beginTransact();
            var rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                DB.commitTransact();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    obj.setId(resultSet.getInt(1));
                }
            } else {
                throw new SQLException("No row affected!");
            }
        } catch (SQLException e) {
            DB.rollBackTransact();
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void update(Department obj) {
        String sql = "UPDATE department SET department.Name = ? WHERE department.Id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1,obj.getName());
            preparedStatement.setInt(2,obj.getId());
            DB.beginTransact();
            preparedStatement.executeUpdate();
            DB.commitTransact();
        } catch (SQLException e) {
            DB.rollBackTransact();
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM department WHERE department.Id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1,id);
            DB.beginTransact();
            preparedStatement.executeUpdate();
            DB.commitTransact();
        } catch (SQLException e) {
            DB.rollBackTransact();
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Optional<Department> findById(Integer id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT Id, Name ");
        sql.append("FROM department ");
        sql.append("WHERE department.Id = ?");
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())){
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(instantiateDepartment(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Department>> findAll() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT Id, Name ");
        sql.append("FROM department ");
        sql.append("ORDER BY department.Name ");
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Department> departments = new ArrayList<>();
            while (resultSet.next()) {
                departments.add(instantiateDepartment(resultSet));
            }
            return departments.isEmpty() ? Optional.empty() : Optional.of(departments);
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException{
        Department department = new Department();
        department.setId(resultSet.getInt(1));
        department.setName(resultSet.getString(2));
        return department;
    }
}

