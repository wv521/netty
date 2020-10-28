package com.server.utils;

import com.common.utils.SpringBeanFactory;
import com.common.utils.ZKUtil;
import com.server.config.InitConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class RegisterToZk implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegisterToZk.class);

    private ZKUtil zk;
    private InitConfiguration conf;

    public RegisterToZk(){
        zk = SpringBeanFactory.getBean(ZKUtil.class);
        conf = SpringBeanFactory.getBean(InitConfiguration.class);
    }
    public void run() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            zk.createRootNode(conf.getRoot());
            String path = conf.getRoot() + "/" +ip+":"+conf.getNettyPort()+":"+conf.getHttpPort();
            try {
                zk.createNode(path);
            } catch (Exception e) {
                LOGGER.error("---zk注册失败,"+e.getMessage());
            }
            LOGGER.info("------服务注册在ZK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
