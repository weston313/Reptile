package com.wozipa.reptile.data.db;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.sun.jna.platform.win32.OaIdl.IDLDESC;
import com.wozipa.reptile.data.Data;

public class IdDBData extends Data{
	
	private static final Logger LOGGER=Logger.getLogger(IdDBData.class);
	
	private String id;
	private String task;
	private String enId;
	private String ogId;
	private String result;
	
	public IdDBData() {
		// TODO Auto-generated constructor stub
	}
	
	public IdDBData(String task,String enId,String ogId,String result)
	{
		this(System.currentTimeMillis()+"",task,enId,ogId,result);
	}
	
	public IdDBData(String id,String task,String enId,String ogId,String result){
		this.id=id;
		this.task=task;
		this.enId=enId;
		this.ogId=ogId;
		this.result=result;
	}
	
	public String getEnId() {
		return enId;
	}

	public void setEnId(String enId) {
		this.enId = enId;
	}

	public String getOgId() {
		return ogId;
	}

	public void setOgId(String ogId) {
		this.ogId = ogId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public Element toXml() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "["+enId+","+ogId+","+result+"]";
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public String getTask()
	{
		return this.task;
	}

}
