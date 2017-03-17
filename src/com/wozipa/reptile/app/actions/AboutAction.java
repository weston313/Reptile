package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class AboutAction extends Action{
	
	public AboutAction()
	{
		super();
		setText("关于(&A)");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'A');
	    setToolTipText("关于");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
