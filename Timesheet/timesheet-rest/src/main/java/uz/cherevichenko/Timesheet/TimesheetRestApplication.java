package uz.cherevichenko.Timesheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import uz.cherevichenko.Timesheet.model.*;
import uz.cherevichenko.Timesheet.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
@EnableDiscoveryClient
@SpringBootApplication
public class TimesheetRestApplication {

	public static void main(String[] args) {
		//SpringApplication.run(TimesheetRestApplication.class);

		ApplicationContext ctx =  SpringApplication.run(TimesheetRestApplication.class, args);
		TimesheetRepository timesheetRepository = ctx.getBean(TimesheetRepository.class);
		ProjectRepository projectRepository = ctx.getBean(ProjectRepository.class);
		EmployeeRepository employeeRepository = ctx.getBean(EmployeeRepository.class);
		//UserRepository userRepository = ctx.getBean(UserRepository.class);
		//RoleRepository roleRepository = ctx.getBean(RoleRepository.class);

//		Role adminRole = new Role();
//		adminRole.setName(RoleName.ADMIN);
//		roleRepository.save(adminRole);
//		System.out.println(adminRole);
//
//
//		Role userRole = new Role();
//		userRole.setName(RoleName.USER);
//		roleRepository.save(userRole);
//		System.out.println(userRole);


//		Role restRole = new Role();
//		restRole.setName(RoleName.REST);
//		roleRepository.save(restRole);
//		System.out.println(restRole);


//		User admin = new User();
//		admin.setLogin("admin");
//		admin.setPassword("$2a$12$5sQpgBwSAL/t23plqYrNlO3mGF4jc6ik1DIc524Wn.QpAtYEoYgH6");
//		admin.setRoles(List.of(adminRole,userRole));
//		userRepository.save(admin);
//		System.out.println(admin);
//
//		User user = new User();
//		user.setLogin("user");
//		user.setPassword("$2a$12$vVncPiR0TR.fliqHqlFFT.yANXgmbMZ2cxv15GYiDY60Y.NETAu1O");
//		user.setRoles(List.of(userRole));
//		userRepository.save(user);
//		System.out.println(user);
//
//		User anonymous = new User();
//		anonymous.setLogin("anon");
//		anonymous.setPassword("$2a$12$v37dtg2jAVxfG9hivwAm5.UclNUqYD9rFnd4a7Hhi10u7YA5f1tPm");
//		userRepository.save(anonymous);
//		System.out.println(anonymous);

//		User rest = new User();
//		rest.setLogin("rest");
//		rest.setPassword("$2a$12$sZsE87PUH2zBd8RkK7SbYeVY/VxPyKCabB0D75r3WQ1m.T9ZCYczK");//rest
//		rest.setRoles(List.of(restRole));
//		userRepository.save(rest);
//
//		System.out.println(rest);

		for (int i = 1; i < 11; i++) {
			Employee employee = new Employee();
			employee.setName("Employee name # " + i);
			employee.setLastName("Employee lastName # " + i);
			employeeRepository.save(employee);
		}


		for (int i = 1; i < 11; i++) {
			Project project = new Project();
            project.setName("Project " + i);
            projectRepository.save(project);

		}

		LocalDateTime createdAt = LocalDateTime.now();
		for (int i = 1; i < 11; i++) {
			createdAt = createdAt.plusDays(1);
			Timesheet timesheet = new Timesheet();
			timesheet.setCreatedAt(createdAt);
			timesheet.setProject(projectRepository.getReferenceById(1L));
			timesheet.setMinutes( ThreadLocalRandom.current().nextInt(100,1000));
			timesheet.setEmployee(employeeRepository.getReferenceById((ThreadLocalRandom.current().nextLong(1,11))));
			timesheetRepository.save(timesheet);

		}



	}


}


