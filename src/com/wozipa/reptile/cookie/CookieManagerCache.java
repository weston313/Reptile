package com.wozipa.reptile.cookie;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.wozipa.reptile.app.config.AppConfiguration;

public class CookieManagerCache {
	
	public static final Log LOG=LogFactory.getLog(CookieManagerCache.class);
	
	public volatile static CookieManagerCache cache=null;
	
	public static CookieManagerCache getCache()
	{
		if(cache==null)
		{
			synchronized (CookieManagerCache.class) {
				if(cache==null)
				{
					cache=new CookieManagerCache();
				}
			}
		}
		return cache;
	}
	
	private CookieManager cookieManager;
	
	private CookieManagerCache()
	{
		AppConfiguration configuration=AppConfiguration.getConfiguration();
		String username=configuration.getKey(AppConfiguration.COOKIE_USERNAME).getVlaue();
		String password=configuration.getKey(AppConfiguration.COOKIE_PASSWORD).getVlaue();
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
			HtmlPage taobaoPage=webClient.getPage("https://www.taobao.com/");
			webClient.waitForBackgroundJavaScript(10000);
			webClient.setJavaScriptTimeout(0);
			cookieManager=webClient.getCookieManager();
		} catch (FailingHttpStatusCodeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CookieManager getCookieManager()
	{
		return cookieManager;
	}

}
