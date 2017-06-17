package com.wozipa.replite.app.staff;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wozipa.reptile.app.tab.ItemPageTabbar;
import com.wozipa.reptile.item.TaoaBaoPage;

public class TaoaBaoPageTest {

	@Test
	public void test() {
		TaoaBaoPage page=new TaoaBaoPage("");
		page.setTask("https://item.taobao.com/item.htm?spm=a230r.1.14.189.0qQiDe&id=542589151236&ns=1&abbucket=2#detail","E:\\Test\\Replite\\Result",0);
		page.startGenerate();
	}

}
