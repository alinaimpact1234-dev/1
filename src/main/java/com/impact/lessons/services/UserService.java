package com.impact.lessons.services;

import com.impact.lessons.database.UserStore;
import com.impact.lessons.models.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private UserStore store = new UserStore();
    public User CreateUser(User user){
        User newUser = new User(user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
        return store.save(newUser);

    }
    public List<User> GetAllUsers(){
        return store.findAll();
    }
    public Optional<User> GetUserById(Long id){ 
        return store.findById(id); 
    }
    public Optional<User> UpdateUser(Long id, User user){
        User updatedUser = new User(user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());
        return store.update(id, updatedUser);
    }
    public Optional<User> UpdatePassword(Long id, String newPassword){
        return store.updatePassword(id, newPassword);
    }
    public Optional<Boolean> disableUser(Long id){
        return store.updateEnabled(id, false);
    }
}
