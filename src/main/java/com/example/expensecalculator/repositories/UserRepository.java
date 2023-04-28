package com.example.expensecalculator.repositories;


import com.example.expensecalculator.entities.User;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {


    public void registerUser(User user) {

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "INSERT INTO users(username, password, email) VALUES(?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void editUser(int id, User editedUser) {

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "UPDATE users SET username = ?, password = ?, email = ? WHERE userid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setString(1, editedUser.getUserName());
            preparedStatement.setString(2, editedUser.getPassword());
            preparedStatement.setString(3, editedUser.getEmail());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public User getUserByCredentials(String userName) {

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "SELECT * FROM users WHERE userName = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserByEmail(String email) {

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "SELECT * FROM users WHERE email = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
