package com.bbsystem.models.dao;

import com.bbsystem.models.entities.Seller;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface SellerDao {

    void insert(Seller obj);
    void update(Seller obj);
    void deleteById(Integer id);
    Optional<Seller> findById(Integer id);
    Optional<List<Seller>> findAll();
    Optional<List<Seller>> findByDepartmentId(Integer id);

}
