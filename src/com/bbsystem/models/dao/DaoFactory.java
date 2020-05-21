package com.bbsystem.models.dao;

import com.bbsystem.db.DB;
import com.bbsystem.models.dao.impl.DepartmentDaoJDBC;
import com.bbsystem.models.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao() {
        return new SellerDaoJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao() {
        return new DepartmentDaoJDBC();
    }

}
