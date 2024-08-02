package uz.cherevichenko.Timesheet.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cherevichenko.Timesheet.model.Project;
import uz.cherevichenko.Timesheet.model.Timesheet;
import uz.cherevichenko.Timesheet.page.ProjectPageDto;
import uz.cherevichenko.Timesheet.page.TimesheetsPageDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectPageService {
    private final ProjectService projectService;

    public Optional<ProjectPageDto> findById(Long id) {
        return projectService.getById(id)
                .map(this::convert);
    }
    private ProjectPageDto convert(Project project){

        ProjectPageDto projectPageDto = new ProjectPageDto();
        projectPageDto.setId(String.valueOf(project.getId()));
        projectPageDto.setName(project.getName());
        return   projectPageDto;

    }
    public List<ProjectPageDto> findAll() {
        return projectService.getAll().stream().map(this::convert).toList();
    }

}
