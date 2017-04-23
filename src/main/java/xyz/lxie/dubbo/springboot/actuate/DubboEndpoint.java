package xyz.lxie.dubbo.springboot.actuate;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Dubbo Endpoint
 *
 * @author xiegang
 * @since 2017/4/20
 */
@ConfigurationProperties(prefix = "endpoints.dubbo")
public class DubboEndpoint extends AbstractEndpoint<Map<String, Object>> {
    public DubboEndpoint() {
        super("dubbo", false);
    }

    @Override
    public Map<String, Object> invoke() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("status", DubboStatus.summary());
        metrics.put("services", DubboStatus.services());
        return metrics;
    }
}
