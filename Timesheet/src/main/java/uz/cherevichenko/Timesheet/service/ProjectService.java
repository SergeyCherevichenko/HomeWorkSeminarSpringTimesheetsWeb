package uz.cherevichenko.Timesheet.service;


import org.springframework.stereotype.Service;
import uz.cherevichenko.Timesheet.model.Project;
import uz.cherevichenko.Timesheet.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectrepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectrepository = projectRepository;
    }
    public Optional<Project> getById(Long id) {
        return projectrepository.findById(id);
    }

    public List<Project> getAll() {
        return projectrepository.findAll();
    }

    public Project create(Project project) {
        return projectrepository.save(project);
    }


    public void delete(Long id) {
        projectrepository.deleteById(id);
    }
}

