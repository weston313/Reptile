package com.wozipa.replite.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.wozipa.reptile.app.config.Key;

public class DomReaderTest {
	
	@Test
	public void test()
	{
		File itemFile=new File("E:\\exej\\reptile\\config\\item-page.xml");
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
					System.out.println("load configuration "+name+" "+value+" "+type);
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
