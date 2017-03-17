package com.wozipa.reptile.app.tab;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

public class SearchPageTabbar extends CTabItem{
	
	private static final Log LOG=LogFactory.getLog(SearchPageTabbar.class);
	
	private Composite composite;

	public SearchPageTabbar(CTabFolder parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		composite=new Composite(parent, style);
		
	}

}
