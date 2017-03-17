package com.wozipa.reptile.app;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabFolderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.wozipa.reptile.app.actions.AboutAction;
import com.wozipa.reptile.app.actions.ConfigAction;
import com.wozipa.reptile.app.actions.ExitAction;
import com.wozipa.reptile.app.actions.HelpAction;
import com.wozipa.reptile.app.actions.JobListAction;
import com.wozipa.reptile.app.actions.NewAction;
import com.wozipa.reptile.app.actions.SaveAction;
import com.wozipa.reptile.app.actions.SaveAsAction;
import com.wozipa.reptile.app.config.AppSizeConfig;
import com.wozipa.reptile.app.tab.ConfigTabbar;

public class ApplicationWindows extends ApplicationWindow{
	
	private static ApplicationWindows app;
	private NewAction newAction;
	private SaveAction saveAction;
	private SaveAsAction saveAsAction;
	private ExitAction exitAction;
	private JobListAction jobListAction;
	private ConfigAction configAction;
	private HelpAction helpAction;
	private AboutAction aboutAction;
	private CTabFolder content;

	public ApplicationWindows() {
		super(null);
		app=this;
		newAction=new NewAction();
		saveAction=new SaveAction();
		saveAsAction=new SaveAsAction();
		exitAction=new ExitAction();
		jobListAction=new JobListAction();
		configAction=new ConfigAction();
		helpAction=new HelpAction();
		aboutAction=new AboutAction();
		// TODO Auto-generated constructor stub
		this.addMenuBar();
		this.addToolBar(SWT.FLAT);
		this.addStatusLine();
	}
	
	/**
	 * @author wozipa
	 * @date 2017-2-25
	 * @see 获取当前的app
	 * @return
	 */
	public static ApplicationWindows GetApp()
	{
		return app;
	}
	
	public CTabFolder getContent()
	{
		return this.content;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		// TODO Auto-generated method stub
		super.configureShell(shell);
		shell.setText("网页爬虫工具");
		shell.setMaximized(true);
		shell.setSize(AppSizeConfig.APP_WIDTH,AppSizeConfig.APP_HEIGHT);
	}
	
	@Override
	protected MenuManager createMenuManager() {
		// TODO Auto-generated method stub
		MenuManager menuBar=new MenuManager();
		//
		MenuManager fileMenu=new MenuManager("新建(&F)");
		MenuManager jobMenu=new MenuManager("任务(&J)");
		MenuManager confMenu=new MenuManager("配置(&C)");
		MenuManager helpMenu=new MenuManager("帮助(&H)");
		//
		menuBar.add(fileMenu);
		menuBar.add(jobMenu);
		menuBar.add(confMenu);
		menuBar.add(helpMenu);
		//添加文件功能
		fileMenu.add(newAction);
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.add(exitAction);
		//添加任务功能
		jobMenu.add(jobListAction);
		//添加配置功能
		confMenu.add(configAction);
		//添加帮助功能
		helpMenu.add(helpAction);
		helpMenu.add(aboutAction);
		return menuBar;
	}
	
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		// TODO Auto-generated method stub
		ToolBarManager toolBar=new ToolBarManager(style);
		
		toolBar.add(newAction);
		toolBar.add(configAction);
		toolBar.add(this.jobListAction);
		return toolBar;
	}
		
	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		parent.setBackground(new Color(null, new RGB(255, 255, 255)));
		content=new CTabFolder(parent,SWT.NONE);
		content.setBackground(new Color(null, new RGB(255, 255, 255)));
		content.addCTabFolder2Listener(new CTabFolder2Listener() {
			
			@Override
			public void showList(CTabFolderEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void restore(CTabFolderEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void minimize(CTabFolderEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void maximize(CTabFolderEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close(CTabFolderEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.item.equals(configAction.getTabbar()))
				{
					
					System.out.println("config close");
					configAction.setTabbar(null);
				}
			}
		});
		return parent;
	}
	
	public static void main(String[] args)
	{
		ApplicationWindows windows=new ApplicationWindows();
		windows.setBlockOnOpen(true);
		windows.open();
		Display.getCurrent().dispose();
	}

}
