package com.wozipa.reptile.shop;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class TaobaoCategoryPageTest {
	
	private static final Log LOG=LogFactory.getLog(TaobaoCategoryPageTest.class);

	@Test
	public void test() {
		TaobaoCategoryPage page=new TaobaoCategoryPage();
		page.setTask("https://shop73428660.taobao.com/category-1261961156.htm?spm=a1z10.5-c-s.w5002-15343728924.3.N8eqN6&search=y&catName=%CF%C4%BC%BE%BE%AD%B5%E4");
		String[] urls=page.getGoodUrls();
		for(String url:urls)
		{
			System.out.println("url is "+url);
		}
	}
	
	@Test
	public void testRegex()
	{
		String url="https://shop73428660.taobao.com/category-1261961156.htm?spm=a1z10.5-c-s.w5002-15343728924.3.N8eqN6&search=y&catName=%CF%C4%BC%BE%BE%AD%B5%E4";
		String regex="^(((http|https)://)(.*)(taobao.com))(.*)?";  //(\\w{1,})(taobao.com)
		Pattern pattern=Pattern.compile(regex);
		Matcher ma=pattern.matcher(url);
		if(ma.matches())
		{
			LOG.info("yes");
			System.out.println("yes");
		}
		else {
			System.out.println("no");
		}
		LOG.info(ma.group(1));
	}
	
	@Test
	public void testDeletePage()
	{
		TaobaoCategoryPage page=new TaobaoCategoryPage();
		String ulr=page.deletePageNo("https://shop58397358.taobao.com/i/asynSearch.htm?_ksTS=1489508567540_203&callback=jsonp204&mid=w-2731136021-0&wid=2731136021&path=/category-760948433.htm&spm=a1z10.1-c.w5002-2731136009.5.LnMx1K&search=y&catName=%BF%E3%D7%D3&catId=760948433&pageNo=3&scid=760948433");
		System.out.println(ulr);
	}

}
