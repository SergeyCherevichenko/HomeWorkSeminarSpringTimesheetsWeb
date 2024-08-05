package uz.cherevichenko.aspect.logging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingAspectSeminarProperties.class)
@ConditionalOnProperty(value = "application.logging.enabled", havingValue = "true")
public class LoggingAutoConfigurationSeminar {

    @Bean
    public LoggingAspectSeminar loggingAspectSeminar(LoggingAspectSeminarProperties properties){
        return new LoggingAspectSeminar(properties);
    }
}

