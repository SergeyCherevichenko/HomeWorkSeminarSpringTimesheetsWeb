package uz.cherevichenko.Timesheet.controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import uz.cherevichenko.Timesheet.model.Employee;
import uz.cherevichenko.Timesheet.model.Project;
import uz.cherevichenko.Timesheet.model.Timesheet;
import uz.cherevichenko.Timesheet.repository.EmployeeRepository;
import uz.cherevichenko.Timesheet.repository.ProjectRepository;
import uz.cherevichenko.Timesheet.repository.TimesheetRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("bd-1")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimesheetControllerTest {

    @LocalServerPort
    private  int port;

    private RestClient restClient;

    @Autowired
    TimesheetRepository timesheetRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        restClient = RestClient.create("http://localhost:" + port);
    }

    @Test
    void testGetTimesheetById() {

            // Создание и сохранение Project
            Project project1 = new Project();
            project1.setName("project1");

            // Создание и сохранение Employee
            Employee employee1 = new Employee();
            employee1.setName("employee1");
            employee1.setLastName("employeeLastName1");
            employee1.setProjects(List.of(project1));

            projectRepository.save(project1);
            employeeRepository.save(employee1);

            // Создание и сохранение Timesheet с установленными связями
            Timesheet timesheet1 = new Timesheet();
            timesheet1.setMinutes(100);
            timesheet1.setCreatedAt(LocalDateTime.of(2024, 8, 1, 19, 0));
            timesheet1.setProject(project1);
            timesheet1.setEmployee(employee1);
            Timesheet savedTimesheet = timesheetRepository.save(timesheet1);

            //  запрос и проверка результат
            ResponseEntity<Timesheet> response = restClient.get()
                    .uri("/timesheets/" + savedTimesheet.getId())
                    .retrieve()
                    .toEntity(Timesheet.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            Timesheet responseBody = response.getBody();
            assertNotNull(responseBody);
            assertEquals(savedTimesheet.getId(), responseBody.getId());
            assertEquals(savedTimesheet.getMinutes(), responseBody.getMinutes());
            assertEquals(savedTimesheet.getCreatedAt(), responseBody.getCreatedAt());
            assertEquals(savedTimesheet.getProject().getId(), responseBody.getProject().getId());
            assertEquals(savedTimesheet.getEmployee().getId(), responseBody.getEmployee().getId());
        }


    @Test
    void testGetAllTimesheet() {
        // создание и заполнение project
        Project project1 = new Project();
        project1.setName("project1");
        Project project2 = new Project();
        project2.setName("project2");

        // Создание и сохранение Employee
        Employee employee1 = new Employee();
        employee1.setName("employee1");
        employee1.setLastName("employeeLastName1");
        employee1.setProjects(List.of(project1,project2));

        projectRepository.save(project1);
        projectRepository.save(project2);
        employeeRepository.save(employee1);

        // Создание и сохранение Timesheet с установленными связями
        Timesheet timesheet1 = new Timesheet();
        timesheet1.setMinutes(100);
        timesheet1.setCreatedAt(LocalDateTime.of(2024, 8, 1, 19, 0));
        timesheet1.setProject(project1);
        timesheet1.setEmployee(employee1);
        timesheetRepository.save(timesheet1);
        Timesheet timesheet2 = new Timesheet();
        timesheet2.setMinutes(150);
        timesheet2.setCreatedAt(LocalDateTime.of(2024, 8, 2, 19, 0));
        timesheet2.setProject(project2);
        timesheet2.setEmployee(employee1);
        timesheetRepository.save(timesheet2);

        List<Timesheet> expected = timesheetRepository.findAll();
        // timesheetRepository.deleteAll();  для проверки если списки не равны



        //  запрос и проверьте результат
        ResponseEntity<List<Timesheet>> response = restClient.get()
                .uri("/timesheets")
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<Timesheet>>() {
                });

        List<Timesheet> responses = response.getBody();

        assertNotNull(responses);
        assertEquals(responses.size(), expected.size());



}

    @Test
    void testCreateTimesheet() {
        // создание и заполнение project
        Project project1 = new Project();
        project1.setName("project1");
        Project project2 = new Project();
        project2.setName("project2");

        // Создание и сохранение Employee
        Employee employee1 = new Employee();
        employee1.setName("employee1");
        employee1.setLastName("employeeLastName1");
        employee1.setProjects(List.of(project1,project2));

        projectRepository.save(project1);
        projectRepository.save(project2);
        employeeRepository.save(employee1);

        // Создание и сохранение Timesheet с установленными связями

        Timesheet timesheet2 = new Timesheet();
        timesheet2.setMinutes(150);
        timesheet2.setCreatedAt(LocalDateTime.of(2024, 8, 2, 19, 0));
        timesheet2.setProject(project1);
        timesheet2.setProject(project2);
        timesheet2.setEmployee(employee1);


        //  запрос и проверьте результат
        ResponseEntity<Timesheet> response = restClient.post()
                .uri("/timesheets")
                .body(timesheet2)
                .retrieve()
                .toEntity(Timesheet.class);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Timesheet responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(responseBody.getProject(), timesheet2.getProject());
        assertEquals(responseBody.getEmployee(), timesheet2.getEmployee());
        assertEquals(responseBody.getMinutes(), timesheet2.getMinutes());
        assertEquals(responseBody.getCreatedAt(), timesheet2.getCreatedAt());
        assertNotNull(responseBody.getId());
        assertTrue(timesheetRepository.existsById(responseBody.getId()));

}

    @Test
    void testDeleteTimesheetById() {
        Project project1 = new Project();
        project1.setName("project1");

        // Создание и сохранение Employee
        Employee employee1 = new Employee();
        employee1.setName("employee1");
        employee1.setLastName("employeeLastName1");
        employee1.setProjects(List.of(project1));
        projectRepository.save(project1);
        employeeRepository.save(employee1);

        // Создание и сохранение Timesheet с установленными связями
        Timesheet timesheet1 = new Timesheet();
        timesheet1.setMinutes(100);
        timesheet1.setCreatedAt(LocalDateTime.of(2024, 8, 1, 19, 0));
        timesheet1.setProject(project1);
        timesheet1.setEmployee(employee1);
        Timesheet savedTimesheet = timesheetRepository.save(timesheet1);



        //  запрос и проверка результат
        ResponseEntity<Void> response = restClient.delete()
                .uri("/timesheets/" + savedTimesheet.getId())
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        assertFalse(timesheetRepository.existsById(timesheet1.getId()));

    }


    @Test
    void testUpdateTimesheet() {
        // Создание и сохранение Project
        Project project1 = new Project();
        project1.setName("project1");
        Project project2 = new Project();
        project2.setName("project2");

        projectRepository.save(project1);
        projectRepository.save(project2);

        // Создание и сохранение Employee
        Employee employee1 = new Employee();
        employee1.setName("employee1");
        employee1.setLastName("employeeLastName1");
        employee1.setProjects(List.of(project1, project2));
        employeeRepository.save(employee1);

        // Создание и сохранение Timesheet
        Timesheet timesheet = new Timesheet();
        timesheet.setMinutes(100);
        timesheet.setCreatedAt(LocalDateTime.of(2024, 8, 1, 19, 0));
        timesheet.setProject(project1);
        timesheet.setEmployee(employee1);
        Timesheet savedTimesheet = timesheetRepository.save(timesheet);

        // Изменение данных для обновления
        Timesheet updatedTimesheet = new Timesheet();
        updatedTimesheet.setMinutes(150);
        updatedTimesheet.setCreatedAt(LocalDateTime.of(2024, 8, 2, 19, 0));
        updatedTimesheet.setProject(project2);
        updatedTimesheet.setEmployee(employee1);

        // Выполнение запроса PUT для обновления Timesheet
        ResponseEntity<Timesheet> response = restClient.put()
                .uri("/timesheets/" + savedTimesheet.getId())
                .body(updatedTimesheet)
                .retrieve()
                .toEntity(Timesheet.class);

        // Проверка ответа
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Timesheet responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(savedTimesheet.getId(), responseBody.getId());
        assertEquals(updatedTimesheet.getMinutes(), responseBody.getMinutes());
        assertEquals(updatedTimesheet.getCreatedAt(), responseBody.getCreatedAt());
        assertEquals(updatedTimesheet.getProject().getId(), responseBody.getProject().getId());
        assertEquals(updatedTimesheet.getEmployee().getId(), responseBody.getEmployee().getId());
    }
    }

