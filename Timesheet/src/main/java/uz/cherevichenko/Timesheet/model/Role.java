package uz.cherevichenko.Timesheet.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name",unique = true,nullable = false)
    private RoleName name;

    @ToString.Exclude
    @ManyToMany( mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User> users;

}
