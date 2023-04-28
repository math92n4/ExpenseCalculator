package com.example.expensecalculator.services;

import com.example.expensecalculator.entities.User;
import com.example.expensecalculator.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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



    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }
}
