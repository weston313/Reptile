package com.wozipa.reptile.app.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;
import org.dom4j.io.XMLWriter;

import com.wozipa.reptile.shop.ShopConfiguration;

public class BaseConfiguration {

	private static final Log LOG=LogFactory.getLog(ShopConfiguration.class);
	
	private static final String CONFIGURATION_QNAME="configuration";
	private static final String PROPERTY_QNAME="property";
	private static final String NAME_QNAME="name";
	private static final String VALUE_QNAME="value";
	private static final String TYPE_QNAME="type";
	
	private Map<String, Key> container=null;
	private String xmlPath=null;
	
	public BaseConfiguration(String fileName)
	{
		container=new HashMap<>();
		this.xmlPath=getConfPath()+"/"+fileName;
		loadFile(this.xmlPath);
	}
	
	public String getConfPath()
	{
		String CONF_DIR=System.getProperty(AppConfiguration.REP_CONF_PATH);
		if(CONF_DIR==null || CONF_DIR.isEmpty())
		{
			CONF_DIR=System.getenv(AppConfiguration.REP_CONF_PATH);
		}
		if(CONF_DIR==null || CONF_DIR.isEmpty())
		{
			ClassLoader loader=BaseConfiguration.class.getClassLoader();
			LOG.info(loader);
			loader.getResource("classpath:config/");
			System.out.println(loader.getResource("config").getPath());
			CONF_DIR=loader.getResource("config").getPath();
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
	
	public void addKey(String name,Key key)
	{
		if(name==null || name.isEmpty())
		{
			return;
		}
		container.put(name, key);
	}
	
	public void close()
	{
		LOG.info("start t delete the data");
		Document document=DocumentHelper.createDocument();
		Element root=document.addElement(CONFIGURATION_QNAME);
		document.setRootElement(root);
		//
		Set<String> names=container.keySet();
		for(String name:names)
		{
			Key key=container.get(name);
			Element propertyNode=root.addElement(PROPERTY_QNAME);
			Element nameNode=propertyNode.addElement(NAME_QNAME);
			nameNode.setText(name);
			Element valueNode=propertyNode.addElement(VALUE_QNAME);
			valueNode.setText(key.getVlaue());
			Element typeNode=propertyNode.addElement(TYPE_QNAME);
			String type=key.getType()==null?"":key.getType();
			typeNode.addText(type);
		}
		//
		try {
			LOG.info(this.xmlPath);
			FileOutputStream outputStream=new FileOutputStream(new File(this.xmlPath));
			OutputStreamWriter osw=new OutputStreamWriter(outputStream, "UTF-8");
			OutputFormat of = new OutputFormat();
			of.setEncoding("UTF-8");  
			of.setIndent(true);  
			of.setIndent("    ");  
			of.setNewlines(true);  
			XMLWriter writer = new XMLWriter(osw, of);  
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
}
