package uz.cherevichenko.aspect.recover;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAndRecoveryAspect {

    private final LoggingAndRecoveryAspectProperties properties;

    @Pointcut("@annotation(uz.cherevichenko.aspect.recover.Recover)")
    public void recoverablePointcut() {}

    @Around("recoverablePointcut() && @annotation(recover)")
    public Object aroundRecoverableMethods(ProceedingJoinPoint pjp, Recover recover) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            if (shouldRecover(ex, recover.noRecoverFor())) {
                logException(pjp, ex); // Логгируем информацию об исключении.
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature(); // Получаем информацию о методе.
                Class<?> returnType = methodSignature.getReturnType(); // Получаем тип возвращаемого значения метода.
                return getDefaultValue(returnType); // Возвращаем значение по умолчанию для этого типа.
            } else {
                // Если исключение не подлежит обработке, пробрасываем его дальше.
                throw ex;
            }
        }
    }

    private boolean shouldRecover(Throwable ex, Class<?>[] noRecoverFor) {
        // Проверяем, не относится ли исключение к одному из классов в noRecoverFor
        return Arrays.stream(noRecoverFor)
                .noneMatch(exceptionClass -> exceptionClass.isAssignableFrom(ex.getClass()));
    }

    private void logException(ProceedingJoinPoint pjp, Throwable ex) {
        // Логгируем информацию об исключении.
        log.warn("Recovering {} after Exception[{}, \"{}\"]", pjp.getSignature(), ex.getClass().getSimpleName(), ex.getMessage());
    }

    private Object getDefaultValue(Class<?> returnType) {
        // Возвращаем значение по умолчанию в зависимости от типа возвращаемого значения метода.
        if (returnType.isPrimitive()) {
            if (returnType == boolean.class) return false;
            if (returnType == byte.class) return (byte) 0;
            if (returnType == short.class) return (short) 0;
            if (returnType == int.class) return 0;
            if (returnType == long.class) return 0L;
            if (returnType == float.class) return 0.0f;
            if (returnType == double.class) return 0.0;
            if (returnType == char.class) return '\u0000';
        }
        return null; // Для ссылочных типов возвращаем null.
    }
}
