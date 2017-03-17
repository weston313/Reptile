package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;

import com.wozipa.reptile.app.ApplicationWindows;
import com.wozipa.reptile.app.tab.JobListTabbar;

public class JobListAction extends Action{
	
	public JobListAction()
	{
		super();
		setText("任务列表(&J)");
	    this.setAccelerator( SWT.ALT + SWT.SHIFT + 'J');
	    setToolTipText("任务列表");
	    //setImageDescriptor(ImageDescriptor.createFromFile(NewAction.class,"icons\\new.gif"));
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ApplicationWindows windows=ApplicationWindows.GetApp();
		JobListTabbar tabbar=new JobListTabbar(windows.getContent(), SWT.NONE);
		tabbar.createContent();
	}

}
