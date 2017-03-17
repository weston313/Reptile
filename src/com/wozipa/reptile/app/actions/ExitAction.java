package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class ExitAction extends Action{
	
	public ExitAction()
	{
		super();
		setText("退出(&E)");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'E');
	    setToolTipText("退出");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}

}
