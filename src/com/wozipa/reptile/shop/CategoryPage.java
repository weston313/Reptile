package com.wozipa.reptile.shop;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wozipa.reptile.app.config.Key;

public abstract class CategoryPage {
	
	private static final Log LOG=LogFactory.getLog(CategoryPage.class);
	
	public abstract String[] getGoodUrls();
	
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
		else
		{
			node=parent.getElementsByTag(key.getVlaue()).first();
		}
		return node;
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
		else if(type.toLowerCase().equals("regex")) {
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
	
	public String deletePageNo(String url)
	{
		String[] parts=url.split("\\?");
		String[] paramParts=parts[1].split("&");
		StringBuffer sb=new StringBuffer(parts[0]+"?");
		for(int i=0;i<paramParts.length;i++)
		{
			String param=paramParts[i];
			if(!param.toLowerCase().contains("pageno=") && !param.toLowerCase().contains("pagenum="))
			{
				sb.append(param);
				if(i<paramParts.length-2)
				{
					sb.append("&");
				}
			}
		}
		return sb.toString();
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
