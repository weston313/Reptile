package com.wozipa.reptile.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wozipa.reptile.data.file.FileConnection;
import com.wozipa.reptile.data.file.IdFileData;

public class ConnManager {
	
	public static final Log LOG=LogFactory.getLog(ConnManager.class);
	
	
	private static ConnManager manager=null;
	
	public static ConnManager getInstance()
	{
		if(manager==null)
		{
			synchronized (ConnManager.class) {
				if(manager==null)
				{
					manager=new ConnManager();
				}
			}
		}
		return manager;
	}
	
	private Map<Class<? extends Data>,Connectin<Data>> connPool=null;
	
	private ConnManager()
	{
		this.connPool=new HashMap<>();
		this.connPool.put(IdFileData.class,new FileConnection<>(IdFileData.FILE_PATH, IdFileData.class));
	}
	
	public Connectin<Data> getConnection(Class<? extends Data> cls)
	{
		if(connPool.containsKey(cls))
		{
			return connPool.get(cls);
		}
		return null;
	}
	
	public Connectin<? extends Data> createConnction(Class<? extends Data> cls)
	{
		if(connPool.containsKey(cls))
		{
			return connPool.get(cls);
		}
		if(cls==IdFileData.class)
		{
			return new FileConnection(IdFileData.FILE_PATH,IdFileData.class);
		}
		return null;
	}
}
