package com.wozipa.reptile.item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.wozipa.reptile.data.ConnManager;
import com.wozipa.reptile.data.Connectin;
import com.wozipa.reptile.data.file.IdFileData;
import com.wozipa.reptile.id.encrypt.Encrypt;
import com.wozipa.reptile.id.encrypt.EncryptUtil;

import net.sf.json.JSONObject;

public class TaoaBaoPage extends Page{
	
	private static final Log LOG=LogFactory.getLog(TaoaBaoPage.class);
	
	private static final Map<String,String> TB_PAGE_CONF=new HashMap<>();
	private static final String TB_CONF_FILE="taobao.xml";
	
	//商品照片
	private static final String IMAGES_CONTAINER="taobao.images.container";
	private static final String IMAGES_NODE="taobao.images.node";
	private static final String IMAGES_VALUE="taobao.images.value";
	private static final String IMAGES_THUMB="taobao.images.thumb";
	private static final String IMAGES_ZOOM="taobao.images.zoom";
	
	//获取商品标题
	private static final String TITLE_CONTAINER="taobao.title.container";
	private static final String TITLE_NODE="taobao.title.node";
	private static final String TITILE_VALUE="taobao.title.value";
	
	//获取商品价格
	private static final String PRICE_CONTAINER="taobao.price.container";
	private static final String PRICE_NODE="taobao.price.node";
	private static final String PRICE_VALUE="taobao.price.value";
	
	private static final String PRICE_PROMO_CONTAINER="taobao.promo.container";
	private static final String PRICE_PROMO_NODE="taobao.promo.node";
	private static final String PRICE_PROMO_VALUE="taobao.promo.value";
	
	//获取商品大小值需要使用的变量
	private static final String SIZE_CONTAINER="taobao.size.container";
	private static final String SIZE_NODE="taobao.size.node";
	private static final String SIZE_VALUE="taobao.size.value";
	
	private static final String COLOR_CONTAINER="taobao.color.container";
	private static final String COLOR_NODE="taobao.color.node";
	private static final String COLOR_VALUE="taobao.color.value";
	
	private static final String DESC_URL_REGEX="taobao.desc.url.regex";
	private static final String DESC_VALUE="taobao.desc.value";
	
	private static final String TAOBAO_BASE_URI="https://item.taobao.com";
	
	private org.jsoup.nodes.Element pageNode;
	
	private String id;
	private String pageUrl;
	private String title;
	private String date;
	private String size;
	private String color;
	private String images;
	private String price;
	private String resultPath;
	
	private int idEncrypt;
	
	private int imageCount=0;
	
	public TaoaBaoPage()
	{
		LOG.info("initlize the taobao page");
		configuration=PageConfiguration.getInstance();
	}
	
	public void setTask(String pageUrl, String resultPath,int encrypt) 
	{
		this.pageUrl=pageUrl;
		this.resultPath=resultPath;
		this.idEncrypt=encrypt;
		//
		WebClient webClient=new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setTimeout(10000);
		//
		webClient.getCookieManager().setCookiesEnabled(true);
		try {
			HtmlPage page=webClient.getPage(this.pageUrl);
			webClient.waitForBackgroundJavaScript(10000);
			webClient.setJavaScriptTimeout(0);
			String pageXml=page.asXml();
			//
			this.pageNode=Jsoup.parse(pageXml, TAOBAO_BASE_URI);
			if(this.pageNode==null){
				LOG.info("the taobao page node is null");
			}
		} catch (FailingHttpStatusCodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//
		
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
			if(param.contains("id="))
			{
				idValue=param.split("=")[1];
			}
		}
		//进行加密,然后将数据封装到任务中
		this.id=EncryptUtil.encrypt(this.idEncrypt, idValue);
		ConnManager connManager=ConnManager.getInstance();
		Connectin connectin=connManager.getConnection(IdFileData.class);
		connectin.write(new IdFileData(this.id,idValue));
	}

	@Override
	public void generateTitle() {
		// TODO Auto-generated method stub
		LOG.info("start to generate title");
		if(this.pageNode==null){
			LOG.info("this page node is null");
		}
		String value=generateValue(this.pageNode,TITLE_CONTAINER, TITLE_NODE, TITILE_VALUE);
		this.title=value;
	}

