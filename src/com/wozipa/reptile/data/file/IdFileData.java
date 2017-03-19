package com.wozipa.reptile.data.file;

import java.lang.reflect.Field;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wozipa.reptile.data.Data;

public class IdFileData extends Data{
	
	private static final Log LOG=LogFactory.getLog(IdFileData.class);
	
	public static final String FILE_NAME="id.xml";
	
	public IdFileData(){}
	
	public IdFileData(String id,String goodId)
	{
		this.id=id;
		this.goodId=goodId;
	}
	
	private String id;
	private String goodId;

	public String getId() {
		LOG.info("return the id "+this.id);
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGoodId() {
		return goodId;
	}

	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	
	@Override
	public Element toXml() {
		// TODO Auto-generated method stub
		Element rowElement=DocumentHelper.createElement(ROW_QNAME);
		rowElement.addElement("id").setText(this.id);
		rowElement.addElement("goodId").setText(this.goodId);
		return rowElement;
	}
}
