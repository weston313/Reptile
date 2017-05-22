package com.wozipa.reptile.app.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;

import com.wozipa.reptile.app.ApplicationWindows;
import com.wozipa.reptile.app.tab.JobListTabbar;

public class JobListAction extends Action{
	
	private JobListTabbar tabbar=null;
	
	public void close()
	{
		tabbar=null;
	}
	
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
		CTabFolder parent=windows.getContent();
		if(tabbar==null)
		{
			
			tabbar=new JobListTabbar(parent, SWT.NONE);
			tabbar.createContent();
		}
		parent.setSelection(tabbar);
	}

}
