package com.wozipa.reptile.shop;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TMallCategoryPageTest {

	@Test
	public void test() {
		TMallCategoryPage page=new TMallCategoryPage();
		page.setTask("https://inman.tmall.com/category-1055517814.htm?spm=a1z10.1-b-s.w12033434-15916696458.9.FC2u8H&tsearch=y&scene=taobao_shop#TmshopSrchNav");
		String[] goodUrls=page.getGoodUrls();
		for(String goodUrl:goodUrls)
		{
			System.out.println(goodUrl);
		}
	}
	
	@Test
	public void testRegex()
	{
		String regex="^(((http|https)://)(.*)(tmall.com))(.*)?";
		String url="https://inman.tmall.com/category-1055517814.htm?spm=a1z10.1-b-s.w12033434-15916696458.9.FC2u8H&tsearch=y&scene=taobao_shop#TmshopSrchNav";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(url);
		if(matcher.find())
		{
			System.out.println(matcher.group(1));
		}
		else{
			System.out.println("not find");
		}
	}

}
