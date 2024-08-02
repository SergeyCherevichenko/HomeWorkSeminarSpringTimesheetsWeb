package uz.cherevichenko.Timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.cherevichenko.Timesheet.model.Timesheet;

import java.time.LocalDateTime;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet,Long> {
    List<Timesheet> getAllTimesheetByProjectId(Long projectId);
    List<Timesheet> findAllByCreatedAtAfter(LocalDateTime createdAt);
    List<Timesheet> findAllByCreatedAtBefore(LocalDateTime createdAt);
    List<Timesheet> findAllTimesheetsByEmployeeId(Long employeeId);
}
