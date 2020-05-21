package com.bbsystem;

import com.bbsystem.models.dao.DaoFactory;
import com.bbsystem.models.dao.SellerDao;
import com.bbsystem.models.entities.Department;
import com.bbsystem.models.entities.Seller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        System.out.println("====== TEST 1: Seller findById() ========");
        System.out.println(sellerDao.findById(3).get());

        System.out.println("====== TEST 2: Seller findByDepartment ========");
        Optional<List<Seller>> sellerList = sellerDao.findByDepartmentId(2);
        if (!sellerList.isEmpty()) {
            sellerList.get().forEach(System.out::println);
        }

    }
}
