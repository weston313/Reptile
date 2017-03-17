package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

public class SaveAsAction extends Action{

	public SaveAsAction()
	{
		super();
		this.setText("另存为(&A)");
		this.setAccelerator(SWT.ALT+SWT.SHIFT+'A');
		this.setToolTipText("另存为");
//		this.setImageDescriptor(null);
	}
}
