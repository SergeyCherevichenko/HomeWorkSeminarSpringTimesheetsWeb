package uz.cherevichenko.Timesheet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cherevichenko.Timesheet.model.Project;
import uz.cherevichenko.Timesheet.model.Timesheet;
import uz.cherevichenko.Timesheet.page.TimesheetsPageDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimesheetPageService {
    private final TimesheetService timesheetService;
    private final ProjectService projectService;

    public Optional<TimesheetsPageDto> findByID(Long id) {
        return timesheetService.getById(id)
                .map(this::convert);

    }

    private TimesheetsPageDto convert(Timesheet timesheet){

        TimesheetsPageDto timesheetsPageDto = new TimesheetsPageDto();
        timesheetsPageDto.setId(String.valueOf(timesheet.getId()));
        timesheetsPageDto.setProjectId(String.valueOf(timesheet.getProject().getId()));
        timesheetsPageDto.setMinutes(String.valueOf(timesheet.getMinutes()));
        timesheetsPageDto.setCreatedAt(timesheet.getCreatedAt().toString());
        timesheetsPageDto.setProjectName(timesheet.getProject().getName().toString());
        return   timesheetsPageDto;

    }
public List<TimesheetsPageDto> findAll() {
        return timesheetService.getAll().stream().map(this::convert).toList();
}

}
