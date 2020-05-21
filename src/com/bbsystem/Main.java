package com.bbsystem;

import com.bbsystem.models.dao.DaoFactory;
import com.bbsystem.models.dao.SellerDao;
import com.bbsystem.models.entities.Department;
import com.bbsystem.models.entities.Seller;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        System.out.println(sellerDao.findById(3).get());
    }
}
