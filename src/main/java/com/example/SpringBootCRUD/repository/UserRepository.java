package com.example.SpringBootCRUD.repository;

import com.example.SpringBootCRUD.model.UserSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<UserSchema, Long> {
    List<UserSchema> id(long id);


    // Custom query to fetch the next ID
    @Query(value = "SELECT NVL(MAX(ID), 0) + 1 FROM verisys_management", nativeQuery = true)
    Long getNextManagementId();


    //findByRecordType
    @Query(value = "SELECT * FROM verisys_management WHERE RECORD_TYPE = 'primary' ", nativeQuery = true)
    List<UserSchema> findByRecordTypeNative(String recordType);


    //findBySubAccountId
    @Query(value = "SELECT * FROM verisys_management WHERE SUB_ACCOUNT_ID = ?1", nativeQuery = true)
    List<UserSchema> findBySubAccountIdNative(Long subAccountId);


    //updateAccountById
    @Modifying
    @Transactional
    @Query("UPDATE UserSchema u SET u.email = :email, u.accountType = :accountType, u.cnic = :cnic, u.cnicIssueDate = :cnicIssueDate, u.mobile = :mobile, u.recordType = :recordType WHERE u.id = :id")
    int updateUserById(Long id, String email, String accountType, String cnic, LocalDate cnicIssueDate, String mobile, String recordType);

    // Sub-records might not have all fields, so we update what’s available
    @Modifying
    @Transactional
    @Query("UPDATE UserSchema u SET u.cnic = :cnic, u.cnicIssueDate = :cnicIssueDate, u.mobile = :mobile, u.recordType = :recordType WHERE u.id = :id")
    int updateSubRecordById(Long id, String cnic, LocalDate cnicIssueDate, String mobile, String recordType);




}
