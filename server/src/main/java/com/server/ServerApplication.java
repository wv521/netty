package com.server;

import com.common.config.BeanConfiguration;
import com.common.utils.SpringBeanFactory;
import com.common.utils.ZKUtil;
import com.server.utils.RegisterToZk;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SpringBeanFactory.class, ZKUtil.class, BeanConfiguration.class})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    public void run(String... args) throws Exception {
        Thread thread = new Thread(new RegisterToZk());
        thread.setName("im-server-register-thread");
        thread.start();
    }
}
