package com.wozipa.reptile.item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.ibm.icu.text.SimpleDateFormat;
import com.wozipa.reptile.app.config.Key;

import net.sf.json.JSONObject;

public class TMallPage extends Page{
	
	private static final Log LOG=LogFactory.getLog(TMallPage.class);
	
	// 商品照片
	private static final String IMAGES_CONTAINER = "tmall.images.container";
	private static final String IMAGES_NODE = "tmall.images.node";
	private static final String IMAGES_VALUE = "tmall.images.value";
	private static final String IMAGES_THUMB="tmall.images.thumb";
	private static final String IMAGES_ZOOM="tmall.images.zoom";

	// 获取商品标题
	private static final String TITLE_CONTAINER = "tmall.title.container";
	private static final String TITLE_NODE = "tmall.title.node";
	private static final String TITILE_VALUE = "tmall.title.value";
	
	private static final String PRICE_PROMO_CONTAINER="tmall.promo.container";
	private static final String PRICE_PROMO_NODE="tmall.promo.node";
	private static final String PRICE_PROMO_VALUE="tmall.promo.value";

	// 获取商品价格
	private static final String PRICE_CONTAINER = "tmall.price.container";
	private static final String PRICE_NODE = "tmall.price.node";
	private static final String PRICE_VALUE = "tmall.price.value";

	// 获取商品大小值需要使用的变量
	private static final String SIZE_CONTAINER = "tmall.size.container";
	private static final String SIZE_NODE = "tmall.price.node";
	private static final String SIZE_VALUE = "tmall.price.value";

	private static final String COLOR_CONTAINER = "tmall.color.container";
	private static final String COLOR_NODE = "tmall.color.node";
	private static final String COLOR_VALUE = "tmall.color.value";
	
	private static final String DESC_CONTAINER="tmall.desc.container";
	private static final String DESC_NODE="tmall.desc.node";
	private static final String DESC_VALUE="tmall.desc.value";
	
	protected Element pageNode;
	
	private String id;
	private String pageUrl;
	private String title;
	private String price;
	private String size;
	private String color;
	private String images;
	private String date;
	
	private String resultPath;
	private int encrypt;
	
	
	public TMallPage() {
		// TODO Auto-generated constructor stub
		configuration=PageConfiguration.getInstance();
	}
	
