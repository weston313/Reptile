package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

public class SaveAction extends Action{
	
	public SaveAction()
	{
		super();
		setText("保存(&S)");
		setToolTipText("保存文件");
		setAccelerator(SWT.ALT + SWT.SHIFT + 'S');
//		setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("另存为按钮点击事件");
	}

}
