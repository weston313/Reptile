package com.wozipa.reptile.shop;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wozipa.reptile.app.config.Key;

public class TMallCategoryPage extends CategoryPage{
	
	private static final Log LOG=LogFactory.getLog(TMallCategoryPage.class);
	
	private static final String GOODS_URL="tmall.goods.url";
	private static final String GOODS_CONTAINER="tmall.goods.container";
	private static final String GOODS_NODE="tmall.goods.node";
	private static final String GOODS_THUMB="tmall.goods.thumb";
	private static final String GOODS_VALUE="tmall.goods.value";
	private static final String NEXT_PAGE="tmall.next.page";
	
	private static final String REGEX="^(((http|https)://)(.*)(tmall.com))(.*)?";
	
	private ShopConfiguration configuration=null;
	private String pageUrl;
	private Element pageNode;
	private Set<String> goodUrls=null;
	
	public TMallCategoryPage()
	{
		configuration=ShopConfiguration.getConfiguration();
		goodUrls=new HashSet<>();
	}
	
	public void setTask(String pageUrl)
	{
		this.pageUrl=pageUrl;
		//开始创建
		WebClient webClient=new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setTimeout(20000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.waitForBackgroundJavaScript(10000);
		try {
			HtmlPage page=webClient.getPage(this.pageUrl);
			String pageXml=page.asXml();
			this.pageNode=Jsoup.parse(pageXml);
			LOG.info(pageNode.outerHtml());
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String[] getGoodUrls()
	{
		//
		if(this.pageNode==null)
		{
			LOG.info("the page node is null");
			return null;
		}
		if(configuration==null)
		{
			LOG.info("the configuration is null");
			return null;
		}
		Key key=configuration.getKey(GOODS_URL);
		Element goodsUrlNode=getElement(this.pageNode, key);
		if(goodsUrlNode==null)
		{
			LOG.info("the good url node is null");
			return null;
		}
		//
		String goodsUrl=goodsUrlNode.attr("value");
		if(goodsUrl==null || goodsUrl.isEmpty())
		{
			goodsUrl=goodsUrlNode.val();
			if(goodsUrl==null || goodsUrl.isEmpty())
			{
				LOG.info("can not get the value of the result url input");
				return null;
			}
		}
		//开始进行信息获取
		String urlPrefix=null;
		Pattern pattern=Pattern.compile(REGEX);
		Matcher matcher=pattern.matcher(this.pageUrl);
		if(matcher.find())
		{
			LOG.info("find it");
			urlPrefix=matcher.group(1);
		}
		//开始进行拼接
		goodsUrl=urlPrefix+goodsUrl;
		//
		tracePage(goodsUrl);
		//
		return this.goodUrls.toArray(new String[this.goodUrls.size()]);
	}
	
	public void tracePage(String url)
	{
		String baseUrl=deletePageNo(url);
		String goodsUrl=null;
		int page=1;
		do{
			goodsUrl=baseUrl+"&pageNo="+page;
			page++;
		}while(ergodicGoodsUrl(goodsUrl));
	}
	
	public boolean ergodicGoodsUrl(String goodsUrl)
	{
		LOG.info("start to ergodic the page "+goodsUrl);
		if(goodsUrl==null || goodsUrl.isEmpty())
		{
			LOG.info("the base url is null");
			return false;
		}
		//
		try {
			Response response=Jsoup.connect(goodsUrl).execute();
			if(response==null || response.statusCode()!=200)
			{
				LOG.info("there is no page existed");
				return false;
			}
			LOG.info(response.statusCode());
			//
			Element goodsPageNode=response.parse();
			if(goodsPageNode==null)
			{
				return false;
			}
			//开始获取相关的 数据
			Key containerKey=configuration.getKey(GOODS_CONTAINER);
			Element containerNode=getElement(goodsPageNode, containerKey);
			if(containerNode==null)
			{
				containerNode=goodsPageNode;
			}
			//
			Key itemKey=configuration.getKey(GOODS_NODE);
			List<Element> itemNodes=getElements(containerNode,itemKey);
			if(itemNodes==null || itemNodes.isEmpty())
			{
				LOG.info("the item nodes is empty");
				return false;
			}
			//
			Key valueKey=configuration.getKey(GOODS_VALUE);
			for(Element itemNode:itemNodes)
			{
				Element thumbNode=getElement(itemNode,configuration.getKey(GOODS_THUMB));
				if(thumbNode==null)
				{
					continue;
				}
				//
				Element valueNode=getElement(itemNode,valueKey);
				String goodUrl=valueNode.attr("href");
				goodUrl=correctUrl(goodUrl);
				if(goodUrl.startsWith("//"))
				{
					goodUrl="https:"+goodUrl;
				}
				else {
					goodUrl="https://"+goodUrl;
				}
				this.goodUrls.add(goodUrl);
			}
			//判断是否还具有下一页
			Key nextPageKey=configuration.getKey(NEXT_PAGE);
			Element nextPageNode=getElement(goodsPageNode,nextPageKey);
			String nextPageUrl=nextPageNode.attr("href");
			if(nextPageUrl==null || nextPageUrl.isEmpty())
			{
				return false;
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
	
}
