package com.example.SpringBootCRUD.repository;

import com.example.SpringBootCRUD.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    List<Employee> id(long id);
}
