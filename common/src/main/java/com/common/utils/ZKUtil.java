package com.common.utils;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ZKUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ZKUtil.class);

    @Autowired
    private ZkClient zkClient;

    List<String> allNode;

    /**
     * 创建父级节点
     */
    public void createRootNode(String root){
        boolean exists = zkClient.exists(root);
        if (exists){
            return;
        }
        //创建 root
        zkClient.createPersistent(root) ;
    }

    /**
     * 写入指定节点 临时目录
     */
    public void createNode(String path) {
        zkClient.createEphemeral(path);
    }

    /**
     * 获取所有注册节点
     * @return
     */
    public List<String> getAllNode(){
        LOGGER.info("查询所有节点成功，节点数："+allNode.size());
        return allNode;
    }

    /**
     * 更新server list
     */
    public void setAllNode(List<String> allNode){
        LOGGER.info("server节点更新，节点数："+allNode.size());
        this.allNode = allNode;
    }

}
