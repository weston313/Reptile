package com.wozipa.replite.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDate {
	
	public static void main(String[] args)
	{
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		System.out.println(sdf.format(date));
	}

}
