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
import org.springframework.web.client.HttpClientErrorException;
import uz.cherevichenko.Timesheet.model.Project;
import uz.cherevichenko.Timesheet.model.Timesheet;
import uz.cherevichenko.Timesheet.myexception.ResourceNotFoundException;
import uz.cherevichenko.Timesheet.service.ProjectService;
import uz.cherevichenko.Timesheet.service.TimesheetService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
@Tag(name = "Project",description = "Api для работы с проектами")
public class ProjectController {
    private  final ProjectService projectService;
    private final TimesheetService timesheetService;

    public ProjectController(ProjectService projectService, TimesheetService timesheetService) {
        this.projectService = projectService;
        this.timesheetService = timesheetService;
    }
    @GetMapping("/{id}") // получить все
    @Operation(summary = "getProject", description ="Получить все проекты по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внуренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })
    public ResponseEntity<Project> get(@PathVariable @Parameter(description = "идентификатор проекта") Long id) {
        Optional<Project> ts = projectService.getById(id);

        if (ts.isPresent()) {
//      return ResponseEntity.ok().body(ts.get());
            return ResponseEntity.status(HttpStatus.OK).body(ts.get());
        }

      throw new  ResourceNotFoundException(HttpStatus.NOT_FOUND,"Ресурс не найден");
    }

    @GetMapping // получить все
    @Operation(summary = "Get ALL projects",description = "Получить все проекты")
    public ResponseEntity<List<Project>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @PostMapping // создание нового ресурса
    @Operation(summary = "Post project",description = "Создать новый проект")
    public ResponseEntity<Project> create(@RequestBody @Parameter(description = "новый проект") Project project) {
        project = projectService.create(project);

        // 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Удалить проект по идентификатору",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внуренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })

    public ResponseEntity<Void> delete(@PathVariable @Parameter(description = "Идентификатор проекта") Long id) {
        projectService.delete(id);

        // 204 No Content
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/timesheets")
    @Operation(summary = "Get all timesheets by project id", description = "Получить все записи по идентификатору проекта",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200", content = @Content(schema = @Schema(implementation = Timesheet.class))),
                    @ApiResponse(description = "Запись не найдена", responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(description = "Внутренняя ошибка", responseCode = "500", content = @Content(schema = @Schema(implementation = Void.class)))
            })
    public ResponseEntity<List<Timesheet>> getAllTimesheetByProjectId(@PathVariable @Parameter(description = "Идентификатор проекта") Long projectId) {
        try {
            List<Timesheet> timesheets = timesheetService.getAllTimesheetByProjectId(projectId);
            if (timesheets == null || timesheets.isEmpty()) {
                // Возвращаем статус 204 No Content, если нет данных
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(timesheets);
        } catch (ResourceNotFoundException e) {
            // Возвращаем статус 404 Not Found, если ресурс не найден
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Возвращаем статус 500 Internal Server Error для других исключений
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


