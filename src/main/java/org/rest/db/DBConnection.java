package org.rest.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/ecommerceDB";
        String user = "root";
        String password = "DEannd87!mysql";
        return DriverManager.getConnection(url, user, password);
    }
}
