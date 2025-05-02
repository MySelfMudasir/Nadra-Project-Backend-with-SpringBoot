package com.example.SpringBootCRUD.controller;

import com.example.SpringBootCRUD.model.Employee;
import com.example.SpringBootCRUD.repository.EmployeeRepo;
import com.example.SpringBootCRUD.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/myapp")
public class EmployeeController {

    @Autowired
    private EmployeeRepo repo;


    @PostMapping("/save")
    public ResponseEntity<String> addEmployee(@RequestBody Employee employee) {
        try {
            repo.save(employee);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Added Failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Added Successfully", HttpStatus.OK);
    }


    @GetMapping("/get")
    public ResponseEntity<List<Employee>> getEmployee() {
        List<Employee> list;
        try {
            list = repo.findAll();
            if(list.isEmpty()) {
                return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<List<Employee>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Employee>>(list, HttpStatus.OK);
    }


    @PostMapping("/get/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        try {
            Employee employee = repo.findById(id).orElse(null);
            if (employee == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        try {
            Employee existingEmployee = repo.findById(id).orElse(null);
            if (existingEmployee == null) {
                return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);
            }
            existingEmployee.setUsername(employee.getUsername());
            existingEmployee.setEmail(employee.getEmail());
            repo.save(existingEmployee);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error occurred while updating", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Updated Successfully", HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id) {
        try {
            if(!repo.existsById(id)) {
                return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);
            }
            repo.deleteById(id); // Assuming the ID is a Long type
        } catch (Exception e) {
            return new ResponseEntity<String>("Error occurred while deleting", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("Deleted Successfully", HttpStatus.OK);
    }


    @GetMapping("/test")
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("API working!");
    }





}
