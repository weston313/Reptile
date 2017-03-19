package com.wozipa.reptile.item;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class ALiPageTest {

	@Test
	public void test() {
		ALiPage page=new ALiPage();
		String pageUrl="https://detail.1688.com/offer/535876137683.html?position=top";
		String resultPath="E:\\Test\\Replite\\Result";
		page.setTask(pageUrl,resultPath, 0);
		page.startGenerate();
		System.out.println(page.toJSON());
	}
	
	@Test
	public void testGoodsUrl()
	{
		try {
			Response response=Jsoup.connect("https://img.alicdn.com/tfscom/TB1wmZ8PVXXXXbiXXXXXXXXXXXX").execute();
			Element element=response.parse();
			Elements elements=element.getElementsByTag("img");
			for(int i=0;i<elements.size();i++)
			{
				System.out.println(elements.get(i).outerHtml());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testUrlRegex()
	{
		String url="https://detail.1688.com/offer/535876137683.html?position=top";
		String regex="(https://detail.1688.com/offer/)([0-9]{1,})";
		Pattern pattern=Pattern.compile(regex);
		Matcher matcher=pattern.matcher(url);
		if(matcher.find())
		{
			System.out.println(matcher.group(2));
		}
	}

}
