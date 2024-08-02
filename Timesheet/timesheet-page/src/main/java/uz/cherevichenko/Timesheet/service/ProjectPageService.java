package uz.cherevichenko.Timesheet.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import uz.cherevichenko.Timesheet.client.ProjectResource;
import uz.cherevichenko.Timesheet.page.ProjectPageDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProjectPageService {
    private final RestClient restClient;

    public ProjectPageService() {
        this.restClient = RestClient.create("http://localhost:8080");
    }

    public Optional<ProjectPageDto> findById(Long id) {
        try {
            ProjectResource project = restClient.get()
                    .uri("/projects/" + id)
                    .retrieve()
                    .body(ProjectResource.class);
            ProjectPageDto projectPageDto = new ProjectPageDto();
            projectPageDto.setId(String.valueOf(project.getId()));
            projectPageDto.setName(project.getName());

            return Optional.of(projectPageDto);
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        }
    }


    public List<ProjectPageDto> findAll() {
    List<ProjectResource> projects = restClient.get()
            .uri("/projects")
            .retrieve()
            .body(new ParameterizedTypeReference<List<ProjectResource>>() {});
        List<ProjectPageDto> result = new ArrayList<>();
        for (ProjectResource project : projects) {
            ProjectPageDto projectPageDto = new ProjectPageDto();
            projectPageDto.setId(String.valueOf(project.getId()));
            projectPageDto.setName(project.getName());
            result.add(projectPageDto);
        }
        return  result;
    }

}

