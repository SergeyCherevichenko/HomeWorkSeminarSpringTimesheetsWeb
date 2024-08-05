package uz.cherevichenko.Timesheet.service;

import org.springframework.stereotype.Service;
import uz.cherevichenko.Timesheet.aspect.Recover;
import uz.cherevichenko.Timesheet.aspect.Timer;
import uz.cherevichenko.Timesheet.model.Timesheet;
import uz.cherevichenko.Timesheet.repository.TimesheetRepository;
import uz.cherevichenko.aspect.logging.Logging;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service // то же самое, что и Component
@Timer
    public class TimesheetService {

        private final TimesheetRepository repository;

        public TimesheetService(TimesheetRepository repository) {
            this.repository = repository;
        }


        @Recover
        public List<Timesheet> getAll() {
            //throw new RuntimeException("Test exception in getAll"); для проверки recover
            return repository.findAll();
        }
       // @Timer
        @Recover(noRecoverFor = RuntimeException.class)
        public Optional<Timesheet> getById(Long id) {
           // throw new RuntimeException("Test exception in getById"); для проверки recover
            return repository.findById(id);
        }

        public Timesheet create(Timesheet timesheet) {
            return repository.save(timesheet);
        }

       // @Timer(enabled = false)
        public void delete(Long id) {
            repository.deleteById(id);
        }

    public List<Timesheet> getAllTimesheetsAfterDateTime(LocalDateTime createdAt){
            return repository.findAllByCreatedAtAfter(createdAt);
    }
    public List<Timesheet> getAllTimesheetsBeforeDateTime(LocalDateTime createdAt){
            return repository.findAllByCreatedAtBefore(createdAt);
    }
    @Recover
    public List<Timesheet> getAllTimesheetByProjectId(Long projectId){
        //throw new RuntimeException("Test exception in getAllTimesheetByProjectId"); для проверки recover
            return repository.getAllTimesheetByProjectId(projectId);
    }


    }



