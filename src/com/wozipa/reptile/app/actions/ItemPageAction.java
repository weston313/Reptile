package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.wozipa.reptile.app.ApplicationWindows;
import com.wozipa.reptile.app.tab.ItemPageTabbar;

public class ItemPageAction extends Action{
	
	public ItemPageAction()
	{
		super();
		setText("商品爬取(&N)");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'N');
	    setToolTipText("新建商品页");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}
	
	public void run() {
		ApplicationWindows windows=ApplicationWindows.GetApp();
		ItemPageTabbar pageTabbar=new ItemPageTabbar(windows.getContent(), SWT.NONE);
		pageTabbar.createContent();
		
	}

}
