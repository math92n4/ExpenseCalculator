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

    public boolean doesUsernameExist(String userName) {

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "SELECT userName FROM users WHERE userName = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "SELECT * FROM users";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("userid"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public void createGroup(GroupDTO group) {

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
            for (Integer user : group.getListOfUsers()) {
                String insertUserGroup = "INSERT INTO user_group (userid, groupid) VALUES (?,?)";
                PreparedStatement insertJoin = con.prepareStatement(insertUserGroup);
                insertJoin.setInt(1,user);
                insertJoin.setInt(2, groupID);
                insertJoin.executeUpdate();
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

        List<UserExpenseDTO> userExpenses = new ArrayList<>();
        List<User> users = new ArrayList<>();

        try {
            Connection con = DatabaseCon.getConnection();

            //get users from group
            String SQL = "SELECT user_group.userid, users.username FROM user_group" +
                    " INNER JOIN users ON user_group.userid = users.userid WHERE user_group.groupid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setInt(1, groupid);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("userid"));
                user.setUserName(resultSet.getString("username"));
                users.add(user);
            }

            for (User user : users) {
                //get expense for each user
                String expenseSQL = "SELECT expense FROM expenses WHERE userid = ? AND groupid = ?";
                PreparedStatement expenseStatement = con.prepareStatement(expenseSQL);
                expenseStatement.setInt(1, user.getUserID());
                expenseStatement.setInt(2, groupid);
                ResultSet expenseResult = expenseStatement.executeQuery();

                if (expenseResult.next()) {
                    double expense = expenseResult.getDouble("expense");
                    UserExpenseDTO userExpense = new UserExpenseDTO(user.getUserID(), user.getUserName(), expense);
                    userExpenses.add(userExpense);
                }
            }




        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userExpenses;
    }

    public void createExpense(double expense, int userid, int groupid) {

        try {
            Connection con = DatabaseCon.getConnection();
            String SQL = "UPDATE expenses SET expense = expense + ? WHERE userid = ? AND groupid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setDouble(1, expense);
            preparedStatement.setInt(2,userid);
            preparedStatement.setInt(3,groupid);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                createExpenseIfTheresNoRow(expense, userid, groupid);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createExpenseIfTheresNoRow(double expense, int userid, int groupid) {

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
