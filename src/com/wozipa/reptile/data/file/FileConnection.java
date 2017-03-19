package com.wozipa.reptile.data.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.wozipa.reptile.data.Connectin;
import com.wozipa.reptile.data.Data;

public class FileConnection<T extends Data> extends Connectin{
	
	private static final Log LOG=LogFactory.getLog(FileConnection.class);
	
	private static final String ROOT_QNAME="data";
	private static final String ROW_QNAME="row";
	
	private String filePath;
	private Class<T> cls;
	private Map<String,Data> container;
	private Map<String, Data> newData;
	
	public FileConnection(String path,Class<T> cls){
		this.filePath=path;
		this.cls=cls;
		this.container=new HashMap<>();
		this.newData=new HashMap<>();
	
		loadFile();
	}
	
	public void loadFile()
	{
		FileInputStream reader;
		try {
			//LOG.info("the path is "+FileConnection.class.getResource("/data/id.xml").getPath());
			InputStream fileInputStream=FileConnection.class.getResourceAsStream(this.filePath);
			Document document=new SAXReader().read(fileInputStream);
			Element root=document.getRootElement();
			List<Element> rows=root.elements(ROW_QNAME);
			for(Element row:rows)
			{
				Data data=cls.newInstance();
				String idString=null;
				List<Element> cells=row.elements();
				for(Element cell:cells)
				{
					String name=cell.getName();
					String value=cell.getText();
					if(name.equals("id"))
					{
						idString=value;
					}
					String methodName="set"+upOneCase(name);
					Method method=data.getClass().getDeclaredMethod(methodName,String.class);
					method.invoke(data, value);
				}
				//将数据插入到container中
				container.put(idString,data);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public synchronized void addRow(String id,Data row)
	{
		LOG.info("write the data");
		if(id==null || id.isEmpty())
		{
			return;
		}
		if(row==null)
		{
			return;
		}
		newData.put(id, row);
	}
	
	/**
	 * @param id 
	 * @return
	 * @see 检测这个id值是否存在
	 */
	public boolean existedId(String id)
	{
		if(container.containsKey(id))
		{
			return true;
		}
		else if(newData.containsKey(id))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void close()
	{
		LOG.info("start to write the data into the file");
		FileInputStream reader;
		try {
			reader = new FileInputStream(FileConnection.class.getResource(this.filePath).getFile());
			Document document=new SAXReader().read(reader);
			Element root=document.getRootElement();
			LOG.info(root.asXML());
			Set<String> keySet=newData.keySet();
			for(String key:keySet)
			{
				Data row=newData.get(key);
				root.add(row.toXml());
			}
			//开始写入数据
			OutputFormat opf=new OutputFormat("\t",true,"UTF-8");
			opf.setTrimText(true);
			XMLWriter writer=new XMLWriter(new FileOutputStream(this.getClass().getResource(this.filePath).getPath()),opf);
			if(writer==null)
			{
				LOG.info("the writer is null");
			}
			writer.write(document);
			writer.flush();
			writer.close();
			LOG.info("close");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void write(Data data) {
		// TODO Auto-generated method stub
		Method method;
		try {
			method = data.getClass().getDeclaredMethod("getId");
			String id=(String) method.invoke(data);
			if(!existedId(id))
			{
				addRow(id,data);
			}
			else {
				LOG.info("the id "+id+" is existed");
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getGoodId(String id)
	{
		if(id==null || id.isEmpty())
		{
			return null;
		}
		if(newData.containsKey(id))
		{
			IdFileData data=(IdFileData) newData.get(id);
			return data.getGoodId();
		}
		if(container.containsKey(id))
		{
			IdFileData data=(IdFileData) container.get(id);
			return data.getGoodId();
		}
		return null;
	}
	

}
