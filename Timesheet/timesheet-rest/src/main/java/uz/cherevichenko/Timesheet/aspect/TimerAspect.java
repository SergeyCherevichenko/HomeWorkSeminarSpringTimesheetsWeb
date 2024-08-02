package uz.cherevichenko.Timesheet.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class TimerAspect {

    @Pointcut("@annotation(uz.cherevichenko.Timesheet.aspect.Timer)")
    public void timerMethodsPointcut(){}

    @Pointcut("@within(uz.cherevichenko.Timesheet.aspect.Timer)")
    public void timerTypePointcut(){}

        @Around(value = "timerMethodsPointcut() || timerTypePointcut()")
        public Object aroundTimesheetsServiceMethods(ProceedingJoinPoint pjp) throws Throwable {
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Object target = pjp.getTarget();
            Method method = methodSignature.getMethod();

            boolean enabled = true;
            if(method.isAnnotationPresent(Timer.class)){
                enabled = method.getAnnotation(Timer.class).enabled();
            }else if(target.getClass().isAnnotationPresent(Timer.class)){
                enabled = target.getClass().getAnnotation(Timer.class).enabled();
            }

            if(!enabled){
                return pjp.proceed();
            }
            long start = System.currentTimeMillis();
            try {
                return pjp.proceed();
            }finally {
                long duration = System.currentTimeMillis() - start;
                log.info("TimesheetsService#{} duration  = {}ms",pjp.getSignature().getName(),duration);
            }

        }
    }


