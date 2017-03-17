package com.wozipa.reptile.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wozipa.reptile.app.config.AppConfiguration;
import com.wozipa.reptile.app.config.BaseConfiguration;
import com.wozipa.reptile.app.config.Key;

public class ShopConfiguration {
	
	private static final Log LOG=LogFactory.getLog(ShopConfiguration.class);
	
	private static final String SHOP_FILE="shop.xml";
	
	private static ShopConfiguration shopConfiguration=null;
	
	public static ShopConfiguration getConfiguration()
	{
		if(shopConfiguration==null)
		{
			shopConfiguration=new ShopConfiguration();
		}
		return shopConfiguration;
	}
	
	//对象内容
	private BaseConfiguration configuration=null;
	
	private ShopConfiguration()
	{
		//String shopXmlPath=getConfPath()+"/"+SHOP_FILE;
		configuration=new BaseConfiguration(SHOP_FILE);
	}
	
	
	
	public Key getKey(String name)
	{
		return configuration.getKey(name);
	}
	
	

}
