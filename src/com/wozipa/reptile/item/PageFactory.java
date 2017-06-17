package com.wozipa.reptile.item;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PageFactory {
	
	private static final Log LOG=LogFactory.getLog(PageFactory.class);
	
	public static final String TMALL="tmall";
	public static final String TAOBAO="taobao";
	public static final String ALIBABA="alibb";
	
	public static Page GetPage(String type,String taskId)
	{
		Page page=null;
		switch (type) {
		case TMALL:
			page=new TMallPage(taskId);
			break;
		case TAOBAO:
			page=new TaoaBaoPage(taskId);
			break;
		case ALIBABA:
			page=new ALiPage(taskId);
			break;
		}
		return page;
	}
}
