
package uz.cherevichenko.Timesheet.page;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.cherevichenko.Timesheet.service.ProjectPageService;


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectPageController {


    private final ProjectPageService projectPageService;

    @GetMapping
    public String getAllProjects(Model model) {
        List<ProjectPageDto> projects = projectPageService.findAll();
        model.addAttribute("projects", projects);
        return "projects-page.html";
    }
    @GetMapping("/{id}")
    public String getProjectPage(@PathVariable Long id, Model model) {
        Optional<ProjectPageDto> projectOpt = projectPageService.findById(id);

        model.addAttribute("project", projectOpt.get());
        return "project-page.html";
    }

}

