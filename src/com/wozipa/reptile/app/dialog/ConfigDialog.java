package com.wozipa.reptile.app.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.wozipa.reptile.app.config.AppConfiguration;
import com.wozipa.reptile.app.config.Key;

public class ConfigDialog extends Dialog{
	
	private Text resultText;
	private Text userNameText;
	private Text passwdText;
	private Shell shell;
	private Composite composite;

	public ConfigDialog(Shell parentShell) {
		super(parentShell);
		this.shell=parentShell;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		this.composite=parent;
		parent.setLayout(new GridLayout(4, false));
		parent.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		createResultArea(parent);
		//
		creeateUserNameArea(parent);
		//
		createPasswordArea(parent);
		//
		return parent;
	}
	
	private static final String DO="确认";
	private static final int DO_ID=0;
	private static final String CACEL="取消";
	private static final int CACEL_ID=1;
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// TODO Auto-generated method stub
		parent.setBackground(new Color(null, new RGB(255, 255, 255)));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		Button doButton=createButton(parent,DO_ID, DO, false);
		Button cacelBtn=createButton(parent, CACEL_ID, CACEL, false);
	}
	
	@Override
	protected void buttonPressed(int buttonId) {
		// TODO Auto-generated method stub
		if(buttonId==DO_ID)
		{
			AppConfiguration configuration=AppConfiguration.getConfiguration();
			String resultPath=resultText.getText();
			String username=userNameText.getText();
			String password=passwdText.getText();
			if(resultPath==null || resultPath.isEmpty() || username==null || username.isEmpty() || password==null || password.isEmpty())
			{
				MessageBox dialog=new MessageBox(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM);
				dialog.setMessage("参数为填写完整");
				dialog.open();
				return;
			}
			configuration.addKey(AppConfiguration.RESULT_PATH,new Key(resultPath,""));
			configuration.addKey(AppConfiguration.COOKIE_USERNAME, new Key(username,""));
			configuration.addKey(AppConfiguration.COOKIE_PASSWORD, new Key(password, ""));
			close();
		}
		else {
			if(this.shell==null)
			{
				System.out.println("hehehe");
			}
			MessageBox dialog=new MessageBox(Display.getCurrent().getActiveShell(), SWT.DIALOG_TRIM);
			dialog.setMessage("没有设置输出结果、淘宝会员账号以及相应密码，可能会影响软件在数据抓取过程中的功能，如果想设置请点击设置按钮");
			dialog.open();
			close();
		}
	}
	
	public void createResultArea(Composite parent)
	{
		Label label=new Label(parent, SWT.NONE);
		label.setText("输出路径");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		resultText=new Text(parent,SWT.BORDER);
		resultText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		resultText.setText(AppConfiguration.getConfiguration().getKey(AppConfiguration.RESULT_PATH).getVlaue());
		//
		Button button=new Button(parent, SWT.NONE);
		button.setText("选择路径");
		button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				DirectoryDialog dialog=new DirectoryDialog(Display.getCurrent().getActiveShell());
				dialog.open();
				String path=dialog.getFilterPath();
				if(path==null || path.isEmpty())
				{
					return;
				}
				resultText.setText(path);
			}
		});
	}
	
	public void creeateUserNameArea(Composite parent)
	{
		Label label=new Label(parent, SWT.NONE);
		label.setText("淘宝会员账户（非手机号）");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		userNameText=new Text(parent,SWT.BORDER);
		userNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		userNameText.setText(AppConfiguration.getConfiguration().getKey(AppConfiguration.COOKIE_USERNAME).getVlaue());
	}
	
	public void createPasswordArea(Composite parent)
	{
		Label label=new Label(parent, SWT.NONE);
		label.setText("淘宝会员密码");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		passwdText=new Text(parent,SWT.BORDER | SWT.PASSWORD);
		passwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		passwdText.setText(AppConfiguration.getConfiguration().getKey(AppConfiguration.COOKIE_PASSWORD).getVlaue());
	}

}
