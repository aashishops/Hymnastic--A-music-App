package com.aina.hymnastic.Sql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HymnasticDatabaseConnection {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/hymnastic";
        String username = "root";
        String password = "root";

        String inputUsername = " "; 
        String inputPassword = " "; 

        try {
            
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            if (connection != null) {
               
                
                Statement statement = connection.createStatement();

                
                String sqlQuery = "SELECT * FROM login WHERE username = '" + inputUsername + "'";
                // String sqlQuery = "SELECT * FROM login WHERE password = '" + inputPassword + "'";
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                
                if (resultSet.next()) {
                    
                    String dbPassword = resultSet.getString("password");

                    if (inputPassword.equals(dbPassword)) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }

             
                resultSet.close();
                statement.close();
                connection.close();
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
