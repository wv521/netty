package com.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitConfiguration {


    @Value("${server.port}")
    private int httpPort;
    @Value("${im.server.port}")
    private int nettyPort;

    @Value("${im.zk.root}")
    private String root;
    @Value("${im.zk.addr}")
    private String addr;

    @Value("${im.route.logout.url}")
    private String routeLogoutUrl;

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getNettyPort() {
        return nettyPort;
    }

    public void setNettyPort(int nettyPort) {
        this.nettyPort = nettyPort;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getRouteLogoutUrl() {
        return routeLogoutUrl;
    }

    public void setRouteLogoutUrl(String routeLogoutUrl) {
        this.routeLogoutUrl = routeLogoutUrl;
    }
}
