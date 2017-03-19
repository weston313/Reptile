package com.wozipa.reptile.shop;

import com.wozipa.reptile.item.ALiPage;
import com.wozipa.reptile.item.Page;
import com.wozipa.reptile.item.TMallPage;
import com.wozipa.reptile.item.TaoaBaoPage;

public class ShopFactory {
	
	public static final String TMALL="tmall";
	public static final String TAOBAO="taobao";
	public static final String ALIBABA="alibb";
	
	public static CategoryPage GetPage(String type)
	{
		CategoryPage page=null;
		switch (type) {
		case TMALL:
			page=new TMallCategoryPage();
			break;
		case TAOBAO:
			page=new TaobaoCategoryPage();
			break;
		case ALIBABA:
			page=new AlibbCategoryPage();
			break;
		}
		return page;
	}

}
