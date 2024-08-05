package uz.cherevichenko.aspect.logging;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
@Slf4j
@Aspect
@Component
public class LoggingAspectSeminar {

    private final LoggingAspectSeminarProperties properties;
    public LoggingAspectSeminar(LoggingAspectSeminarProperties properties) {
        this.properties = properties;
        log.info("LoggingAspectSeminar initialized with log level: {}", properties.getLevel());
    }

    @Pointcut("@annotation(uz.cherevichenko.aspect.logging.Logging)")
    public void loggingMethodsPointcut(){}

    @Pointcut("@within(uz.cherevichenko.aspect.logging.Logging)")
    public void loggingTypesPointcut(){}

    @Around(value = "loggingMethodsPointcut() || loggingTypesPointcut()")
    public Object loggingMethod(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        Object[] args = pjp.getArgs();
        StringBuilder argTypes = new StringBuilder();
        StringBuilder argValues = new StringBuilder();

        // Перебираем все аргументы метода.
        for (Object arg : args) {
            if (arg != null) {
                // Получаем тип аргумента.
                argTypes.append(arg.getClass().getSimpleName()).append(", ");
                argValues.append(arg.toString()).append(", ");
            }
        }

        String logBeforeMessage = "Before -> TimesheetService#" + methodName;
        String logAfterMessage = "After -> TimesheetService#" + methodName;

        if (properties.isPrintArgs()) {
            logBeforeMessage += ", types = " + argTypes + "values = " + argValues;
            logAfterMessage += ", types = " + argTypes + "values = " + argValues;
        }

        log.atLevel(properties.getLevel()).log(logBeforeMessage);

        try {
            return pjp.proceed();
        } finally {
            log.atLevel(properties.getLevel()).log(logAfterMessage);
        }
    }

}
