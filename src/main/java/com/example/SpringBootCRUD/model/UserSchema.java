package com.example.SpringBootCRUD.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "temp_mudasir")

public class UserSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "temp_mudasir_seq")
    @SequenceGenerator(name = "temp_mudasir_seq", sequenceName = "temp_mudasir_seq", allocationSize = 1)
    private Long id;


    private String username;
    private String email;
    private String address;
}
