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
	private static final String CONF_FILE="replite.xml";
	
	public static final String KEY_DOWNLOAD_PATH="replite.download.path";
	
	private static final Map<String, String> APP_CONF=new HashMap<>();
	
	static{
		String confDir=System.getProperty(REP_CONF_PATH);
		try {
			Document document=new SAXReader().read(new File(confDir)+"/"+CONF_FILE);
			Element root=document.getRootElement();
			List<Element> list=root.elements("property");
			for(Element property:list)
			{
				String name=property.elementText("name");
				String value=property.elementText("value");
				APP_CONF.put(name, value);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
