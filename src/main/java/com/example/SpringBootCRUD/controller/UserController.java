package com.example.SpringBootCRUD.controller;

import com.example.SpringBootCRUD.Service.UserService;
import com.example.SpringBootCRUD.model.Employee;
import com.example.SpringBootCRUD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SpringBootCRUD.model.UserSchema;

import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/addUser")
    public ResponseEntity<String> addUser(@RequestBody UserSchema userschema) {

        // Optional: Validate the inputs (e.g., check if the email is valid)
        if (userschema.getUsername() == null || userschema.getUsername().trim().isEmpty()) {
            return new ResponseEntity<>("Error: User name is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema.getEmail() == null || userschema.getEmail().trim().isEmpty()) {
            return new ResponseEntity<>("Error: Email is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema.getAddress() == null || userschema.getAddress().trim().isEmpty()) {
            return new ResponseEntity<>("Error: Address is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema == null) {
            return new ResponseEntity<>("Received null user schema", HttpStatus.BAD_REQUEST);
        }
        return userService.saveUser(userschema.getUsername(), userschema.getEmail(), userschema.getAddress());
    }



    @GetMapping("/getUsers")
    public ResponseEntity<List<UserSchema>> getUsers() {
        try {
            List<UserSchema> data = userService.findAllUsers();
            if (data == null || data.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error occurred while fetching users: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserSchema> getUserById(@PathVariable("id") Long id) {
        try {
            Optional<UserSchema> user = userService.findUserById(id);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long id, @RequestBody UserSchema userSchema) {
        try {
            Optional<UserSchema> existingUserOptional = userService.findUserById(id);
            if (existingUserOptional.isPresent()) {
                UserSchema existingUser = existingUserOptional.get();
                existingUser.setUsername(userSchema.getUsername());
                existingUser.setEmail(userSchema.getEmail());
                existingUser.setAddress(userSchema.getAddress());

                userService.updateUser(existingUser);
                return new ResponseEntity<String>("Updated successfully", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> delteUser(@PathVariable("id") Long id) {
        try {
            Optional<UserSchema> user = userService.findUserById(id);
            if(user.isPresent()) {
                userService.deleteUser(id);
                return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
