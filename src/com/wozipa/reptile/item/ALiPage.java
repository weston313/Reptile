package com.wozipa.reptile.item;

import java.io.IOException;
import java.util.Date;

import javax.swing.text.StyleConstants.ColorConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;

import com.ibm.icu.text.SimpleDateFormat;

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
	
//	public ALiPage(String pageUrl,String resultPath) {
//		// TODO Auto-generated constructor stub
//		this.pageUrl=pageUrl;
//		this.resultPath=resultPath;
//		Connection connection=Jsoup.connect(this.pageUrl);
//		try {
//			Response response=connection.execute();
//			if(response==null || response.statusCode()!=200)
//			{
//				LOG.info("cannot open the page url "+pageUrl);
//				return;
//			}
//			this.pageNode=response.parse();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
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
		Connection connection=Jsoup.connect(this.pageUrl);
		try {
			Response response=connection.execute();
			if(response==null || response.statusCode()!=200)
			{
				LOG.info("cannot open the page url "+pageUrl);
				return;
			}
			this.pageNode=response.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String paramLine=this.pageUrl.split("?")[1];
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
