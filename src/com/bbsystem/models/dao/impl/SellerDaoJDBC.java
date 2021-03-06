package com.bbsystem.models.dao.impl;

import com.bbsystem.db.DB;
import com.bbsystem.db.exceptions.DBException;
import com.bbsystem.models.dao.SellerDao;
import com.bbsystem.models.entities.Department;
import com.bbsystem.models.entities.Seller;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO seller ");
        sql.append("(Name, Email, BirthDate, BaseSalary, DepartmentId) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?, ?)");
        try (PreparedStatement preparedStatement = conn
                .prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS)) {
            DB.beginTransact();
            preparedStatement.setString(1,obj.getName());
            preparedStatement.setString(2,obj.getEmail());
            preparedStatement.setDate(3,java.sql.Date.valueOf(obj.getBirthDate()));
            preparedStatement.setDouble(4,obj.getBaseSalary());
            preparedStatement.setInt(5,obj.getDepartment().getId());
            int rowsAffected = preparedStatement.executeUpdate();
            DB.commitTransact();

            if (rowsAffected <= 0) {
                throw new DBException("Error! No row affected!");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                obj.setId(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            DB.rollBackTransact();
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void update(Seller obj) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE seller ");
        sql.append("SET ");
        sql.append("Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? ");
        sql.append("WHERE ");
        sql.append("Id = ?");
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
            DB.beginTransact();
            preparedStatement.setString(1,obj.getName());
            preparedStatement.setString(2,obj.getEmail());
            preparedStatement.setDate(3,java.sql.Date.valueOf(obj.getBirthDate()));
            preparedStatement.setDouble(4,obj.getBaseSalary());
            preparedStatement.setInt(5,obj.getDepartment().getId());
            preparedStatement.setInt(6,obj.getId());
            preparedStatement.executeUpdate();
            DB.commitTransact();
        } catch (SQLException e) {
            DB.rollBackTransact();
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM seller WHERE seller.Id = ?";
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
    public Optional<Seller> findById(Integer id) {
        var sql = new StringBuilder();
        sql.append("SELECT seller.*, department.name AS name_department ");
        sql.append("FROM seller INNER JOIN department ON seller.DepartmentId = department.id ");
        sql.append("WHERE seller.id = ? ");
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);
                Seller seller = instantiateSeller(resultSet,department);
                return Optional.of(seller);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Seller>> findAll() {
        var sql = new StringBuilder();
        sql.append("SELECT seller.*, department.name AS name_department ");
        sql.append("FROM seller INNER JOIN department ON seller.DepartmentId = department.id ");
        sql.append("ORDER BY seller.name");
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Seller> sellers = new ArrayList<>();
            Map<Integer,Department> departmentMap = new HashMap<>();
            while (resultSet.next()) {
                Integer departmentIdResultSet = resultSet.getInt("DepartmentId");
                Department department;
                if (departmentMap.containsKey(departmentIdResultSet)) {
                    department = departmentMap.get(departmentIdResultSet);
                } else {
                    department = instantiateDepartment(resultSet);
                    departmentMap.put(departmentIdResultSet,department);
                };
                Seller seller = instantiateSeller(resultSet,department);
                sellers.add(seller);
            }
            return Optional.of(sellers);
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Seller>> findByDepartmentId(Integer departmentId) {
        var sql = new StringBuilder();
        sql.append("SELECT seller.*, department.name AS name_department ");
        sql.append("FROM seller INNER JOIN department ON seller.DepartmentId = department.id ");
        sql.append("WHERE seller.DepartmentId = ? ");
        sql.append("ORDER BY seller.name");
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
            preparedStatement.setInt(1,departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Seller> sellers = new ArrayList<>();
            Map<Integer,Department> departmentMap = new HashMap<>();
            while (resultSet.next()) {
                Integer departmentIdResultSet = resultSet.getInt("DepartmentId");
                Department department;
                if (departmentMap.containsKey(departmentIdResultSet)) {
                    department = departmentMap.get(departmentIdResultSet);
                } else {
                    department = instantiateDepartment(resultSet);
                    departmentMap.put(departmentIdResultSet,department);
                };
                Seller seller = instantiateSeller(resultSet,department);
                sellers.add(seller);
            }
            return Optional.of(sellers);
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate").toLocalDate());
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("name_department"));
        return department;
    }
}
