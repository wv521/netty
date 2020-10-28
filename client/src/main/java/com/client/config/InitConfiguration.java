package com.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitConfiguration {

    @Value("${im.user.id}")
    private String userId;
    @Value("${im.user.name}")
    private String userName;
    @Value("${im.route.login.url}")
    private String routeLoginUrl;
    @Value("${im.route.chat.url}")
    private String routeChatUrl;
    @Value("${im.route.logout.url}")
    private String routeLogoutUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRouteLoginUrl() {
        return routeLoginUrl;
    }

    public void setRouteLoginUrl(String routeLoginUrl) {
        this.routeLoginUrl = routeLoginUrl;
    }

    public String getRouteChatUrl() {
        return routeChatUrl;
    }

    public void setRouteChatUrl(String routeChatUrl) {
        this.routeChatUrl = routeChatUrl;
    }

    public String getRouteLogoutUrl() {
        return routeLogoutUrl;
    }

    public void setRouteLogoutUrl(String routeLogoutUrl) {
        this.routeLogoutUrl = routeLogoutUrl;
    }
}
