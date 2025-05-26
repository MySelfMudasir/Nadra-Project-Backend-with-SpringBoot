package com.example.SpringBootCRUD.controller;

import com.example.SpringBootCRUD.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.SpringBootCRUD.model.UserSchema;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/nadra")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/addAccount")
    public ResponseEntity<String> addUser(@RequestBody UserSchema userschema) {

        System.out.println("=================================================");
        System.out.println("Received request to add user: " + userschema);
        System.out.println("=================================================");

        // Validate the inputs
        if (userschema.getEmail() == null || userschema.getEmail().trim().isEmpty()) {
            return new ResponseEntity<>("Error: Email is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema.getAccountType() == null || userschema.getAccountType().trim().isEmpty()) {
            return new ResponseEntity<>("Error: Account type is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema.getCnic() == null || userschema.getCnic().trim().isEmpty()) {
            return new ResponseEntity<>("Error: CNIC is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema.getCnicIssueDate() == null) {
            return new ResponseEntity<>("Error: CNIC issue date is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema.getMobile() == null || userschema.getMobile().trim().isEmpty()) {
            return new ResponseEntity<>("Error: Mobile number is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema.getAccountType() == null || userschema.getAccountType().trim().isEmpty()) {
            return new ResponseEntity<>("Error: AccountType is required", HttpStatus.BAD_REQUEST);
        }
        if (userschema.getRecordType() == null || userschema.getRecordType().trim().isEmpty()) {
            return new ResponseEntity<>("Error: RecordType is required", HttpStatus.BAD_REQUEST);
        }

        // Convert LocalDate to String (assuming format is yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String cnicIssueDateStr = userschema.getCnicIssueDate().format(formatter); // Convert LocalDate to String

        return userService.saveAccount(userschema.getEmail(), userschema.getAccountType(), userschema.getCnic(), cnicIssueDateStr, userschema.getMobile(), userschema.getRecordType(), userschema.getNtn(), userschema.getSubRecords());
    }




    @GetMapping("/getAccountsDetails")
    public ResponseEntity<?> getPrimaryAccountsWithSubs() {
        try {
            List<UserSchema> data = userService.findAllUsers();

            if (data == null ) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





//    @GetMapping("/getAccount/{id}")
//    public ResponseEntity<UserSchema> getUserById(@PathVariable("id") Long id) {
//        try {
//            Optional<UserSchema> user = userService.findUserById(id);
//            if (user.isPresent()) {
//                return new ResponseEntity<>(user.get(), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PutMapping("/updateAccount")
    public ResponseEntity<?> updateUser(@RequestBody UserSchema userschema) {

        System.out.println("=================================================");
        System.out.println("Received request to update user: " + userschema);
        System.out.println("=================================================");

        // Convert LocalDate to String (assuming format is yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String cnicIssueDateStr = userschema.getCnicIssueDate().format(formatter); // Convert LocalDate to String

        return userService.updateAccount(userschema.getId(), userschema.getEmail(), userschema.getAccountType(), userschema.getCnic(), cnicIssueDateStr, userschema.getMobile(), userschema.getRecordType(), userschema.getNtn(), userschema.getSubRecords());
    }




    @PutMapping("/resetStatus")
    public ResponseEntity<?> resetStatus(@RequestBody UserSchema userschema) {

        System.out.println("=================================================");
        System.out.println("Received request to update user: " + userschema);
        System.out.println("=================================================");

        return userService.resetStatus(userschema.getId(),userschema.getCnic());
    }



//    @DeleteMapping("/deleteAccount/{id}")
//    public ResponseEntity<?> delteUser(@PathVariable("id") Long id) {
//        try {
//            Optional<UserSchema> user = userService.findUserById(id);
//            if(user.isPresent()) {
//                userService.deleteUser(id);
//                return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//            }
//        }
//        catch (Exception e) {
//            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



}
