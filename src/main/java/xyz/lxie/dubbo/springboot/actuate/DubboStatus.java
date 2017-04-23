package xyz.lxie.dubbo.springboot.actuate;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.status.Status;
import com.alibaba.dubbo.common.status.StatusChecker;
import com.alibaba.dubbo.common.status.support.StatusUtils;
import com.alibaba.dubbo.rpc.protocol.dubbo.DubboProtocol;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Dubbo Status
 *
 * @author xiegang
 * @since 2017/4/20
 */
class DubboStatus {
    private static final List<String> IGNORE_STATS = Collections.singletonList("spring");

    static Map<String, SimpleStatus> summary() {
        return status().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, SimpleStatus::of));
    }

    static boolean isHealth() {
        return isHealth(status());
    }

    static boolean isHealth(Map<String, Status> status) {
        Status stat = StatusUtils.getSummaryStatus(status);
        return !Status.Level.ERROR.equals(stat.getLevel());
    }

    static Map<String, Status> status() {
        Map<String, Status> statusMap = new TreeMap<>();
        ExtensionLoader<StatusChecker> extensionLoader = ExtensionLoader.getExtensionLoader(StatusChecker.class);

        extensionLoader.getSupportedExtensions()
                .stream()
                .filter(name -> !IGNORE_STATS.contains(name))
                .forEach(name -> {
                    StatusChecker extension = extensionLoader.getExtension(name);
                    if (extension != null) {
                        Status status = extension.check();
                        if (status != null && !Status.Level.UNKNOWN.equals(status.getLevel())) {
                            statusMap.put(name, status);
                        }
                    }
                });

        return statusMap;
    }

    static Map<String, Set<String>> services() {
        return DubboProtocol.getDubboProtocol().getExporters()
                .stream()
                .map(exporter -> exporter.getInvoker().getInterface())
                .collect(Collectors.toMap(Class::getName, DubboStatus::getMethods));
    }

    private static Set<String> getMethods(Class<?> face) {
        return Arrays.stream(face.getMethods())
                .map(Method::getName)
                .collect(Collectors.toSet());
    }

     /** simple dubbo status **/
     static class SimpleStatus {
        private final String level;

        private final String message;

        static SimpleStatus of(Map.Entry<String, Status> statusEntry) {
            Status status = statusEntry.getValue();
            return new SimpleStatus(status.getLevel().name(), status.getMessage());
        }

        SimpleStatus(String level, String message){
            this.level = level;
            this.message = message;
        }

        public String getLevel() {
            return level;
        }

        public String getMessage() {
            return message;
        }
    }
}
