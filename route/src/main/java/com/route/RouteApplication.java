package com.route;

import com.common.config.BeanConfiguration;
import com.common.utils.SpringBeanFactory;
import com.common.utils.ZKUtil;
import com.route.config.InitConfiguration;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.List;

@SpringBootApplication
@Import({SpringBeanFactory.class, ZKUtil.class, BeanConfiguration.class})
public class RouteApplication {

    @Autowired
    private InitConfiguration conf;
    @Autowired
    private ZKUtil zkUtil;

    public static void main(String[] args) {
        SpringApplication.run(RouteApplication.class, args);
    }

    @Bean
    @Primary
    public ZkClient createZKClient(){
        ZkClient zk = new ZkClient(conf.getAddr());

        //监听/im节点下子节点的变化，实时更新server list
        zk.subscribeChildChanges(conf.getRoot(), new IZkChildListener() {

            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                zkUtil.setAllNode(currentChilds);
            }
        });
        return zk;
    }

}
