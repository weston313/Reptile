package com.wozipa.replite.data;

import org.junit.Test;

import com.wozipa.reptile.data.ConnManager;
import com.wozipa.reptile.data.Connectin;
import com.wozipa.reptile.data.Data;
import com.wozipa.reptile.data.file.IdFileData;

public class ConnManagerTest {

	@Test
	public void test() {
		ConnManager manager=ConnManager.getInstance();
		Connectin connectin=manager.createConnction(IdFileData.class);
		Data data=new IdFileData("new_id_1","good_id_1");
		connectin.write(data);
		connectin.close();
	}
	
	

}
