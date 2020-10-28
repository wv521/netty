package com.common.bean;


import java.io.Serializable;

public class ServerInfo implements Serializable {

	private static final long serialVersionUID = -2230742812761280401L;
	private String ip;
	private Integer nettyPort;
	private Integer httpPort;

	public ServerInfo(String ip, Integer nettyPort, Integer httpPort) {
		this.ip = ip;
		this.nettyPort = nettyPort;
		this.httpPort = httpPort;
	}

	public ServerInfo() {
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getNettyPort() {
		return nettyPort;
	}

	public void setNettyPort(Integer nettyPort) {
		this.nettyPort = nettyPort;
	}

	public Integer getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(Integer httpPort) {
		this.httpPort = httpPort;
	}
}
