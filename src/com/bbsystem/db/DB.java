package com.bbsystem.db;

import com.bbsystem.db.exceptions.DBException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try (FileInputStream fileInputStream = new FileInputStream("db.properties")) {
                Properties properties = new Properties();
                properties.load(fileInputStream);
                conn = DriverManager.getConnection(properties.getProperty("url"),properties);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
        return conn;
    }

    public static void beginTransact() {
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void commitTransact() {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void rollBackTransact() {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }

}
