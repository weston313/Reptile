package com.wozipa.replite.test;

import java.io.IOException;
import java.util.Set;

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
			HtmlPage tmallPage=webClient.getPage("https://detail.tmall.com/item.htm?spm=a3211.0-7143085.userDefined_1489153322243_11.18.01gUHE&id=545311091867&scene=taobao_shop&gccpm=13090938.600.2.subject-1106&sta=gccpm:13090938.600.2.subject-1106&track_params={%22gccpm%22:%2213090938.600.2.subject-1106%22}&skuId=3453324123795");
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
