package com.example.SpringBootCRUD.Service;

import com.example.SpringBootCRUD.model.UserSchema;
import com.example.SpringBootCRUD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    // Injecting the UserRepository to save data to DB
    private UserRepository userRepository;


    public ResponseEntity<String> saveAccount(String email, String accountType, String cnic, String cnicIssueDate, String mobile, String recordType, String ntn, List<UserSchema> subRecords) {
        // Create a new UserSchema object and set the values

        // Get the next ID from the repository
        Long nextId = userRepository.getNextManagementId();
        if (nextId == null) nextId = 1L;


        try {
            // Save main user
            UserSchema mainRecord = new UserSchema();
            mainRecord.setId(nextId);
            mainRecord.setAccountType(accountType.trim());
            mainRecord.setCnic(cnic.trim());
            mainRecord.setEmail(email.trim());
            mainRecord.setMobile(mobile.trim());
            mainRecord.setStatus("P".trim()); // Use uppercase
            mainRecord.setRecordType(recordType.trim()); // <- make sure this is being called!
            mainRecord.setNtn(ntn.trim()); // <- make sure this is being called!

            // Convert String date to LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate cnicIssueDateConverted = LocalDate.parse(cnicIssueDate, formatter);
            mainRecord.setCnicIssueDate(cnicIssueDateConverted);

            System.out.println("=================================================");
            System.out.println("Saving mainRecord: " + mainRecord);
            System.out.println("=================================================");

            // Save the user to the database
            userRepository.save(mainRecord);

            // Now save subRecords
            if (subRecords != null) {
                for (UserSchema sub : subRecords) {
                    Long subNextId = userRepository.getNextManagementId(); // get a new ID
                    sub.setId(subNextId); // set the ID
                    sub.setSubAccountId(nextId); // set the ID
                    mainRecord.setRecordType(recordType.trim()); // <- check if recordType is null
                    mainRecord.setNtn(ntn.trim()); // <- check if recordType is null
                    mainRecord.setCnic(cnic);
                    sub.setStatus("P"); // Also use uppercase
                    mainRecord.setCnicIssueDate(cnicIssueDateConverted);
                    userRepository.save(sub);
                }
            }
        } catch (Exception e) {
            // Return a 500 response if there was an error saving
            return new ResponseEntity<>("Error: Account addition failed. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return a success response if everything went well
        return new ResponseEntity<>("Account added successfully", HttpStatus.OK);
    }




    public List<UserSchema> findAllUsers() {
        List<UserSchema> primaryRecords = userRepository.findByRecordTypeNative("primary");
        for (UserSchema primary : primaryRecords) {
            List<UserSchema> subRecords = userRepository.findBySubAccountIdNative(primary.getId());
            primary.setSubRecords(subRecords);

            // Normalize primary + sub-records
            normalizeUserSchema(primary);
        }
        return primaryRecords;
    }




    private void normalizeUserSchema(UserSchema user) {
        // Normalize accountType
        if (user.getAccountType() != null) {
            switch (user.getAccountType().toLowerCase()) {
                case "i":
                    user.setAccountType("individual");
                    break;
                case "c":
                    user.setAccountType("corporate");
                    break;
            }
        }

        // Normalize status
        if (user.getStatus() != null) {
            switch (user.getStatus().toUpperCase()) {
                case "P":
                    user.setStatus("pending");
                    break;
                case "A":
                    user.setStatus("active");
                    break;
            }
        }

        // Normalize sub-records recursively
        if (user.getSubRecords() != null) {
            for (UserSchema sub : user.getSubRecords()) {
                normalizeUserSchema(sub);
            }
        }
    }






    public Optional<UserSchema> findUserById(Long id) {
        return userRepository.findById(id); // Assuming userRepository extends JpaRepository
    }





    public ResponseEntity<String> updateAccount(Long id, String email, String accountType, String cnic, String cnicIssueDateStr, String mobile, String recordType, String ntn, List<UserSchema> subRecords) {
        try {
            LocalDate cnicIssueDate = LocalDate.parse(cnicIssueDateStr);

            // 1. Update parent record
            int updated = userRepository.updateUserById(id, email, accountType, cnic, cnicIssueDate, mobile, recordType, ntn);
            if (updated == 0) {
                return ResponseEntity.badRequest().body("Parent record not found.");
            }

            // 2. Update sub-records
            if (subRecords != null && !subRecords.isEmpty()) {
                for (UserSchema sub : subRecords) {
                    LocalDate subCnicIssueDate = sub.getCnicIssueDate();

                    // Try to update the existing sub-record
                    int subUpdated = userRepository.updateSubRecordById(
                            sub.getId(),
                            sub.getCnic(),
                            subCnicIssueDate,
                            sub.getMobile(),
                            sub.getRecordType(),
                            sub.getNtn()
                    );

                    // If update fails, optional: insert new sub-record
                    if (subUpdated == 0) {
                        sub.setSubAccountId(id); // Link to parent
                        userRepository.save(sub);
                    }
                }
            }

            return ResponseEntity.ok("Parent and sub-records updated");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }





    public ResponseEntity<String> resetStatus(Long id, String cnic) {
        try {

            // 1. Update parent record
            int updated = userRepository.resetStatusByCnic("P", cnic); // Use a valid value like "A"

            if (updated == 0) {
                return ResponseEntity.ok("cnic not found");
            }

            return ResponseEntity.ok("status updated");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }



    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }



}
