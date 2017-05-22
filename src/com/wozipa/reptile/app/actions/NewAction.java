package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;

import com.wozipa.reptile.app.ApplicationWindows;
import com.wozipa.reptile.app.tab.ItemPageTabbar;

public class NewAction extends Action{
	
	public NewAction()
	{
		super();
		setText("新建(&N)");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'N');
	    setToolTipText("新建");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}
	
	public void run() {
		ApplicationWindows windows=ApplicationWindows.GetApp();
		CTabFolder parent=windows.getContent();
		ItemPageTabbar pageTabbar=new ItemPageTabbar(parent, SWT.NONE);
		pageTabbar.createContent();
		parent.setSelection(pageTabbar);
	}
	
}
