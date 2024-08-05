package uz.cherevichenko.aspect.recover;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("exception.logging")
public class LoggingAndRecoveryAspectProperties {
}
