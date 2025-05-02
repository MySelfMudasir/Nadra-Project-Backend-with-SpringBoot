package com.example.SpringBootCRUD.Service;

import com.example.SpringBootCRUD.model.Employee;
import com.example.SpringBootCRUD.model.UserSchema;
import com.example.SpringBootCRUD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    // Injecting the UserRepository to save data to DB
    private UserRepository userRepository;


    public ResponseEntity<String> saveUser(String username, String email, String address) {
        // Create a new UserSchema object and set the values
        UserSchema user = new UserSchema();
        user.setUsername(username);
        user.setEmail(email);
        user.setAddress(address);

        try {
            // Save the user to the database
            userRepository.save(user);
        } catch (Exception e) {
            // Return a 500 response if there was an error saving
            return new ResponseEntity<>("Error: User addition failed. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Return a success response if everything went well
        return new ResponseEntity<>("User added successfully", HttpStatus.OK);
    }




    public List<UserSchema> findAllUsers() {
        return userRepository.findAll();
    }


    public Optional<UserSchema> findUserById(Long id) {
        return userRepository.findById(id); // Assuming userRepository extends JpaRepository
    }


    public UserSchema updateUser(UserSchema user) {
        return userRepository.save(user);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }



}
