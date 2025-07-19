package com.fitness.idm.repository;

import com.fitness.idm.dto.InvalidTokenDTO;

import java.sql.*;

public class InvalidTokenRepository {

    private Connection connection;

    public InvalidTokenRepository() {
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
    public InvalidTokenDTO save(InvalidTokenDTO token) throws SQLException {
        String query = "INSERT INTO token (token, information) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, token.getToken());
            preparedStatement.setString(2, token.getInformation());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error while adding the token into table");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    token.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("ID not generated");
                }
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
            throw e;
        }
        return token;
    }

    public InvalidTokenDTO findByToken(String token) {
        String sql = "SELECT * FROM token WHERE token = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, token);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    InvalidTokenDTO invalidToken = new InvalidTokenDTO();
                    invalidToken.setId(resultSet.getInt("id"));
                    invalidToken.setToken(resultSet.getString("token"));
                    invalidToken.setInformation(resultSet.getString("information"));
                    return invalidToken;
                }
            }
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null;
    }
}
