package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class HelpAction extends Action{

	
	public HelpAction()
	{
		super();
		setText("帮助(&H)");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'H');
	    setToolTipText("帮助");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}
}
