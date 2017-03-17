package com.wozipa.reptile.app.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.junit.experimental.theories.Theories;

public class ConfigTabbar extends CTabItem{
	
	private CTabFolder parent;
	private int style;
	private Composite composite=null;

	public ConfigTabbar(CTabFolder parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		this.parent=parent;
		this.style=style;
		this.composite=new Composite(parent, style);
		//
		this.setShowClose(true);
		this.setText("全局设置");
		this.setControl(composite);
		this.addListener(SWT.CLOSE, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				System.out.println("close");
			}
		});
		//
		composite.setBackground(new Color(null, new RGB(255, 255, 255)));
		this.composite.setLayout(new GridLayout(3, false));
		
	}
	
	public void createContent()
	{
		createResultArea();
		createCookieArea();
	}
	
	public void createResultArea()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("结果路径");
		label.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER, false, false, 1, 1));
		//
		Text resultText=new Text(composite,SWT.BORDER);
		resultText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER, true, false, 1, 1));
		//
		Button button=new Button(composite, SWT.BUTTON1);
		button.setText("更换目录");
		button.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,false,false,1,1));
	}
	
	private void createCookieArea() {
		//
		Text usernameLabel=new Text(composite,SWT.NONE);
		usernameLabel.setText("淘宝账号");
		usernameLabel.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER, false, false, 1, 1));
		//
		Text usernameText=new Text(composite,SWT.BORDER);
		usernameText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER, true, false, 2, 1));
		//
		Text passwdLabel=new Text(composite,SWT.NONE);
		passwdLabel.setText("淘宝密码");
		passwdLabel.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER, false, false, 1, 1));
		//
		Text passwdText=new Text(composite,SWT.PASSWORD | SWT.BORDER);
		passwdText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true, false, 2, 1));
	}
	
	
}