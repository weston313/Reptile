package com.wozipa.reptile.app.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class AppConfiguration {
	
	private static final Log LOG=LogFactory.getLog(AppConfiguration.class);
	
	public static final String REP_CONF_PATH="REP_CONF_PATH";
	private static final String CONF_FILE="reptile-default.xml";
	
	public static final String RESULT_PATH="reptile.result.path";
	public static final String COOKIE_USERNAME="reptile.cookie.username";
	public static final String COOKIE_PASSWORD="reptile.cookie.password";
	
	private volatile static AppConfiguration configuration=null;
	
	public static AppConfiguration getConfiguration()
	{
		if(configuration==null)
		{
			synchronized(BaseConfiguration.class)
			{
				if(configuration==null){
					configuration=new AppConfiguration();
				}
			}
		}
		return configuration;
	}
	
	private BaseConfiguration baseConfiguration=null;
	
	private AppConfiguration()
	{
		baseConfiguration=new BaseConfiguration(CONF_FILE);
	}
	
	public Key getKey(String name)
	{
		if(name==null || name.isEmpty())
		{
			LOG.info("the name is null");
			return null;
		}
		return baseConfiguration.getKey(name);
	}
	
	public void addKey(String name,Key key)
	{
		baseConfiguration.addKey(name, key);
	}
	
	public void close()
	{
		baseConfiguration.close();
	}
	
}
