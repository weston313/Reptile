package com.wozipa.reptile.vpn;

import org.apache.log4j.Logger;

public class Proxy {

	private static final Logger LOGGER=Logger.getLogger(Proxy.class);
	
	private String host;
	private String port;
	private String username;
	private String password;
	
	private Proxy()
	{
		
	}
	
	Proxy(String host,String port,String user,String pwd)
	{
		this.host=host;
		this.port=port;
		this.username=user;
		this.password=pwd;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
