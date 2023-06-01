package com.example.expensecalculator.services;

import com.example.expensecalculator.dtos.GroupDTO;
import com.example.expensecalculator.dtos.GroupTransferDTO;
import com.example.expensecalculator.dtos.TransferDTO;
import com.example.expensecalculator.dtos.UserExpenseDTO;
import com.example.expensecalculator.entities.Group;
import com.example.expensecalculator.entities.User;
import com.example.expensecalculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.registerUser(user);
    }

    public void editUser(int id, User editedUser) {
        String encodedPassword = passwordEncoder.encode(editedUser.getPassword());
        editedUser.setPassword(encodedPassword);
        userRepository.editUser(id, editedUser);
    }

    public User getUserByCredentials(String userName, String password) {
        User user = userRepository.getUserByCredentials(userName);
        if (user != null) {
            String encodedPassword = user.getPassword();
            boolean passwordMatch = passwordEncoder.matches(password, encodedPassword);
            if (passwordMatch) {
                return user;
            }
        }
        return null;
    }

    public boolean doesUsernameExist(String userName) {
        return userRepository.doesUsernameExist(userName);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void createGroup(GroupDTO group) {
        userRepository.createGroup(group);
    }

    public List<Group> getGroupsByUserID(int id) {
        return userRepository.getGroupsByUserID(id);
    }

    public Group getGroupByGroupID(int groupid) {
        return userRepository.getGroupByGroupID(groupid);
    }

    public List<UserExpenseDTO> getUsersAndExpensesByGroupID(int groupid) {
        return userRepository.getUsersAndExpensesByGroupID(groupid);
    }

    public void createExpense(double expense, int userid, int groupid) {
        userRepository.createExpense(expense, userid, groupid);
    }

    public List<TransferDTO> calculateExpenses(int groupid) {
        List<UserExpenseDTO> users = userRepository.getUsersAndExpensesByGroupID(groupid);
        Set<UserExpenseDTO> usersSet = new HashSet<>();
        double amountToShare = 0;

        for (UserExpenseDTO user : users) {
            UserExpenseDTO person = new UserExpenseDTO();
            person.setUserName(user.getUserName());
            person.setExpense(user.getExpense());
            amountToShare += user.getExpense();
            usersSet.add(person);
        }
        GroupTransferDTO group = new GroupTransferDTO(amountToShare,usersSet);
        return group.calculateTransfers();
    }

    public void deleteGroup(int groupid) {
        userRepository.deleteGroup(groupid);
    }
}
