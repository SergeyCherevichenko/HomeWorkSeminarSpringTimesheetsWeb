package uz.cherevichenko.Timesheet.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
// Класс для хранения информации о пользователе
public class User {

    @Id
    @GeneratedValue(strategy =  GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "login",unique = true,nullable = false)
    private String login;

    @Column(name = "password",nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
