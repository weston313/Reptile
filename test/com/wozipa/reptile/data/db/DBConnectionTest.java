package com.wozipa.reptile.data.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class DBConnectionTest {

	@Test
	public void test() {
		DBConnection connection=new DBConnection();
		if(!connection.tableExist())
		{
			connection.createTable();
		}
		IdDBData data=new IdDBData("testEn","testOg","E:\\Test");
//		connection.write(data);
		List<IdDBData> datas=connection.search("test");
		for(IdDBData d:datas)
		{
			System.out.println(d.toString());
		}
		connection.close();
	}

}
