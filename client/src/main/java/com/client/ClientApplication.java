package com.client;

import com.client.handle.ScannerHandle;
import com.common.config.BeanConfiguration;
import com.common.utils.SpringBeanFactory;
import com.common.utils.ZKUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ZKUtil.class, BeanConfiguration.class, SpringBeanFactory.class})
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    public void run(String... args) throws Exception {
        try {
            Thread thread = new Thread(new ScannerHandle());
            thread.setName("client-scanner-thread");
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
