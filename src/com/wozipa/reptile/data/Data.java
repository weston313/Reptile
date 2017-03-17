package com.wozipa.reptile.data;

import java.lang.reflect.Field;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public abstract class Data {
	
	public static final String ROW_QNAME="row";
	
	public abstract Element toXml();

}
