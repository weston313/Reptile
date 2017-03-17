package com.wozipa.reptile.shop;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wozipa.reptile.app.config.Key;

public class AlibbCategoryPage extends CategoryPage{
	
	private static final Log LOG=LogFactory.getLog(AlibbCategoryPage.class);
	
	private static final String GOODS_CONTAINER="alibb.goods.container";
	private static final String GOODS_WINDOWS="alibb.goods.windows";
	private static final String GOODS_NODE="alibb.goods.node";
	private static final String GOODS_TITLE="alibb.goods.title";
	private static final String GOODS_VALUE="alibb.goods.value";
	
	private static final String GOODS_PAGE="alibb.goods.page";
	private static final String GOODS_NEXT_PAGE="alibb.goods.next.page";
	
	private String pageUrl=null;
	private Set<String> goodUrls=null;
	private ShopConfiguration configuration=null;
	
	public AlibbCategoryPage() {
		// TODO Auto-generated constructor stub
		configuration=ShopConfiguration.getConfiguration();
		goodUrls=new HashSet<>();
	}
	
	public void setTask(String pageUrl)
	{
		this.pageUrl=pageUrl;
	}

	@Override
	public String[] getGoodUrls() {
		// TODO Auto-generated method stub
		//
		String baseUrl=deletePageNo(this.pageUrl);
		String goodsUrl=null;
		int page=1;
//		do
//		{
//			goodsUrl=baseUrl+"&pageNum="+page;
//			page++;
//		}while(ergodicGoodsUrl(goodsUrl));
		ergodicGoodsUrl(this.pageUrl);
		return this.goodUrls.toArray(new String[this.goodUrls.size()]);
	}
	
	public boolean ergodicGoodsUrl(String url)
	{
		
		Element pageNode=null;
		try {
			WebClient webClient=new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setRedirectEnabled(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setTimeout(20000);
			webClient.waitForBackgroundJavaScript(20000);
			HtmlPage page=webClient.getPage(url);
			System.out.println(page.asXml());
			//
			pageNode=Jsoup.parse(page.asXml());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		LOG.info(pageNode.outerHtml());
		LOG.info("load the page url "+url);
		//
		Key containerKey=configuration.getKey(GOODS_CONTAINER);
		Element containerNode=getElement(pageNode, containerKey);
		if(containerNode==null)
		{
			LOG.info("the container node is null");
			return true;
//			containerNode=pageNode;
		}
		//
		Key windowKey=configuration.getKey(GOODS_WINDOWS);
		Element windowNode=getElement(containerNode,windowKey);
		if(windowNode==null)
		{
			LOG.info("the windows node is null");
			return true;
			//windowNode=containerNode;
		}
		//
		Key nodeKey=configuration.getKey(GOODS_NODE);
		List<Element> nodes=getElements(windowNode, nodeKey);
		for(Element node:nodes)
		{
			LOG.info(node.outerHtml());
			Key titleKey=configuration.getKey(GOODS_TITLE);
			Element titleNode=getElement(node,titleKey);
			//LOG.info(titleNode.outerHtml());
			Key valueKey=configuration.getKey(GOODS_VALUE);
			Element valueNode=getElement(titleNode, valueKey);
			
			String goodUrl=valueNode.attr("href");
			goodUrl=correctUrl(goodUrl);
			this.goodUrls.add(goodUrl);
		}
		//获取下一页内容
		Key pageKey=configuration.getKey(GOODS_PAGE);
		Element pagerContanerNode=getElement(pageNode, pageKey);
		Key nextPageKey=configuration.getKey(GOODS_NEXT_PAGE);
		Element nextPageNode=getElement(pagerContanerNode, nextPageKey);
		if(nextPageNode==null)
		{
			LOG.info("the next page button is null");
			return false;
		}
		String nextUrl=nextPageNode.attr("href");
		if(nextUrl==null || nextUrl.isEmpty())
		{
			return false;
		}
		return true;
	}

}
