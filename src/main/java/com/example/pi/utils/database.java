package com.example.pi.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    private static database instance;
    private final String URL = "jdbc:mysql://localhost:3306/dprojet";
    private final String USER = "root";
    private final String PASSWORD = "";
    private Connection connection;

    private database() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public static database getInstance()
    {
        if(instance==null)
            instance=new database();
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }


}



