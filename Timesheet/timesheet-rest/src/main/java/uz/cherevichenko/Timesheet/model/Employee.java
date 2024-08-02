package uz.cherevichenko.Timesheet.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String lastName;
    @ManyToMany(mappedBy = "employees")
    private List<Project> projects = new ArrayList<>();
}


