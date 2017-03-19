package com.wozipa.reptile.util;

import org.apache.derby.tools.sysinfo;

public class ReptileUtils {
	
	private static final String REPTILE_DATA="REPTILE_DATA";
	private static final String REPTILE_CONF="REPTILE_CONF";
	
	public static String GetDataPath()
	{
		return GetEnverimentProperty(REPTILE_DATA);
	}
	
	public static String GetConfPath()
	{
		 return GetEnverimentProperty(REPTILE_CONF);
	}
	
	public static String GetEnverimentProperty(String name)
	{
		String path=System.getProperty(name);
		if(path==null || path.isEmpty())
		{
			return System.getenv(name);
		}
		return path;
	}

}
