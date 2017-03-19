package com.wozipa.reptile.shop;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.operations.Div;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wozipa.reptile.app.config.Key;
import com.wozipa.reptile.cookie.CookieManagerCache;

public class TaobaoCategoryPage extends CategoryPage{
	
	private static final Log LOG=LogFactory.getLog(TaobaoCategoryPage.class);
	
	private static final String GOODS_URL="taobao.goods.url";
	private static final String GOODS_CONTAINER="taobao.goods.container";
	private static final String GOODS_NODE="taobao.goods.node";
	private static final String GOODS_VALUE="taobao.goods.value";
	
	private static final String REGEX="^(((http|https)://)(.*)(taobao.com))(.*)?";
	
	private ShopConfiguration configuration=null;
	
	private String pageUrl;
	private Element pageNode;
	private Set<String> goodUrls;
	private int test=0;
	
	public TaobaoCategoryPage()
	{
		configuration=ShopConfiguration.getConfiguration();
		goodUrls=new HashSet<>();
	}
	
	public void setTask(String pageUrl)
	{
		this.pageUrl=pageUrl;
		init();
	}
	
	public void init()
	{
		if(this.pageUrl==null || this.pageUrl.isEmpty())
		{
			LOG.info("the page url is empty");
			return;
		}
		WebClient webClient=new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setTimeout(10000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.setCookieManager(CookieManagerCache.getCache().getCookieManager());
		
		//开始进行抓取
		try {
			HtmlPage page=webClient.getPage(this.pageUrl);
			webClient.waitForBackgroundJavaScript(10000);
			webClient.setJavaScriptTimeout(0);
			String pageXml=page.asXml();
			//
			this.pageNode=Jsoup.parse(pageXml);
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String[] getGoodUrls() {
		// TODO Auto-generated method stub
		Key urlKey=configuration.getKey(GOODS_URL);
		LOG.info(this.pageNode.outerHtml());
		Element urlNode=getElement(this.pageNode,urlKey);
		if(urlNode==null)
		{
			LOG.info("the url node is null");
			return null;
		}
		String url=urlNode.attr("value");
		if(url==null || url.isEmpty())
		{
			LOG.info("the url is null");
			return null;
		}
		//
		Pattern pattern=Pattern.compile(REGEX);
		Matcher matcher=pattern.matcher(this.pageUrl);
		String prefix=null;
		if(matcher.find())
		{
			LOG.info("find it");
			prefix=matcher.group(1);
		}
		if(prefix==null || prefix.isEmpty())
		{
			LOG.info("cat get the page "+this.pageUrl);
			return null;
		}
		String goodsUrl=prefix+"/"+url;
		LOG.info("goods url is "+goodsUrl);
		//分析改url
		tracePage(goodsUrl);
		return goodUrls.toArray(new String[goodUrls.size()]);
	}
	
	public void tracePage(String url)
	{
		int page=1;
		String basePage=deletePageNo(url);
		String goodsUrl=null;
		do{
			goodsUrl=basePage+"&pageNo="+page;
			page++;
		}
		while(ergodicGoodsUrl(goodsUrl));
	}
	
	public boolean ergodicGoodsUrl(String goodsUrl)
	{
		try {
			Response response=Jsoup.connect(goodsUrl).execute();
			if(response==null || response.statusCode()!=200)
			{
				return false;
			}
			Element root=response.parse();
			Key containerKey=configuration.getKey(GOODS_CONTAINER);
			Element containerNode=getElement(root, containerKey);
			if(containerNode==null)
			{
				return false;
			}
			Key nodeKey=configuration.getKey(GOODS_NODE);
			Key valueKey=configuration.getKey(GOODS_VALUE);
			Elements nodes=containerNode.select(nodeKey.getVlaue());
			if(nodes.size()<=0)
			{
				return false;
			}
			for(int i=0;i<nodes.size();i++)
			{
				Element node=nodes.get(i);
				Element valueNode=getElement(node, valueKey);
				String goodUrl=valueNode.attr("href");
				if(goodUrl.startsWith("\\"))
				{
					goodUrl=goodUrl.substring(1);
				}
				if(goodUrl.startsWith("\""))
				{
					goodUrl=goodUrl.substring(1);
				}
				//
				if(goodUrl.endsWith("\""))
				{
					goodUrl=goodUrl.substring(0, goodUrl.length()-2);
				}
				//
				if(goodUrl.startsWith("//"))
				{
					goodUrl="https:"+goodUrl;
				}
				else
				{
					goodUrl="https://"+goodUrl;
				}
				//
				
				//
				this.goodUrls.add(goodUrl);
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
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

}
