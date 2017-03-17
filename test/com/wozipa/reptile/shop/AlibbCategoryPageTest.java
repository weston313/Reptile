package com.wozipa.reptile.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class AlibbCategoryPageTest {

	@Test
	public void test() {
		AlibbCategoryPage page=new AlibbCategoryPage();
		page.setTask("https://olrik.1688.com/page/offerlist_14613257.htm?spm=a2615.7691456.0.0.PoE0n5&tradenumFilter=false&sampleFilter=false&mixFilter=false&privateFilter=false&mobileOfferFilter=%24mobileOfferFilter&groupFilter=false&sortType=tradenumdown&pageNum=1");
		String[] items=page.getGoodUrls();
		for(String item:items)
		{
			System.out.println(item);
		}
	}

}
