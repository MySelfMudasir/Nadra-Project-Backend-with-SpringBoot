package com.example.SpringBootCRUD.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "temp_mudasir")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "temp_mudasir_seq")
    @SequenceGenerator(name = "temp_mudasir_seq", sequenceName = "temp_mudasir_seq", allocationSize = 1)

    private long id;
    private String username;
    private String email;
}
