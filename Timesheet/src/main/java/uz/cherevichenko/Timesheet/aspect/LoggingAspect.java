package uz.cherevichenko.Timesheet.aspect;


import jakarta.persistence.Column;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* uz.cherevichenko.Timesheet.service.TimesheetService.*(..))")
    public void timesheetsServiceMethodsPointcut(){


    }

    @Before(value = "timesheetsServiceMethodsPointcut()")
    public void beforeTimesheetServiceFindById(JoinPoint jp) {
        // Получаем имя метода, который будет вызван.
        String methodName = jp.getSignature().getName();
        // Получаем аргументы метода.
        Object[] args = jp.getArgs();
        // Перебираем все аргументы метода.
        for (Object arg : args) {
            if (arg != null) {
                // Получаем тип аргумента.
                String argType = arg.getClass().getSimpleName();
                String argValue = arg.toString();

                log.info("Before -> TimesheetService#{}, type = {},id = {}", methodName, argType, argValue);
            }
        }
    }


    }
//    @AfterThrowing(value = "timesheetsServiceMethodsPointcut()",throwing = "ex")
//    public void afterTimesheetServiceFindById(JoinPoint jp,Exception ex){
//        String methodName = jp.getSignature().getName();
//        //Long id = (Long)jp.getArgs()[0];
//        log.info("AfterThrowing -> TimesheetService#{}",methodName,ex.getClass().getName());
//
//    }
//
//}
