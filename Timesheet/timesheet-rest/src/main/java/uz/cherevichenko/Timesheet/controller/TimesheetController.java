package uz.cherevichenko.Timesheet.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cherevichenko.Timesheet.model.Timesheet;
import uz.cherevichenko.Timesheet.myexception.ResourceNotFoundException;
import uz.cherevichenko.Timesheet.service.TimesheetService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/timesheets")
@Tag(name =  "Timesheets",description = "Api для работы с записями(распределение рабочего времени")
public class TimesheetController {
    // GET - получить - не содержит тела
    // POST - create
    // PUT - изменение
    // PATCH - изменение
    // DELETE - удаление

    // @GetMapping("/timesheets/{id}") // получить конкретную запись по идентификатору
    // @DeleteMapping("/timesheets/{id}") // удалить конкретную запись по идентификатору
    // @PutMapping("/timesheets/{id}") // обновить конкретную запись по идентификатору

    private final TimesheetService service;


    public TimesheetController(TimesheetService service) {
        this.service = service;
    }

    // /timesheets/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Get timesheet",
            description = "Получить запись по ее идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "404", content = @Content(schema = @Schema(implementation = Void.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })
    public ResponseEntity<Timesheet> get(@PathVariable @Parameter(description = "Идентификатор записи") Long id) {
        try {
            Optional<Timesheet> ts = service.getById(id);
            if (ts.isPresent()) {
                return ResponseEntity.ok(ts.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping // получить все
    @Operation(summary = "Get all timesheets", description = "Получить все записи")
    public ResponseEntity<List<Timesheet>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping // создание нового ресурса
    @Operation(summary = "Create timesheet", description = "Создать новую запись")
    public ResponseEntity<Timesheet> create(@RequestBody @Parameter(description = "новая запись") Timesheet timesheet) {
            timesheet = service.create(timesheet);

            // 201 Created
            return ResponseEntity.status(HttpStatus.CREATED).body(timesheet);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete timesheet", description = "Удалить запись по ее индентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внуренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })

    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "Идентификатор записи") Long id) {
        service.delete(id);

        // 204 No Content
        return ResponseEntity.notFound().build();
    }

    // ресурс /timesheets?created_at_after={date}
    @GetMapping("/createdAtAfter/{date}")
    @Operation(summary = "Get all timesheets after given date", description = "Получить все записи, созданные после указанной даты",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внуренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })

    public ResponseEntity<List<Timesheet>> getAllTimesheetsAfterDate(@PathVariable @Parameter(description = "передать дынные в формате YYYY-mm-dd HH:mm") LocalDateTime date) {
          List<Timesheet> timesheets = service.getAllTimesheetsAfterDateTime(date);
          if (timesheets.isEmpty()) {
              return ResponseEntity.notFound().build();
          }
          return ResponseEntity.ok().body(timesheets);

}


        // ресурс /timesheets?createdAtBefore=2024-07-04
        @GetMapping("/createdAtBefore/{date}")
        @Operation(summary = "Get all timesheets before given date", description = "Получить все записи, созданные до указанной даты",
                responses = {
                        @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                        @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                        @ApiResponse(description = "Внуренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
                })

    public ResponseEntity<List<Timesheet>> getAllTimesheetsBeforeDateTime(@PathVariable @Parameter(description = "передать дынные в формате YYYY-mm-dd HH:mm")LocalDateTime date) {
        try {
            List<Timesheet> timesheets = service.getAllTimesheetsBeforeDateTime(date);
            if (timesheets.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(timesheets);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



}



