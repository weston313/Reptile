package com.wozipa.reptile.item;

import static org.junit.Assert.*;

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

}
