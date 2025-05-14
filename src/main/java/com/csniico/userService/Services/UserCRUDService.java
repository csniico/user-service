package com.csniico.userService.Services;

import com.csniico.userService.Entity.User;
import com.csniico.userService.dto.UserRequest;
import com.csniico.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCRUDService {

    private final UserRepository userRepository;

    @Autowired
    public UserCRUDService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createUser(UserRequest userRequest) {
        try{
            User user = new User();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());
            userRepository.save(user);
            return true;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public void updateUser(UserRequest userRequest) {
        try{
            User user = userRepository.findById(userRequest.getUserId()).get();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());
            userRepository.save(user);
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteUser(Long userId) {
        try{
            userRepository.deleteById(userId);
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public User getUser(Long userId) {
        try{
            return userRepository.findById(userId).get();
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public User getUserByEmail(String email) {
        try{
            User user = userRepository.findByEmail(email);
            if(user == null) {
                throw new RuntimeException("User not found");
            }

            return user;
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public User signIn(String email, String password) {
        try{
            User user = userRepository.findByEmail(email);
            if(user == null) {
                throw new RuntimeException("User not found");
            }
            if(!user.getPassword().equals(password)) {
                throw new RuntimeException("Invalid password");
            }

            return user;
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
