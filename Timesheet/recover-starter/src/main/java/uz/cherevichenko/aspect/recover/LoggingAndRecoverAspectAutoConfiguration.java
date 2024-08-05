package uz.cherevichenko.aspect.recover;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingAndRecoveryAspectProperties.class)
@ConditionalOnProperty(value = "exception.logging.enabled", havingValue = "true")
public class LoggingAndRecoverAspectAutoConfiguration {

    @Bean
    LoggingAndRecoveryAspect loggingAndRecoveryAspect(LoggingAndRecoveryAspectProperties properties){
        return new LoggingAndRecoveryAspect(properties);
    }
}
