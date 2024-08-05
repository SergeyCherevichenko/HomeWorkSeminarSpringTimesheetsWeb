package uz.cherevichenko.aspect.logging;

import lombok.Data;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.event.Level;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("application.logging")
public class LoggingAspectSeminarProperties {

    private Level level = Level.DEBUG;
    private boolean printArgs = false;
}
