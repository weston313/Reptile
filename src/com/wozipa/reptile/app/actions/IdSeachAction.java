package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;

import com.wozipa.reptile.app.ApplicationWindows;
import com.wozipa.reptile.app.tab.IdSearchTabbar;

public class IdSeachAction extends Action{
	
	public IdSeachAction()
	{
		super();
		setText("ID功能");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'A');
	    setToolTipText("搜索相关ID值");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ApplicationWindows windows=ApplicationWindows.GetApp();
		CTabFolder parent=windows.getContent();
		IdSearchTabbar tabbar=new IdSearchTabbar(parent,SWT.NONE);
		tabbar.createContent();
		parent.setSelection(tabbar);
	}

}
