package com.wozipa.replite.app.task;

import java.io.File;
import java.util.UUID;

import org.junit.Test;
import com.wozipa.reptile.app.tab.ItemPageTabbar;
import com.wozipa.reptile.app.task.ItemTaskThread;
import com.wozipa.reptile.item.PageFactory;

public class TaskThreadTest {

	@Test
	public void test() {
		String[] urls=new String[]{"https://item.taobao.com/item.htm?spm=a230r.1.14.189.0qQiDe&id=542589151236&ns=1&abbucket=2#detail"};
		ItemTaskThread thread=new ItemTaskThread(UUID.randomUUID().toString(),urls,"E:\\Test\\Replite\\Result",PageFactory.TAOBAO,0);
		thread.start();
	}
	
	@Test
	public void testFile()
	{
		new File("file:\\E:\\Test\\Replite\\reptile.jar!\\config\\reptile.xml");
	}

}
