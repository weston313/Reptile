package com.wozipa.replite.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.junit.Test;

public class SepartorRegexTest {
	
	@Test
	public void test()
	{
		String regex="[,;\r\n]{1,}";
		String url="https://detail.tmall.com/item.htm?spm=a1z10.4-b-s.w5003-15793859010.1.MbVlgg&id=544546067172&scene=taobao_shop\r\nhttps://detail.tmall.com/item.htm?spm=a1z10.4-b-s.w5003-15793859010.1.MbVlgg&id=544546067172&scene=taobao_shop\r\nhttps://detail.tmall.com/item.htm?spm=a1z10.4-b-s.w5003-15793859010.1.MbVlgg&id=544546067172&scene=taobao_shop";
		String[] parts=url.split(regex);
		for(String part:parts)
		{
			System.out.println(part);
		}
	}
	
	@Test
	public void testFile(){
		File file=new File("file:\\E:\\Test\\Replite\\reptile.jar!\\config\\reptile-default.xml");
		try {
			FileOutputStream fileOutputStream=new FileOutputStream("file:\\E:\\Test\\Replite\\reptile.jar!\\config\\reptile-default.xml");
			SepartorRegexTest.class.getResource("file:\\E:\\Test\\Replite\\reptile.jar!\\config\\reptile-default.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
