package com.wozipa.reptile.app.tab;

import org.apache.commons.collections.functors.IfClosure;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.wozipa.reptile.app.config.AppConfiguration;
import com.wozipa.reptile.app.config.Key;

public class ConfigTabbar extends CTabItem{
	
	private static final Logger LOGGER=Logger.getLogger(ConfigTabbar.class);
	private static final String[] PROXY_COMBO={"关闭","打开"};
	
	private CTabFolder parent;
	private int style;
	private Composite composite=null;
	
	private Text resultText=null;
	private Text usernameText=null;
	private Text passwordText=null;
	private Combo proxyCombo=null;

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
		//
		composite.setBackground(new Color(null, new RGB(255, 255, 255)));
		this.composite.setLayout(new GridLayout(3, false));
		//
	}
	
	public void createContent()
	{
		createResultArea();
		createCookieArea();
		createProxyArea();
		createSaveArea();
	}
	
	public void createResultArea()
	{
		Label label=new Label(composite, SWT.NONE);
		label.setText("结果路径");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		resultText=new Text(composite,SWT.BORDER);
		resultText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER, true, false, 1, 1));
		resultText.setText(AppConfiguration.getConfiguration().getKey(AppConfiguration.RESULT_PATH).getVlaue());
		//
		Button button=new Button(composite, SWT.BUTTON_MASK);
		button.setText("更换目录");
		button.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER,false,false,1,1));
		button.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				DirectoryDialog dialog=new DirectoryDialog(parent.getShell());
				dialog.open();
				String path=dialog.getFilterPath();
				if(path!=null && !path.isEmpty())
				{
					resultText.setText(path);
				}
			}
		});
	}
	
	private void createCookieArea() {
		//
		Label label=new Label(composite, SWT.NONE);
		label.setText("淘宝账号");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		usernameText=new Text(composite,SWT.BORDER);
		usernameText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER, true, false, 2, 1));
		usernameText.setText(AppConfiguration.getConfiguration().getKey(AppConfiguration.COOKIE_USERNAME).getVlaue());
		//
		Label label1=new Label(composite, SWT.NONE);
		label1.setText("淘宝密码");
		label1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label1.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		passwordText=new Text(composite,SWT.PASSWORD | SWT.BORDER);
		passwordText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true, false, 2, 1));
		passwordText.setText(AppConfiguration.getConfiguration().getKey(AppConfiguration.COOKIE_PASSWORD).getVlaue());
	}
	
	public void createSaveArea()
	{
		Button button=new Button(composite, SWT.NONE);
		button.setText("确认修改");
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		button.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				String path=resultText.getText();
				String username=usernameText.getText();
				String password=passwordText.getText();
				
				//
				AppConfiguration configuration=AppConfiguration.getConfiguration();
				configuration.addKey(AppConfiguration.RESULT_PATH,new Key(path, ""));
				configuration.addKey(AppConfiguration.COOKIE_USERNAME,new Key(username, ""));
				configuration.addKey(AppConfiguration.COOKIE_PASSWORD,new Key(password, ""));
				String proxy=proxyCombo.getText();
				if(proxy.equals(PROXY_COMBO[0]))
				{
					configuration.addKey(AppConfiguration.PROXY_STATE,new Key("0",""));
				}
				else
				{
					configuration.addKey(AppConfiguration.PROXY_STATE, new Key("1",""));	
				}
			}
		});
		
		Button clearConfig=new Button(composite,SWT.NONE);
		clearConfig.setText("清空配置");
		clearConfig.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		clearConfig.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				String path=resultText.getText();
				String username=usernameText.getText();
				String password=passwordText.getText();
				AppConfiguration configuration=AppConfiguration.getConfiguration();
				configuration.addKey(AppConfiguration.RESULT_PATH,new Key(path, ""));
				configuration.addKey(AppConfiguration.COOKIE_USERNAME,new Key(username, ""));
				configuration.addKey(AppConfiguration.COOKIE_PASSWORD,new Key(password, ""));
			}
		});
	}
	
	/**
	 * @author wozipa
	 * @date 2017-6-14
	 * @see create the proxy area to let user chose open the proxy or not
	 */
	public void createProxyArea()
	{
		Text text=new Text(composite, style);
		text.setText("IP代理");
		text.setBackground(new Color(null, new RGB(255, 255, 255)));
		text.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER,false,false,1,1));
		//
		proxyCombo=new Combo(composite, SWT.DROP_DOWN |SWT.READ_ONLY);
		proxyCombo.setItems(PROXY_COMBO);
		proxyCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,2,1));
		Key proxyKey=AppConfiguration.getConfiguration().getKey(AppConfiguration.PROXY_STATE);
		proxyCombo.select(Integer.parseInt(proxyKey.getVlaue()));
	}
}
