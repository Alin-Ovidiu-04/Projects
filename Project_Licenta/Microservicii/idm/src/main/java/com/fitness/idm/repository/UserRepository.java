package com.fitness.idm.repository;

import com.fitness.idm.dto.UserDTO;

import java.sql.*;
//import org.mariadb.jdbc.Connection;
//import org.mariadb.jdbc.Statement;
public class UserRepository {
    private Connection connection;

    public UserRepository() {
        String url = "jdbc:mysql://:3306/idm?useSSL=true";
        String username = "root";
        String password = "@overwritemysql";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to MySQL database: " + e.getMessage());
        }
    }

    public UserDTO save(UserDTO user) throws SQLException {
        String query = "INSERT INTO user (role, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getRole());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("User creation failed");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("No id");
                }
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
            throw  e;
        }
        return user;
    }

    public UserDTO getUserByUsername(String username) {

        UserDTO user = null;
        String query = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new UserDTO();
                    user.setId(resultSet.getLong("id"));
                    user.setRole(resultSet.getString("role"));
                    user.setUsername(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return user;
    }
}