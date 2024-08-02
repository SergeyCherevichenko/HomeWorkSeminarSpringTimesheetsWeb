package uz.cherevichenko.Timesheet.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "timesheet")
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private  Long id;
    private  Integer minutes;
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}

