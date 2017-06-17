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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.ibm.icu.text.SimpleDateFormat;
import com.wozipa.reptile.app.config.Key;
import com.wozipa.reptile.cookie.CookieManagerCache;
import com.wozipa.reptile.data.ConnManager;
import com.wozipa.reptile.data.Connectin;
import com.wozipa.reptile.data.db.DBConnection;
import com.wozipa.reptile.data.db.IdDBData;
import com.wozipa.reptile.data.file.IdFileData;
import com.wozipa.reptile.id.encrypt.EncryptUtil;

import net.sf.json.JSONObject;

public class TMallPage extends Page{
	
	private static final Log LOG=LogFactory.getLog(TMallPage.class);
	
	private static final String PREFIX_ID="M";
	
	//商品ID
	private static final String ID_REGEX="tmall.id.regex";
	
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
	private static final String SIZE_NODE = "tmall.size.node";
	private static final String SIZE_VALUE = "tmall.size.value";

	private static final String COLOR_CONTAINER = "tmall.color.container";
	private static final String COLOR_NODE = "tmall.color.node";
	private static final String COLOR_VALUE = "tmall.color.value";
	
	private static final String DESC_URL_CONTAINER="tmall.desc.url.container";
	private static final String DESC_URL_KEYWORK="tmall.desc.url.keyword";
	private static final String DESC_URL_SPLIT="tmall.desc.url.split";
	private static final String DESC_URL_DOMAIN="tmall.desc.url.domain";
	private static final String DESC_VALUE_REGEX="tmall.desc.value";
	
	protected Element pageNode;
	
	private String id;
	private String pageUrl;
	private String title;
	private String price;
	private String size;
	private String color;
	private String images;
	private String date;
	
	private int imageCount=0;
	private String resultPath;
	private int encrypt;
	private String taskId;
	
	
	public TMallPage(String task) {
		// TODO Auto-generated constructor stub
		configuration=PageConfiguration.getInstance();
		this.taskId=task;
	}
	
	@Override
	public void setTask(String pageUrl, String resultPath,int encrypt) {
		// TODO Auto-generated method stub
		this.pageUrl=pageUrl;
		this.resultPath=resultPath;
		this.encrypt=encrypt;
		//
		WebClient webClient=new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setTimeout(10000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.setCookieManager(CookieManagerCache.getCache().getCookieManager());
		try {
			HtmlPage page=webClient.getPage(this.pageUrl);
			webClient.waitForBackgroundJavaScript(10000);
			webClient.setJavaScriptTimeout(0);
			this.pageNode=Jsoup.parse(page.asXml());
		} catch (FailingHttpStatusCodeException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void generateId() {
		// TODO Auto-generated method stub
		String idValue=null;
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
			if(param.startsWith("id="))
			{
				idValue=param.split("=")[1];
			}
		}
		//
		this.id=PREFIX_ID+EncryptUtil.encrypt(this.encrypt, idValue);
//		ConnManager connManager=ConnManager.getInstance();
//		Connectin connectin=connManager.getConnection(IdFileData.class);
//		connectin.write(new IdFileData(this.id,idValue));
		DBConnection connection=DBConnection.GetDataBase();
		connection.write(new IdDBData(this.taskId,this.id,idValue,resultPath));
//		connection.close();
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
		LOG.info("start to generate size");
		LOG.info(this.pageNode.outerHtml());
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
		for(Element image:valuesNode)
		{
			String srcUrl=image.attr("src");
			if(srcUrl.contains(thumnStr))
			{
				srcUrl=srcUrl.replaceAll(thumnStr, zoomStr);
			}
			//
			if(srcUrl.startsWith("//"))
			{
				srcUrl="http:"+srcUrl;
			}
			else if(srcUrl.startsWith("http://"))
			{
				srcUrl="http://"+srcUrl;
			}
			String name=this.id;
			if(this.imageCount>0)
			{
				name=name+"_0"+this.imageCount;
			}
			downloadImage(srcUrl, name);
			this.imageCount++;
		}
	}
	
	/**
	 * @author wozipa
	 * @date 2017-3-11 
	 * @see 讲商品描述的图片
	 */
	public void generateDescription()
	{
		Key jsContainerKey=configuration.getKey(DESC_URL_CONTAINER);
		List<Element> jsElements=getElements(this.pageNode,jsContainerKey);//this.pageNode.getElementsByTag("script");
		String jsCode=null;
		Key jsKeywordKey=configuration.getKey(DESC_URL_KEYWORK);
		for(int i=0;i<jsElements.size();i++)
		{
			String html=jsElements.get(i).html();
			if(html.toLowerCase().contains(jsKeywordKey.getVlaue()))
			{
				jsCode=html;
				break;
			}
		}
		//
		String descUrl=null;
		Key jsSplitKey=configuration.getKey(DESC_URL_SPLIT);
		String[] parts=jsCode.split(jsSplitKey.getVlaue());
		Key urlDomainKey=configuration.getKey(DESC_URL_DOMAIN);
		for(String part:parts)
		{
			if(part.contains(urlDomainKey.getVlaue()))
			{
				descUrl=part;
			}
		}
		System.out.println(descUrl);
		if(descUrl.startsWith("//"))
		{
			descUrl="https:"+descUrl;
		}
		else if(!descUrl.startsWith("https")){
			descUrl="https://"+descUrl;
		}
		try {
			Response response=Jsoup.connect(descUrl).execute();
			Element document=response.parse();
			Key key=configuration.getKey(DESC_VALUE_REGEX);
			Elements imgs=document.select(key.getVlaue());
			for(int i=0;i<imgs.size();i++)
			{
				Element imgNode=imgs.get(i);
				String imgUrl=imgNode.attr("src");
				imgUrl=correctUrl(imgUrl);
				String name=id;
				if(name==null || name.isEmpty())
				{
					name="img";
				}
				if(this.imageCount>0)
				{
					name=name+"_"+this.imageCount;
				}
				downloadImage(imgUrl,name);
				this.imageCount++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			LOG.info(this.resultPath+"\\"+name+".jpg");
			File image=new File(this.resultPath+"\\"+name+".jpg");
			if(!image.exists())
			{
				image.createNewFile();
			}
			FileOutputStream writer=new FileOutputStream(image);
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
		generateId();
		generateTitle();
		generateColor();
		generateDate();
		generateImages();
		generatePrice();
		generateSize();
		generateDescription();
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
