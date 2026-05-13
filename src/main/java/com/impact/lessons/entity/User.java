package com.impact.lessons.entity;
import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "password_hash", nullable = false) // ADAUGĂ ACEASTA
    private String passwordHash;
    @OneToOne(mappedBy = "user")
    private UserCredentials credentials;

    @OneToOne(mappedBy = "user")
    private UserPersonalData personalData;

    @OneToOne(mappedBy = "user")
    private UserStatus status;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserEmail> emails;

}
