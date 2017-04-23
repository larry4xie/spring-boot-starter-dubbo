package xyz.lxie.dubbo.springboot.actuate;

import com.alibaba.dubbo.common.status.Status;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

import java.util.Map;

/**
 * @author xiegang
 * @since 2017/4/20
 */
public class DubboHealthIndicator extends AbstractHealthIndicator {
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Map<String, Status> status = DubboStatus.status();

        if (DubboStatus.isHealth(status)) {
            builder.up();
        } else {
            builder.down();
        }

        status.forEach((name, stat) -> builder.withDetail(name, "[" + stat.getLevel() + "] " + stat.getMessage()));
    }
}
