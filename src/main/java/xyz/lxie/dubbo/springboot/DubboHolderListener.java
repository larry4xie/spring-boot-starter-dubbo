package xyz.lxie.dubbo.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DubboHolder Spring Listener
 *
 * @author xiegang
 * @since 2017/4/19
 */
public class DubboHolderListener implements ApplicationListener {
    private DubboHolder holder = new DubboHolder();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationPreparedEvent) {
            holder.start();
        } else if (event instanceof ContextClosedEvent) {
            holder.shutdown();
        }
    }


    /** Dubbo Holder **/
    static class DubboHolder {
        private static final Logger logger = LoggerFactory.getLogger(DubboHolder.class);

        private static final int CHECK_INTERVAL_IN_SECONDS = 2;

        private volatile Thread holdThread;
        private AtomicBoolean running = new AtomicBoolean(false);
        private int checkIntervalInSeconds;

        public DubboHolder() {
            this(CHECK_INTERVAL_IN_SECONDS);
        }

        public DubboHolder(int checkIntervalInSeconds) {
            this.checkIntervalInSeconds = checkIntervalInSeconds;
        }

        public void start() {
            if (running.compareAndSet(false, true)) {
                holdThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("Start dubbo holder thread: " + Thread.currentThread().getName());
                        while (running.get() && !Thread.currentThread().isInterrupted()) {
                            try {
                                TimeUnit.SECONDS.sleep(checkIntervalInSeconds);
                            } catch (InterruptedException ignored) {
                            }
                        }
                        logger.info("Shutdown dubbo holder thread: " + Thread.currentThread().getName());
                    }
                }, "Dubbo-Holder");
                holdThread.setDaemon(false);
                holdThread.start();
            }
        }

        public void shutdown() {
            if (running.compareAndSet(true, false)) {
                holdThread.interrupt();
                holdThread = null;
            }
        }
    }
}
