package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.wozipa.reptile.app.ApplicationWindows;
import com.wozipa.reptile.app.tab.ConfigTabbar;

public class ConfigAction extends Action{
	
	private ConfigTabbar tabbar=null;
	
	
	
	public ConfigTabbar getTabbar() {
		return tabbar;
	}

	public void setTabbar(ConfigTabbar tabbar) {
		this.tabbar = tabbar;
	}

	public ConfigAction()
	{
		super();
		setText("设置(&C)");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'C');
	    setToolTipText("设置");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ApplicationWindows windows=ApplicationWindows.GetApp();
		if(tabbar==null)
		{
			tabbar=new ConfigTabbar(windows.getContent(),SWT.NONE);
			tabbar.createContent();
		}
		windows.getContent().setSelection(tabbar);
	}
	
	

}
