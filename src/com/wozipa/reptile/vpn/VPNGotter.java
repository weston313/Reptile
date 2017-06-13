package com.wozipa.reptile.vpn;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wozipa.reptile.app.config.AppConfiguration;
import com.wozipa.reptile.app.config.Key;

public class VPNGotter{
	
	private static final Logger LOGGER=Logger.getLogger(VPNGotter.class);

	private static final String PROXY_SITE="reptile.proxy.site";
	private static final String PROXY_CONTAINER="reptile.proxy.container";
	private static final String PROXY_VALUE="reptile.proxy.value";
	private static final String PROXY_HOST="reptile.proxy.host";
	private static final String PROXY_PORT="reptile.proxy.port";
	private static final String PROXY_USERNAME="reptile.proxy.user";
	private static final String PROXY_PASSWORD="reptile.proxy.pwd";
	
	private AppConfiguration configuration=null;
	private VPNProxy vpnProxy=null;
	
	public VPNGotter(AppConfiguration configuration)
	{
		this.configuration=configuration;
		this.vpnProxy=VPNProxy.GetVpn();
	}
	
	public void getProxySource()
	{
		Key siteKey=configuration.getKey(PROXY_SITE);
		String site=siteKey.getVlaue();
		WebClient webClient=new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setTimeout(10000);
		//
		try {
			HtmlPage page=webClient.getPage(site);
			Document document=Jsoup.parse(page.asXml());
			//
			List<Element> containers=getElements(document, configuration.getKey(PROXY_CONTAINER));
			for(Element container:containers)
			{
				List<Element> values= getElements(container,configuration.getKey(PROXY_VALUE));
				for(Element value:values)
				{
					Elements elements=value.getElementsByTag("tr");
					String host=null;
					if(configuration.getKey(PROXY_HOST)!=null)
					{
						host=elements.get(Integer.parseInt(configuration.getKey(PROXY_HOST).getVlaue())).text();
					}
					String port=null;
					if(configuration.getKey(PROXY_PORT)!=null)
					{
						port=elements.get(Integer.parseInt(configuration.getKey(PROXY_HOST).getVlaue())).text();
					}
					String user=null;
					if(configuration.getKey(PROXY_USERNAME)!=null)
					{
						user=elements.get(Integer.parseInt(configuration.getKey(PROXY_HOST).getVlaue())).text();
					}
					String pwd=null;
					if(configuration.getKey(PROXY_PASSWORD)!=null)
					{
						pwd=elements.get(Integer.parseInt(configuration.getKey(PROXY_HOST).getVlaue())).text();
					}
					vpnProxy.addProxy(host, port, user, pwd);
				}
			}
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			LOGGER.info("the parent node is null");
			return null;
		}
		if(key==null)
		{
			LOGGER.info("the key object is null");
			return null;
		}
		String type=key.getType();
		if(type.toLowerCase().equals("id"))
		{
			Element node=parent.getElementById(key.getVlaue());
			if(node==null)
			{
				LOGGER.info(parent.id());
				LOGGER.info("the node from the id "+key.getVlaue()+" is null");
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
			System.out.println("the parent node is null");
			return null;
		}
		if(key==null)
		{
			System.out.println("the key object is null");
			return null;
		}
		Element node=null;
		String type=key.getType();
		System.out.println("the type is "+type);
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
			System.out.println("hehehehehe");
			System.out.println(key.getVlaue());
			node=parent.select(key.getVlaue()).first();
		}
		System.out.println("hahahaha");
		return node;
	}
}
