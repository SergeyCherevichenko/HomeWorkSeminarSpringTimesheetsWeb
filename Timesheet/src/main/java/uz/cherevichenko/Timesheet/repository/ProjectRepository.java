package uz.cherevichenko.Timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cherevichenko.Timesheet.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
}
