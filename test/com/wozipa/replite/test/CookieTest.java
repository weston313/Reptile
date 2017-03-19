package com.wozipa.replite.test;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class CookieTest {
	
	@Test
	public void test()
	{
		
	}
	
	@Test
	public void testGetCookie()
	{
		WebClient webClient=new WebClient(BrowserVersion.FIREFOX_38);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setTimeout(10000);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setRedirectEnabled(true);
		try {
			HtmlPage page=webClient.getPage("https://login.taobao.com/member/login.jhtml");
			webClient.waitForBackgroundJavaScript(10000);
			webClient.setJavaScriptTimeout(0);
			//
			page.getElementById("TPL_username_1").setNodeValue("wzp06354");
			page.getElementById("TPL_password_1").setNodeValue("Wozipa@313#tb");
			WebResponse response=page.getElementById("J_SubmitStatic").click().getWebResponse();
			//
			HtmlPage tmallPage=webClient.getPage("https://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.16.LGlnqu&id=42172636647&skuId=86924828463&areaId=110100&user_id=2271751309&cat_id=2&is_b=1&rn=2d64f2b937d94e14578888ec78fe5648");
			Element pageNode=Jsoup.parse(tmallPage.asXml());
			Elements elemenets=pageNode.select("ul[class=tm-clear J_TSaleProp]");
			System.out.println(elemenets.size());
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
