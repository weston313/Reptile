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

import com.wozipa.reptile.shop.ShopConfiguration;

public class BaseConfiguration {

	private static final Log LOG=LogFactory.getLog(ShopConfiguration.class);
	
	private static final String PROPERTY_QNAME="property";
	private static final String NAME_QNAME="name";
	private static final String VALUE_QNAME="value";
	private static final String TYPE_QNAME="type";
	
	private Map<String, Key> container=null;
	
	public BaseConfiguration(String xmlPath)
	{
		container=new HashMap<>();
		xmlPath=getConfPath()+"/"+xmlPath;
		loadFile(xmlPath);
	}
	
	private String getConfPath()
	{
		String CONF_DIR=System.getProperty(AppConfiguration.REP_CONF_PATH);
		if(CONF_DIR==null || CONF_DIR.isEmpty())
		{
			CONF_DIR=System.getenv(AppConfiguration.REP_CONF_PATH);
		}
		if(CONF_DIR==null || CONF_DIR.isEmpty())
		{
			CONF_DIR=ShopConfiguration.class.getResource("/config").getPath();
		}
		return CONF_DIR;
	}
	
	public void loadFile(String xmlPath){
		try {
			Document document=new SAXReader().read(new File(xmlPath));
			Element root=document.getRootElement();
			List<Element> properties=root.elements(PROPERTY_QNAME);
			for(Element property:properties)
			{
				String name=property.elementText(NAME_QNAME);
				if(name==null || name.isEmpty())
				{
					continue;
				}
				String value=property.elementText(VALUE_QNAME);
				String type=property.elementText(TYPE_QNAME);
				LOG.info("load the configuration property "+name+" "+value+" "+type);
				container.put(name,new Key(value, type));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Key getKey(String name)
	{
		if(name==null ||name.isEmpty())
		{
			return null;
		}
		return container.get(name);
	}
}
