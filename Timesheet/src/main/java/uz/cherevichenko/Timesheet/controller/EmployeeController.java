package uz.cherevichenko.Timesheet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cherevichenko.Timesheet.model.Employee;
import uz.cherevichenko.Timesheet.model.Timesheet;
import uz.cherevichenko.Timesheet.repository.TimesheetRepository;
import uz.cherevichenko.Timesheet.service.EmployeeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
@Tag(name = "employees",description = "Api для работы с сотрудником")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final TimesheetRepository timesheetRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Get Employee", description = "Получить сотрудника по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внуренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })

    public ResponseEntity<Employee> get(@PathVariable @Parameter(description = "идентификатор сотрудника") Long id) {
        Optional<Employee> ts = employeeService.getById(id);

        if (ts.isPresent()) {

            return ResponseEntity.status(HttpStatus.OK).body(ts.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @GetMapping // получить все
    @Operation(summary = "Get all employees", description = "Получить всех сотрудников")

    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee", description = "Удалить сотрудника по его идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внуренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })

    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "идентификатор сотрудника") Long id) {
        employeeService.delete(id);

        // 204 No Content
        return ResponseEntity.noContent().build();
    }
    @GetMapping("{id}/timesheets")
    @Operation(summary = "Get all timesheets by employee id", description = "Получить все записи сотрудника по его идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внуренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })

    public ResponseEntity<List<Timesheet>> findAllTimesheetByEmployeeId(@PathVariable @Parameter(description = "идентификатор сотрудника") Long id) {
        return ResponseEntity.ok(timesheetRepository.findAllTimesheetsByEmployeeId(id));
    }
}
