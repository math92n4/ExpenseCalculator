package com.example.expensecalculator.repositories;


import com.example.expensecalculator.dtos.GroupDTO;
import com.example.expensecalculator.dtos.UserExpenseDTO;
import com.example.expensecalculator.entities.Group;
import com.example.expensecalculator.entities.User;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<User> getUsersByGroupID(int groupID) {
        List<User> users = new ArrayList<>();
        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "SELECT users.userid, userName FROM users INNER JOIN user_group ON users.userid = user_group.userid WHERE user_group.groupid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setInt(1,groupID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt(1));
                user.setUserName(resultSet.getString(2));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public boolean doesExist(List<String> userNames) {

        try {
            Connection con = DatabaseCon.getConnection();
            for (String userName : userNames) {
                String SQL = "SELECT username FROM users WHERE username = ?";
                try (PreparedStatement preparedStatement = con.prepareStatement(SQL)) {
                    preparedStatement.setString(1, userName);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (!resultSet.next()) {
                            return false;
                        }
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void createGroup(Group group) {

        try {
            Connection con = DatabaseCon.getConnection();
            //insert group
            String insertGroup = "INSERT INTO `groups` (groupname, groupdesc) VALUES(?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertGroup, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, group.getName());
            preparedStatement.setString(2, group.getDesc());
            preparedStatement.executeUpdate();

            //get id from group
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            int groupID = 0;
            if (generatedKey.next()) {
                groupID = generatedKey.getInt(1);
            }

            //insert ids into user_group table
            String insertUserGroup = "INSERT INTO user_group (userid, groupid) VALUES (?,?)";
            PreparedStatement userGroup = con.prepareStatement(insertUserGroup);
            for (String member : group.getMembers()) {
                //find user
                String findUser = "SELECT userid FROM users WHERE username = ?";
                PreparedStatement findUserStatement = con.prepareStatement(findUser);
                findUserStatement.setString(1, member);
                ResultSet userResult = findUserStatement.executeQuery();

                int userID = 0;
                if (userResult.next()) {
                    userID = userResult.getInt("userid");
                }

                userGroup.setInt(1, userID);
                userGroup.setInt(2, groupID);
                userGroup.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Group> getGroupsByUserID(int id) {
        List<Group> groups = new ArrayList<>();

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "SELECT * FROM `groups`\n" +
                    "INNER JOIN user_group ON `groups`.groupid = user_group.groupid\n" +
                    "WHERE user_group.userid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                groups.add(new Group(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public Group getGroupByGroupID(int groupid) {
        Group group = null;
        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "SELECT * FROM `groups` WHERE groupid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setInt(1,groupid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                group = new Group(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public List<UserExpenseDTO> getUsersAndExpensesByGroupID(int groupid) {

        List<UserExpenseDTO> users = new ArrayList<>();

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "SELECT users.userid, users.username, SUM(expenses.expense)\n" +
                    "FROM users\n" +
                    "INNER JOIN expenses ON users.userid = expenses.userid\n" +
                    "INNER JOIN user_group ON users.userid = user_group.userid\n" +
                    "INNER JOIN `groups` ON user_group.groupid = `groups`.groupid\n" +
                    "WHERE `groups`.groupid = ?\n" +
                    "GROUP BY users.userid, users.username\n" +
                    "ORDER BY users.userid";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setInt(1,groupid);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserExpenseDTO user = new UserExpenseDTO(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void createExpense(double expense, int userid, int groupid) {

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "INSERT INTO expenses (expense, userid, groupid) VALUES (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setDouble(1, expense);
            preparedStatement.setInt(2,userid);
            preparedStatement.setInt(3,groupid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
