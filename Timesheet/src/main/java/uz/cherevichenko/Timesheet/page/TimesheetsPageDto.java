package uz.cherevichenko.Timesheet.page;


import lombok.Data;

@Data
public class TimesheetsPageDto {

   private String id;
   private String projectId;
   private String minutes;
   private String createdAt;
   private String projectName;

}
