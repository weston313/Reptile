package com.wozipa.reptile.item;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wozipa.reptile.app.config.Key;

public abstract class Page {
	
	private static final Log LOG=LogFactory.getLog(Page.class);
	protected PageConfiguration configuration;
	
	
	public abstract void generateId();
	
	public abstract void generateTitle();
	
	public abstract void generateSize();
	
	public abstract void generateColor();
	
	public abstract void generateDate();
	
	public abstract void generatePrice();
	
	public abstract void generateImages();
	
	public abstract void startGenerate();
	
	public abstract String getPageUrl();

	public abstract String getID();
	
	public abstract String getTitle();
	
	public abstract String getPrice();
	
	public abstract String getColor();
	
	public abstract String getSize();
	
	public abstract String getPlatform();
	
	public abstract String getDate();
	
	public abstract void setTask(String pageUrl,String resultPath,int encrypt);
	
	/**
	 * @param containerName
	 * @param nodeName
	 * @param valueName
	 * @return 根据三层关系获取第三层的数据节点
	 */
	public String generateValue(Element pageNode,String containerName,String nodeName,String valueName)
	{
		if(pageNode==null)
		{
			LOG.info("the page node is null");
			return null;
		}
		Key containerKey=configuration.getKey(containerName);
		Element containerNode=getElement(pageNode,containerKey);
		if(containerNode==null)
		{
			LOG.info("the container node is null");
			containerNode=pageNode;
		}
		//
		Key nodeKey=configuration.getKey(nodeName);
		Element node=getElement(containerNode,nodeKey);
		if(node==null)
		{
			LOG.info("the node is null");
			node=pageNode;
		}
		//
		Key valueKey=configuration.getKey(valueName);
		List<Element> valuesNode=getElements(node,valueKey);
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<valuesNode.size();i++)
		{
			Element valueNode=valuesNode.get(i);
			if(valueNode==null)
			{
				LOG.info("the value node is null");
			}
			String text=valueNode.text();
			if(text==null || text.isEmpty())
			{
				text=valueNode.val();
			}
			sb.append(text);
			if(i<valuesNode.size()-1)
			{
				sb.append(";");
			}
		}
		return sb.toString();
	}
	
	/**
	 * @param parent
	 * @param key
	 * @return 获取element列表
	 * @see 获取一个列表
	 */
	public List<Element> getElements(Element parent,Key key)
	{
		List<Element> nodes=new ArrayList<>();
		if(parent==null)
		{
			LOG.info("the parent node is null");
			return null;
		}
		if(key==null)
		{
			LOG.info("the key object is null");
			return null;
		}
		String type=key.getType();
		if(type.toLowerCase().equals("id"))
		{
			Element node=parent.getElementById(key.getVlaue());
			if(node==null)
			{
				LOG.info(parent.id());
				LOG.info("the node from the id "+key.getVlaue()+" is null");
			}
			else
			{
				nodes.add(node);
			}
			
		}
		else if(type.toLowerCase().equals("class"))
		{
			Elements elements=parent.getElementsByClass(key.getVlaue());
			if(elements!=null && !elements.isEmpty())
			{
				for(int i=0;i<elements.size();i++)
				{
					nodes.add(elements.get(i));
				}
			}
			
		}
		else if(type.toLowerCase().equals("tag"))
		{
			Elements elements=parent.getElementsByTag(key.getVlaue());
			if(elements!=null && !elements.isEmpty())
			{
				for(int i=0;i<elements.size();i++)
				{
					nodes.add(elements.get(i));
				}
			}
		}
		else if(type.toLowerCase().equals("regex"))
		{
			Elements elements=parent.select(key.getVlaue());
			if(elements!=null && !elements.isEmpty())
			{
				for(int i=0;i<elements.size();i++)
				{
					nodes.add(elements.get(i));
				}
			}
		}
		return nodes;
	}
	
	/**
	 * @param parent
	 * @param key
	 * @return 获取指定的element
	 * @see 获取指定的element节点
	 */
	public Element getElement(Element parent,Key key)
	{
		if(parent==null)
		{
			LOG.info("the parent node is null");
			return null;
		}
		if(key==null)
		{
			LOG.info("the key object is null");
			return null;
		}
		Element node=null;
		String type=key.getType();
		if(type.toLowerCase().equals("id"))
		{
			node=parent.getElementById(key.getVlaue());
		}
		else if(type.toLowerCase().equals("class"))
		{
			node=parent.getElementsByClass(key.getVlaue()).first();
		}
		else if(type.toLowerCase().equals("tag"))
		{
			node=parent.getElementsByTag(key.getVlaue()).first();
		}
		else if(type.toLowerCase().equals("regex"))
		{
			node=parent.select(key.getVlaue()).first();
		}
		return node;
	}
	
	public String correctUrl(String url)
	{
		if(url.startsWith("\\"))
		{
			url=url.substring(1);
		}
		if(url.startsWith("\""))
		{
			url=url.substring(1);
		}
		if(url.endsWith("\""))
		{
			url=url.substring(0, url.length()-2);
		}
		if(url.endsWith("\""))
		{
			url=url.substring(0, url.length()-2);
		}
		return url;
	}
}
