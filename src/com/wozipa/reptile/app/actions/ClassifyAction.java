package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.wozipa.reptile.app.ApplicationWindows;
import com.wozipa.reptile.app.tab.ClassifyPageTabbar;
import com.wozipa.reptile.app.tab.ItemPageTabbar;

public class ClassifyAction extends Action{
	
	public ClassifyAction()
	{
		super();
		setText("分类爬取(&N)");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'N');
	    setToolTipText("新建商品分类");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}
	
	public void run() {
		ApplicationWindows windows=ApplicationWindows.GetApp();
		ClassifyPageTabbar pageTabbar=new ClassifyPageTabbar(windows.getContent(), SWT.NONE);
		pageTabbar.createContent();
		
	}

}
