package uz.cherevichenko.Timesheet.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cherevichenko.Timesheet.model.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee,Long> {

}
