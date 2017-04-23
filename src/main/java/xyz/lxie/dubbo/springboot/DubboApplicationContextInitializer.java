package xyz.lxie.dubbo.springboot;

import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Dubbo ApplicationContextInitializer
 *
 * @author xiegang
 * @since 2017/4/23
 */
public class DubboApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment env = applicationContext.getEnvironment();
        String scan = env.getProperty("spring.dubbo.scan");

        if (scan != null && !scan.isEmpty()) {
            ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
            AnnotationBean scanner = BeanUtils.instantiate(AnnotationBean.class);
            scanner.setPackage(scan);
            scanner.setApplicationContext(applicationContext);
            applicationContext.addBeanFactoryPostProcessor(scanner);
            beanFactory.addBeanPostProcessor(scanner);
            beanFactory.registerSingleton("annotationBean", scanner);
        }
    }
}
