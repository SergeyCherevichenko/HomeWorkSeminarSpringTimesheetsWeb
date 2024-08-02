package uz.cherevichenko.Timesheet.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.cherevichenko.Timesheet.service.TimesheetPageService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/timesheets")
@RequiredArgsConstructor
public class TimesheetPageController {

    private final TimesheetPageService timesheetPageService;

    @GetMapping
    public String getAllTimesheets(Model model) {
        List<TimesheetsPageDto> timesheets = timesheetPageService.findAll();
        model.addAttribute("timesheets", timesheets);
        return "timesheets-page.html";
    }

    @GetMapping("/{id}")
    public String getTimesheetPage(@PathVariable Long id, Model model) {
        Optional<TimesheetsPageDto> timesheetOpt = timesheetPageService.findByID(id);
        if (timesheetOpt.isEmpty()) {
            return "not-found.html";
        }
        model.addAttribute("timesheet", timesheetOpt.get());
        return "timesheet-page.html";
    }

}