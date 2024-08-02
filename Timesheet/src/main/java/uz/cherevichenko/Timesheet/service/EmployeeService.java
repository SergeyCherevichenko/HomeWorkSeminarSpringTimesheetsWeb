package uz.cherevichenko.Timesheet.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cherevichenko.Timesheet.model.Employee;
import uz.cherevichenko.Timesheet.model.Timesheet;
import uz.cherevichenko.Timesheet.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private  final EmployeeRepository employeeRepository;


    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
    public Optional<Employee> getById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

}
