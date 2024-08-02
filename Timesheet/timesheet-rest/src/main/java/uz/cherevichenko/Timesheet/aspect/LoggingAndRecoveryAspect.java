package uz.cherevichenko.Timesheet.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAndRecoveryAspect {

    @Pointcut("@annotation(uz.cherevichenko.Timesheet.aspect.Recover)")
    public void recoverablePointcut() {}

    @Around("recoverablePointcut() && @annotation(recover)")
    public Object aroundRecoverableMethods(ProceedingJoinPoint pjp, Recover recover) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            // Если происходит исключение, проверяем, можно ли обработать его согласно аннотации @Recover.
            if (shouldRecover(ex, recover.noRecoverFor())) {
                logException(pjp, ex); // Логгируем информацию об исключении.
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature(); // Получаем информацию о методе, вызвавшем исключение.
                Class<?> returnType = methodSignature.getReturnType(); // Получаем тип возвращаемого значения метода.
                return getDefaultValue(returnType); // Возвращаем значение по умолчанию для этого типа.
            } else {
                // Если исключение не подлежит обработке, пробрасываем его дальше.
                throw ex;
            }
        }
    }

    private boolean shouldRecover(Throwable ex, Class<?>[] noRecoverFor) {
        // Проверяем, не относится ли исключение к одному из классов в noRecoverFor. Если исключение не в списке, возвращаем true.
        return Arrays.stream(noRecoverFor).noneMatch(exceptionClass -> exceptionClass.isAssignableFrom(ex.getClass()));
    }

    private void logException(ProceedingJoinPoint pjp, Throwable ex) {
        // Логгируем информацию об исключении, используя форматирование сообщения.
        log.warn("Recovering {} after Exception[{}, \"{}\"]", pjp.getSignature(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    private Object getDefaultValue(Class<?> returnType) {
        // Возвращаем значение по умолчанию в зависимости от типа возвращаемого значения метода.
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class) return false; // Значение по умолчанию для boolean.
            if (returnType == byte.class) return (byte) 0; // Значение по умолчанию для byte.
            if (returnType == short.class) return (short) 0; // Значение по умолчанию для short.
            if (returnType == int.class) return 0; // Значение по умолчанию для int.
            if (returnType == long.class) return 0L; // Значение по умолчанию для long.
            if (returnType == float.class) return 0.0f; // Значение по умолчанию для float.
            if (returnType == double.class) return 0.0; // Значение по умолчанию для double.
            if (returnType == char.class) return '\u0000'; // Значение по умолчанию для char.
        }
        return null; // Для ссылочных типов возвращаем null.
    }
}
