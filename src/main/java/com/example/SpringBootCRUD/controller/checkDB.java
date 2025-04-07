package com.example.SpringBootCRUD.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class checkDB {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/checkdb")
    public String checkDbConnection() {
        try {
            jdbcTemplate.execute("SELECT 1 FROM DUAL");  // Simple query to test the connection
            return "Database connection is successful!";
        } catch (Exception e) {
            return "Failed to connect to the database!";
        }
    }
}
