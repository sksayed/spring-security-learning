package com.sayed.rahman.springsecurityclient.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "User")
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email", name = "email_unique_constraints"),
})
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String firstName;
    private String lastName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(length = 60)
    private String password;
    private String role;
    private boolean enabled = false;
}
