package com.example.SpringBootCRUD.repository;

import com.example.SpringBootCRUD.model.UserSchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserSchema, Long> {
    List<UserSchema> id(long id);

}
