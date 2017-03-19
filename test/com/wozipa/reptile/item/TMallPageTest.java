package com.wozipa.reptile.item;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class TMallPageTest {

	@Test
	public void test() {
		TMallPage page=new TMallPage();
		String pageUrl="https://detail.tmall.com/item.htm?spm=a3211.0-7143085.userDefined_1489153322243_11.18.01gUHE&id=545311091867&scene=taobao_shop&gccpm=13090938.600.2.subject-1106&sta=gccpm:13090938.600.2.subject-1106&track_params={%22gccpm%22:%2213090938.600.2.subject-1106%22}&skuId=3453324123795";
		String resultPath="E:\\Test\\Replite\\Result";
		page.setTask(pageUrl, resultPath,0);
		page.startGenerate();
		System.out.println(page.toJson());
	}
	
	@Test
	public void testGoodUrl()
	{
		try {
			Response response=Jsoup.connect("https://desc.alicdn.com/i4/540/540/544546067172/TB14iFXPFXXXXcyXVXX8qtpFXlX.desc%7Cvar%5Edesc%3Bsign%5Ed9fac8f672af986728f10e82e5c1212f%3Blang%5Egbk%3Bt%5E1489766493").execute();
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
}
