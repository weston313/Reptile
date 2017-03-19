package com.wozipa.reptile.item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ibm.icu.text.SimpleDateFormat;
import com.wozipa.reptile.app.config.Key;
import com.wozipa.reptile.cookie.CookieManagerCache;
import com.wozipa.reptile.data.ConnManager;
import com.wozipa.reptile.data.Connectin;
import com.wozipa.reptile.data.file.IdFileData;
import com.wozipa.reptile.id.encrypt.EncryptUtil;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;


public class ALiPage extends Page{
	
	private static final Log LOG=LogFactory.getLog(ALiPage.class);
	// 商品照片
	private static final String IMAGES_CONTAINER = "alibb.images.container";
	private static final String IMAGES_NODE = "alibb.images.node";
	private static final String IMAGES_VALUE = "alibb.images.value";
	
	private static final String ID_REGEX="alibb.id.regex";

	// 获取商品标题
	private static final String TITLE_CONTAINER = "alibb.title.container";
	private static final String TITLE_NODE = "alibb.title.node";
	private static final String TITILE_VALUE = "alibb.title.value";

	// 获取商品价格
	private static final String PRICE_CONTAINER = "alibb.price.container";
	private static final String PRICE_NODE = "alibb.price.node";
	private static final String PRICE_VALUE = "alibb.price.value";

	// 获取商品大小值需要使用的变量
	private static final String SIZE_CONTAINER = "alibb.size.container";
	private static final String SIZE_NODE = "alibb.size.node";
	private static final String SIZE_VALUE = "alibb.size.value";

	private static final String COLOR_CONTAINER = "alibb.color.container";
	private static final String COLOR_NODE = "alibb.color.node";
	private static final String COLOR_VALUE = "alibb.color.value";
	
	private static final String DESC_URL_CONTAINER="alibb.desc.url.contianer";
	private static final String DESC_URL_ATTR="alibb.desc.url.attr";
	private static final String DESC_GOODS_NODE="alibb.desc.goods.node";
	
	protected Element pageNode;
	
	private String id;
	private String pageUrl;
	private String title;
	private String price;
	private String color;
	private String size;
	private String date;
	private String platform;
	private String images;
	
	private int imageCuont=0;
	private String resultPath;
	private int encrypt;
	
	public ALiPage() {
		// TODO Auto-generated constructor stub
		configuration=PageConfiguration.getInstance();
		platform="阿里巴巴";
	}
	
	/**
	 * @author wozipa
	 * @date 2017-3-6
	 * @see 设置task信息
	 */
	public void setTask(String pageUrl, String resultPath,int encrypt) 
	{
		this.pageUrl=pageUrl;
		this.resultPath=resultPath;
		this.encrypt=encrypt;
		//
		WebClient webClient=new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setTimeout(10000);
		//
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.setCookieManager(CookieManagerCache.getCache().getCookieManager());
		try {
			HtmlPage page=webClient.getPage(this.pageUrl);
			webClient.waitForBackgroundJavaScript(10000);
			webClient.setJavaScriptTimeout(0);
			String pageXml=page.asXml();
			//
			this.pageNode=Jsoup.parse(pageXml);
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
	}

	

	@Override
	public void generateId() {
		// TODO Auto-generated method stub
		String idValue=null;
		String regex=configuration.getKey(ID_REGEX).getVlaue();
		System.out.println(regex);
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(this.pageUrl);
		if(matcher.find())
		{
			idValue=matcher.group(2);
			LOG.info(idValue);
		}
		
		//
		this.id=EncryptUtil.encrypt(this.encrypt, idValue);
//		ConnManager connManager=ConnManager.getInstance();
//		Connectin connectin=connManager.getConnection(IdFileData.class);
//		connectin.write(new IdFileData(this.id,idValue));
	}

	@Override
	public void generateTitle() {
		// TODO Auto-generated method stub
		String value=generateValue(this.pageNode,TITLE_CONTAINER, TITLE_NODE, TITILE_VALUE);
		this.title=value;
	}

	@Override
	public void generateSize() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		Key containerKey=configuration.getKey(SIZE_CONTAINER);
		Element containerNode=getElement(this.pageNode, containerKey);
		if(containerNode==null)
		{
			containerNode=this.pageNode;
		}
		//
		Key nodeKey=configuration.getKey(SIZE_NODE);
		Key valueKey=configuration.getKey(SIZE_VALUE);
		List<Element> nodes=getElements(containerNode, nodeKey);
		for(int i=0;i<nodes.size();i++)
		{
			Element node=nodes.get(i);
			Element valueNode=getElement(node,valueKey);
			String size=valueNode.text();
			sb.append(size);
			if(i<nodes.size()-1)
			{
				sb.append(";");
			}
		}
		
		this.size=sb.toString();
	}

	@Override
	public void generateColor() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		Key containerKey=configuration.getKey(COLOR_CONTAINER);
		Element containerNode=getElement(this.pageNode, containerKey);
		if(containerNode==null)
		{
			LOG.info("the container node is null");
			containerNode=this.pageNode;
		}
		//
		Key nodeKey=configuration.getKey(COLOR_NODE);
		Key valueKey=configuration.getKey(COLOR_VALUE);
		List<Element> nodes=getElements(containerNode, nodeKey);
		for(int i=0;i<nodes.size();i++)
		{
			Element node=nodes.get(i);
			Element valueNode=getElement(node,valueKey);
			sb.append(valueNode.attr("alt"));
			if(i<nodes.size()-1)
			{
				sb.append(";");
			}
		}
		this.color=sb.toString();
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
		String price=generateValue(this.pageNode, PRICE_CONTAINER, PRICE_NODE, PRICE_VALUE);
		this.price=price;
	}

	@Override
	public void generateImages() {
		// TODO Auto-generated method stub
		
	}
	
	private void generateDescription() {
		// TODO Auto-generated method stub
		Key descUrlContanerKey=configuration.getKey(DESC_URL_CONTAINER);
		Element descUrlContainerNode=getElement(this.pageNode, descUrlContanerKey);
		if(descUrlContainerNode==null)
		{
			LOG.info("the container is null");
			descUrlContainerNode=this.pageNode;
		}
		//
		Key descUrlAttrKey=configuration.getKey(DESC_URL_ATTR);
		String url=descUrlContainerNode.attr(descUrlAttrKey.getVlaue());
		System.out.println(url);
		//
		try {
			Response response=Jsoup.connect(url).execute();
			Element imagesNode=response.parse();
			Key goodImgNodeKey=configuration.getKey(DESC_GOODS_NODE);
			List<Element> imgs=getElements(imagesNode,goodImgNodeKey);
			for(int i=0;i<imgs.size();i++)
			{
				Element img=imgs.get(i);
				String imgUrl=img.attr("src");
				imgUrl=correctUrl(imgUrl);
				String name=this.id;
				if(name==null || name.isEmpty())
				{
					name="img";
				}
				if(this.imageCuont>0)
				{
					name=name+"_"+this.imageCuont;
				}
				downloadImage(imgUrl, name);
				this.imageCuont++;
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
			LOG.info("start to download the iamge of "+urlStr);
			URL url=new URL(urlStr);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			InputStream reader=connection.getInputStream();
			LOG.info(this.resultPath+"/"+name);
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



	@Override
	public void startGenerate()
	{
		generateId();
		generateTitle();
		generatePrice();
		generateColor();
		generateDate();
//		generateImages();
		generateSize();
		generateDescription();
		
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
		object.put("platform", this.platform);
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
		return "天猫";
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return date;
	}
	
	

}
