package uz.cherevichenko.Timesheet.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import uz.cherevichenko.Timesheet.model.Project;
import uz.cherevichenko.Timesheet.repository.ProjectRepository;
import uz.cherevichenko.Timesheet.repository.TimesheetRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest()
@ActiveProfiles("test")
class ProjectServiceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TimesheetRepository timesheetRepository;

    @Autowired
    ProjectService projectService;

    @Test
    void getByIdEmpty() {
        assertFalse(projectRepository.existsById(2L));
        assertTrue(projectService.getById(2L).isEmpty());
    }
    @Test
    void getByIdPresent(){
        Project project = new Project();
        project.setName("projectName");
        project = projectRepository.save(project);
        Optional<Project> actual = projectService.getById(project.getId());
        assertTrue(actual.isPresent());
        assertEquals( actual.get().getId(), project.getId() );
        assertEquals( actual.get().getName(), project.getName() );


    }
}