package com.wozipa.reptile.item;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wozipa.reptile.app.config.AppConfiguration;
import com.wozipa.reptile.app.config.Key;
import com.wozipa.reptile.util.ReptileUtils;

public class PageConfiguration {
	
	public static final Log LOG=LogFactory.getLog(PageConfiguration.class);
	
	private static final Map<String,Key> CONTAINER=new HashMap<>();
	private static final String CONFIG_FILE="item-page.xml";
	
	static{
		String confPath=ReptileUtils.GetConfPath();
		if(confPath==null || confPath.isEmpty())
		{
			LOG.info("not set REPTILE_CONF property");
		}
		else
		{
			File itemFile=new File(confPath+"/"+CONFIG_FILE);
			if(itemFile.exists())
			{
				try {
					Document document=new SAXReader().read(new BufferedInputStream(new FileInputStream(itemFile)));
					Element root=document.getRootElement();
					List<Element> properties=root.elements("property");
					for(Element property:properties)
					{
						String name=property.elementText("name");
						String value=property.elementText("value");
						String type=property.elementText("type");
						LOG.info("load configuration "+name+" "+value+" "+type);
						CONTAINER.put(name,new Key(value, type));
					}
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private volatile static PageConfiguration configuration=null;
	
	private PageConfiguration(){}
	
	public static PageConfiguration getInstance()
	{
		if(configuration==null)
		{
			synchronized (PageConfiguration.class) {
				if(configuration==null)
				{
					configuration=new PageConfiguration();
				}
			}
		}
		return configuration;
	}
	
	public Key getKey(String name)
	{
		if(name==null || name.isEmpty())
		{
			return null;
		}
		Key key=CONTAINER.get(name);
		return key;
	}
}