package com.wozipa.reptile.vpn;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class VPNProxy {
	
	public final static Logger LOGGER=Logger.getLogger(VPNProxy.class);
		
	private static VPNProxy vpnProxy=null;
	
	public static VPNProxy GetVpn()
	{
		if(vpnProxy==null)
		{
			synchronized (VPNProxy.class) {
				if(vpnProxy==null)
				{
					vpnProxy=new VPNProxy();
				}
			}
		}
		return vpnProxy;
	}
	
	private List<Proxy> proxies=null;
	
	private VPNProxy()
	{
		proxies=new ArrayList<>();
	}
	
	public Proxy getProxy()
	{
		return null;
	}
	
	public void addProxy(String host,String port,String user,String pwd)
	{
		proxies.add(new Proxy(host,port,user,pwd));
	}
	
	

}
