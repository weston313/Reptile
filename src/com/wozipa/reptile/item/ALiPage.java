package com.wozipa.reptile.item;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;

import javax.swing.text.StyleConstants.ColorConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.ibm.icu.text.SimpleDateFormat;
import com.wozipa.reptile.cookie.CookieManagerCache;
import com.wozipa.reptile.data.ConnManager;
import com.wozipa.reptile.data.Connectin;
import com.wozipa.reptile.data.file.IdFileData;
import com.wozipa.reptile.id.encrypt.EncryptUtil;

import org.jsoup.Jsoup;

public class ALiPage extends Page{
	
	private static final Log LOG=LogFactory.getLog(ALiPage.class);
	// 商品照片
	private static final String IMAGES_CONTAINER = "alibb.images.container";
	private static final String IMAGES_NODE = "alibb.images.node";
	private static final String IMAGES_VALUE = "alibb.images.value";

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
	private static final String SIZE_NODE = "alibb.price.node";
	private static final String SIZE_VALUE = "alibb.price.value";

	private static final String COLOR_CONTAINER = "alibb.color.container";
	private static final String COLOR_NODE = "alibb.color.node";
	private static final String COLOR_VALUE = "alibb.color.value";
	
	protected Element pageNode;
	
	private String id;
	private String pageUrl;
	private String title;
	private String price;
	private String color;
	private String size;
	private String date;
	private String images;
	
	private String resultPath;
	private int encrypt;
	
	public ALiPage() {
		// TODO Auto-generated constructor stub
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
		if(this.pageUrl==null || pageUrl.isEmpty())
		{
			LOG.info("the page url is empty");
			return;
		}
		//
		String paramLine=this.pageUrl.split("?")[1];
		String[] params=paramLine.split("&");
		for(String param:params)
		{
			if(param.contains("id="))
			{
				this.id=param.split("=")[1];
			}
		}
		//
		this.id=EncryptUtil.encrypt(this.encrypt, idValue);
		ConnManager connManager=ConnManager.getInstance();
		Connectin connectin=connManager.getConnection(IdFileData.class);
		connectin.write(new IdFileData(this.id,idValue));
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
		String value=generateValue(this.pageNode,SIZE_CONTAINER, SIZE_NODE, SIZE_VALUE);
		this.size=value;
	}

	@Override
	public void generateColor() {
		// TODO Auto-generated method stub
		String color=generateValue(this.pageNode,COLOR_CONTAINER,COLOR_NODE,COLOR_VALUE);
		this.color=color;
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
		
	}

	@Override
	public void generateImages() {
		// TODO Auto-generated method stub
		String priceValue=generateValue(this.pageNode,PRICE_CONTAINER, PRICE_NODE, PRICE_VALUE);
		this.price=priceValue;
	}



	@Override
	public void startGenerate()
	{
		generateId();
		generateColor();
		generateDate();
		generateImages();
		generatePrice();
		generateSize();
		generateTitle();
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
