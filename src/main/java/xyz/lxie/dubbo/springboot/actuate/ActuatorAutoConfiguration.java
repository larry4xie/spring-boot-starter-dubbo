package xyz.lxie.dubbo.springboot.actuate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dubbo Actuator AutoConfiguration
 *
 * @author xiegang
 * @since 2017/4/21
 */
@Configuration
@ConditionalOnClass(name = { "org.springframework.boot.actuate.endpoint.AbstractEndpoint",
        "org.springframework.boot.actuate.health.AbstractHealthIndicator" })
public class ActuatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DubboEndpoint dubboEndpoint() {
        return new DubboEndpoint();
    }

    @Bean
    @ConditionalOnMissingBean
    public DubboHealthIndicator dubboHealthIndicator() {
        return new DubboHealthIndicator();
    }
}
