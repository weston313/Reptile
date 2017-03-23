package com.wozipa.reptile.app.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

public class AboutAction extends Action{
	
	private static final Log LOG=LogFactory.getLog(AboutAction.class);
	
	private static final String AUTHOR="WOZIPA";
	private static final String TIME="2017-3-22 0:00";
	private static final String VERSION="0.1.0";
	
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
		MessageBox message=new MessageBox(Display.getCurrent().getActiveShell(),SWT.ICON_INFORMATION);
		message.setMessage("针对阿里巴巴旗下电子商务网爬虫\n"
				+ "Reptile For Product of Alibaba Company\n"
				+"软件版本\t"+VERSION+"\n"
				+"版本时间\t"+TIME+"\n");
		message.open();
	}
}