	@Override
	public void setTask(String pageUrl, String resultPath,int encrypt) {
		// TODO Auto-generated method stub
		this.pageUrl=pageUrl;
		this.resultPath=resultPath;
		this.encrypt=encrypt;
		//
		WebClient webClient=new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setTimeout(60*1000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		//
		
		//
		try {
			HtmlPage page=webClient.getPage(this.pageUrl);
			webClient.waitForBackgroundJavaScript(10000);
			webClient.setJavaScriptTimeout(0);
			LOG.info(page.asXml());
			this.pageNode=Jsoup.parse(page.asXml());
		} catch (FailingHttpStatusCodeException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void generateId() {
		// TODO Auto-generated method stub
		if(this.pageUrl==null || pageUrl.isEmpty())
		{
			LOG.info("the page url is empty");
			return;
		}
		//
		String paramLine=this.pageUrl.split("\\?")[1];
		String[] params=paramLine.split("&");
		for(String param:params)
		{
			if(param.contains("id="))
			{
				this.id=param.split("=")[1];
			}
		}
	}

	@Override
	public void generateTitle() {
		// TODO Auto-generated method stub
		String value=generateValue(this.pageNode,TITLE_CONTAINER, TITLE_NODE, TITILE_VALUE);
		System.out.println(value);
		this.title=value;
	}

	@Override
	public void generateSize() {
		// TODO Auto-generated method stub
		String sizeValue=generateValue(this.pageNode,SIZE_CONTAINER, SIZE_NODE, SIZE_VALUE);
		this.size=sizeValue;
	}

	@Override
	public void generateColor() {
		// TODO Auto-generated method stub
		String colorValue=generateValue(this.pageNode,COLOR_CONTAINER, COLOR_NODE, COLOR_VALUE);
		this.color=colorValue;
	}

	@Override
	public void generateDate() {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.date=sdf.format(new Date());
	}

	@Override
	public void generatePrice() {
		// TODO Auto-generated method stub
		LOG.info("start to generate the price");
		String price=generateValue(this.pageNode, PRICE_PROMO_CONTAINER, PRICE_PROMO_NODE, PRICE_PROMO_VALUE);
		if(price==null || price.isEmpty()){
			price=generateValue(this.pageNode,PRICE_CONTAINER, PRICE_NODE, PRICE_VALUE);
		}
		
		this.price=price;
	}

	@Override
	public void generateImages() {
		// TODO Auto-generated method stub
		Key containerKey=configuration.getKey(IMAGES_CONTAINER);
		Element containerNode=getElement(this.pageNode,containerKey);
		if(containerNode==null)
		{
			containerNode=this.pageNode;
		}
		//
		Key nodeKey=configuration.getKey(IMAGES_NODE);
		Element node=getElement(containerNode,nodeKey);
		if(nodeKey==null)
		{
			node=this.pageNode;
		}
		//
		Key valueKey=configuration.getKey(IMAGES_VALUE);
		List<Element> valuesNode=getElements(node,valueKey);
		//
		String thumnStr=configuration.getKey(IMAGES_THUMB).getVlaue();
		String zoomStr=configuration.getKey(IMAGES_ZOOM).getVlaue();
		int k=0;
		for(Element image:valuesNode)
		{
			String srcUrl=image.attr("src");
			if(srcUrl.contains(thumnStr))
			{
				srcUrl=srcUrl.replaceAll(thumnStr, zoomStr);
			}
			//
			String name=this.id;
			if(k>0)
			{
				name=name+"_0"+k;
			}
			downloadImage(srcUrl, name);
			
		}
	}
	
	/**
	 * @author wozipa
	 * @date 2017-3-11 
	 * @see 讲商品描述的图片
	 */
	public void generateDescription()
	{
		Key containerKey=configuration.getKey(DESC_CONTAINER);
		Element containerNode=getElement(this.pageNode,containerKey);
		if(containerNode==null)
		{
			containerNode=this.pageNode;
		}
		//
		Key nodeKey=configuration.getKey(DESC_NODE);
		Element node=getElement(containerNode,nodeKey);
		if(nodeKey==null)
		{
			node=this.pageNode;
		}
		//
		Key valueKey=configuration.getKey(DESC_VALUE);
		List<Element> valuesNode=getElements(node,valueKey);
		//
		String thumnStr=configuration.getKey(IMAGES_THUMB).getVlaue();
		String zoomStr=configuration.getKey(IMAGES_ZOOM).getVlaue();
		int k=0;
		for(Element image:valuesNode)
		{
			String srcUrl=image.attr("src");
			if(srcUrl.contains(thumnStr))
			{
				srcUrl=srcUrl.replaceAll(thumnStr, zoomStr);
			}
			//
			String name=this.id;
			if(k>0)
			{
				name=name+"_0"+k;
			}
			downloadImage(srcUrl, name);
		}
	}
	
	/**
	 * @author wozip
	 * @date 2017-3-5
	 * @see 下载图片
	 * @param urlStr
	 * @param name
	 */
	public void downloadImage(String urlStr,String name)
	{
		try {
			URL url=new URL(urlStr);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			InputStream reader=connection.getInputStream();
			FileOutputStream writer=new FileOutputStream(new File(this.resultPath+"/"+name));
			byte[] buffer=new byte[4096];
			int eof=0;
			while((eof=reader.read(buffer))>=0)
			{
				writer.write(buffer,0,eof);
			}
			writer.flush();
			writer.close();
			reader.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	/**
//	 * @param containerName
//	 * @param nodeName
//	 * @param valueName
//	 * @return 根据三层关系获取第三层的数据节点
//	 */
//	public String generateValue(String containerName,String nodeName,String valueName)
//	{
//		Key containerKey=configuration.getKey(containerName);
//		Element containerNode=getElement(this.pageNode,containerKey);
//		if(containerNode==null)
//		{
//			containerNode=this.pageNode;
//		}
//		//
//		Key nodeKey=configuration.getKey(nodeName);
//		Element node=getElement(containerNode,nodeKey);
//		if(nodeKey==null)
//		{
//			node=this.pageNode;
//		}
//		//
//		Key valueKey=configuration.getKey(valueName);
//		List<Element> valuesNode=getElements(node,valueKey);
//		
//		System.out.println(valuesNode.size());
//		StringBuffer sb=new StringBuffer();
//		for(int i=0;i<valuesNode.size();i++)
//		{
//			Element valueNode=valuesNode.get(i);
//			String value=valueNode.text();
//			if (value==null || value.isEmpty()) {
//				value=valueNode.val();
//			}
//			sb.append(value);
//			if(i<valueNode.childNodeSize()-1)
//			{
//				sb.append(";");
//			}
//		}
//		return sb.toString();
//	}
	
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
		Element node=null;
		String type=key.getType();
		if(type.toLowerCase().equals("id"))
		{
			node=parent.getElementById(key.getVlaue());
			nodes.add(node);
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
		else
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
		else
		{
			node=parent.getElementsByTag(key.getVlaue()).first();
		}
		return node;
	}
	
	public String toJson()
	{
		JSONObject object=new JSONObject();
		object.put("id", this.id);
		object.put("pageUrl", this.pageUrl);
		object.put("title",this.title);
		object.put("color", this.color);
		object.put("size", this.size);
		object.put("date", this.date);
		object.put("price", this.price);
		object.put("images", this.images);
		object.put("platform", "天猫");
		return object.toString();
	}
	
	@Override
	public void startGenerate()
	{
//		generateId();
//		generateTitle();
//		generateColor();
//		generateDate();
//		generateImages();
		generatePrice();
//		generateSize();
//		generateDescription();

	}

	@Override
	public String getPageUrl() {
		// TODO Auto-generated method stub
		return this.pageUrl;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}

	@Override
	public String getPrice() {
		// TODO Auto-generated method stub
		return price;
	}

	@Override
	public String getColor() {
		// TODO Auto-generated method stub
		return color;
	}

	@Override
	public String getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public String getPlatform() {
		// TODO Auto-generated method stub
		return "天猫";
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return date;
	}
}