	@Override
	public void generateSize() {
		// TODO Auto-generated method stub
		LOG.info("start to generate the size");
		String sizeValue=generateValue(this.pageNode,SIZE_CONTAINER, SIZE_NODE, SIZE_VALUE);
		this.size=sizeValue;
	}

	@Override
	public void generateColor() {
		// TODO Auto-generated method stub
		LOG.info("start to generate the color");
		String colorValue=generateValue(this.pageNode,COLOR_CONTAINER, COLOR_NODE, COLOR_VALUE);
		this.color=colorValue;
	}

	@Override
	public void generateDate() {
		// TODO Auto-generated method stub
		LOG.info("start to generate the date");
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
		LOG.info("start to generate the images");
		Key containerKey=configuration.getKey(IMAGES_CONTAINER);
		Element containerNode=getElement(this.pageNode,containerKey);
		if(containerNode==null)
		{
			LOG.info("the container node is null");
			containerNode=this.pageNode;
		}
		//
		Key nodeKey=configuration.getKey(IMAGES_NODE);
		Element node=getElement(containerNode,nodeKey);
		if(node==null)
		{
			LOG.info("the node is null");
			node=this.pageNode;
		}
		//
		Key valueKey=configuration.getKey(IMAGES_VALUE);
		List<Element> valuesNode=getElements(node,valueKey);
		//
		String thumnStr=configuration.getKey(IMAGES_THUMB).getVlaue();
		String zoomStr=configuration.getKey(IMAGES_ZOOM).getVlaue();
		LOG.info("the size of the image is "+valuesNode.size());
		for(Element image:valuesNode)
		{
			String srcUrl="http:"+image.attr("data-src");
			if(srcUrl.contains(thumnStr))
			{
				srcUrl=srcUrl.replaceAll(thumnStr, zoomStr);
			}
			//
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
	 * @author wozip
	 * @date 2017-3-5
	 * @see 下载图片
	 * @param urlStr
	 * @param name
	 */
	public void downloadImage(String urlStr,String name)
	{
		try {
			LOG.info("start to download the iamge of "+urlStr);
			URL url=new URL(urlStr);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			InputStream reader=connection.getInputStream();
			LOG.info(this.resultPath+"/"+name);
			File image=new File(this.resultPath+"\\"+name+".jpg");
			image.createNewFile();
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
	
	/**
	 * @author wozipa
	 * @date 2017-3-11 
	 * @see 讲商品描述的图片
	 */
	public void generateDescription()
	{
		LOG.info("start to generate description");
		String reg="script[src~=(//desc.alicdn.com/)(.*)$]";
		Key descRegexKey=configuration.getKey(DESC_URL_REGEX);
		Elements elements=this.pageNode.select(descRegexKey.getVlaue());
		String descUrl=elements.first().attr("src");
		if(!descUrl.startsWith("http:"))
		{
			LOG.info("add prefix");
			descUrl="http:"+descUrl;
		}
		//
		LOG.info("load the page "+descUrl);
		Connection connection=Jsoup.connect(descUrl);
		try {
			Document root=connection.execute().parse();
			Elements imgs=root.getElementsByTag(configuration.getKey(DESC_VALUE).getVlaue());
			for(int i=0;i<imgs.size();i++)
			{
				System.out.println(imgs.attr("src"));
				Element img=imgs.get(i);
				String imgUrl=img.attr("src");
				String name="image";
				if(this.imageCount>0)
				{
					name=name+"_0"+this.imageCount;
				}
				downloadImage(imgUrl,name);
				this.imageCount++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void startGenerate()
	{
		generateId();
		generateTitle();
		generateColor();
		generateDate();
		generatePrice();
		generateSize();
		generateImages();
//		generateDescription();
		System.out.println(this.toJSON());
	}
	
	public String toJSON()
	{
		JSONObject object=new JSONObject();
		object.put("id", this.id);
		object.put("title", this.title);
		object.put("price", this.price);
		object.put("color", this.color);
		object.put("size", this.size);
		object.put("date", this.date);
		object.put("image", this.images);
		object.put("pageUrl", this.pageUrl);
		return object.toString();
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
		return "淘宝";
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return date;
	}
}
