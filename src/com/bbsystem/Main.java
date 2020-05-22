package com.bbsystem;

import com.bbsystem.models.dao.DaoFactory;
import com.bbsystem.models.dao.SellerDao;
import com.bbsystem.models.entities.Department;
import com.bbsystem.models.entities.Seller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        SellerDao sellerDao = DaoFactory.createSellerDao();
        System.out.println("====== TEST 1: Seller findById() ========");
        System.out.println(sellerDao.findById(3).get());

        System.out.println("====== TEST 2: Seller findByDepartment ========");
        Optional<List<Seller>> sellerListByDepartment = sellerDao.findByDepartmentId(2);
        if (!sellerListByDepartment.isEmpty()) {
            sellerListByDepartment.get().forEach(System.out::println);
        }

        System.out.println("====== TEST 3: Seller findAll ========");
        Optional<List<Seller>> sellerList = sellerDao.findAll();
        if (!sellerList.isEmpty()) {
            sellerList.get().forEach(System.out::println);
        }

        System.out.println("====== TEST 4: Seller insert ========");
        Seller seller = new Seller(null,"jane","jane@gmail.com",
                LocalDate.parse("11/03/1981", DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                1000.00, new Department(1,null));
        sellerDao.insert(seller);
        System.out.println("New seller id: "+seller.getId());

        System.out.println("====== TEST 5: Seller update ========");
        seller = sellerDao.findById(1).get();
        seller.setName("Problema Resolvido");
        sellerDao.update(seller);
        System.out.println("Update success!");

        System.out.println("====== TEST 6: Seller deleteById ========");
        System.out.println("Enter with seller id for delete: ");
        int sellerIdDelete = sc.nextInt();
        sellerDao.deleteById(sellerIdDelete);
        System.out.println("Delete success!");
    }
}
