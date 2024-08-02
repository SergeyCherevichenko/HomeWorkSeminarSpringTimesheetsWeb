package uz.cherevichenko.Timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.cherevichenko.Timesheet.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {

}
