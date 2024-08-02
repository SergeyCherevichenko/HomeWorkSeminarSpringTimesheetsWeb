package uz.cherevichenko.Timesheet.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import uz.cherevichenko.Timesheet.model.Project;
import uz.cherevichenko.Timesheet.repository.ProjectRepository;
import uz.cherevichenko.Timesheet.service.ProjectService;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureWebTestClient
class ProjectControllerTest {

  @LocalServerPort
  private  int port;


  private  RestClient restClient;

    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
       restClient = RestClient.create("http://localhost:" + port);
    }


//    @Autowired
//    WebTestClient webTestClient;

    @Test
    void testGetById() {
        Project project = new Project();
        project.setName("projectName");
        Project expected = projectRepository.save(project);

//        ResponseEntity<Project> entity = WebClient.create().get()
//                .uri()
//                .retrieve()
//                .toEntity(Project.class)
//                .block();
//
//    webTestClient.get()
//            .uri("/projects/" + expected.getId())
//            .exchange()
//            .expectStatus().isOk()
//            .expectBody(Project.class)
//            .value(actual -> {
//
//                assertEquals(expected.getId(), actual.getId());
//                assertEquals(expected.getName(), actual.getName());
//
//            });

//
        ResponseEntity<Project> actual= restClient.get()
                .uri("/projects/" + expected.getId())
                .retrieve()
                .toEntity(Project.class);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Project responseBody = actual.getBody();
        assertNotNull(responseBody);
        assertEquals(expected.getId(), responseBody.getId());
        assertEquals(expected.getName(), responseBody.getName());
    }

  @Test
  void testByIdNotFound() {
      Assertions.assertThrows(HttpClientErrorException.NotFound.class,() ->{
          ResponseEntity<Void> actual =  restClient.get()
                  .uri("/projects/-2")
                  .retrieve()
                  .toBodilessEntity();
      });

       // assertEquals(HttpStatus.NOT_FOUND,actual.getStatusCode());


  }
  @Test
    void testToCreate() {
      Project toCreate = new Project();
      toCreate.setName("testName");

      ResponseEntity<Project> response = restClient.post()
              .uri("/projects")
              .body(toCreate)
              .retrieve()
              .toEntity(Project.class);
      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      Project responseBody = response.getBody();
      assertNotNull(responseBody);
      assertEquals(responseBody.getName(), toCreate.getName());
      assertNotNull(responseBody.getId());

      assertTrue(projectRepository.existsById(responseBody.getId()));
  }

      @Test
      void testToDeleteById(){
          Project project = new Project();
          project.setName("testName");
          Project expected = projectRepository.save(project);

          ResponseEntity<Void> responses = restClient.delete()
                 .uri("/projects/" + expected.getId())
                 .retrieve()
                 .toBodilessEntity();

          assertEquals(HttpStatus.NO_CONTENT, responses.getStatusCode());

          assertFalse(projectRepository.existsById(project.getId()));
      }

  }
