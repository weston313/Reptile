package com.wozipa.reptile.data;

public abstract class Connectin<T extends Data> {
	
	public static final String REPTILE_DATA="REPTILE_DATA";
	
	public abstract void write(Data data);
	
	public abstract void close();
	
	public String upOneCase(String str)
	{
		if(str==null || str.isEmpty())
		{
			return null;
		}
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}

}
