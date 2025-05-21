package com.example.SpringBootCRUD.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Entity
@Table(name = "verisys_management")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSchema {

    @Id
    // Use sequence or identity based on Oracle setup (see earlier advice)
    private Long id;

    @Column(name = "SUB_ACCOUNT_ID")
    private Long subAccountId;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    private String cnic;

    @Column(name = "CNIC_ISSUE_DATE")
    private LocalDate cnicIssueDate;

    private String email;
    private String mobile;

    @Column(name = "RECORD_TYPE")
    private String recordType;


    @Column(name = "STATUS")
    private String status;




    @Transient
    private List<UserSchema> subRecords;
}
