package com.wozipa.reptile.app;

import java.awt.dnd.DnDConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabFolderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.dialogs.ListDialog;

import com.wozipa.reptile.app.actions.AboutAction;
import com.wozipa.reptile.app.actions.ClassifyAction;
import com.wozipa.reptile.app.actions.ConfigAction;
import com.wozipa.reptile.app.actions.ExitAction;
import com.wozipa.reptile.app.actions.HelpAction;
import com.wozipa.reptile.app.actions.IdSeachAction;
import com.wozipa.reptile.app.actions.ItemPageAction;
import com.wozipa.reptile.app.actions.JobListAction;
import com.wozipa.reptile.app.actions.NewAction;
import com.wozipa.reptile.app.actions.SaveAction;
import com.wozipa.reptile.app.actions.SaveAsAction;
import com.wozipa.reptile.app.config.AppConfiguration;
import com.wozipa.reptile.app.config.AppSizeConfig;
import com.wozipa.reptile.app.config.Key;
import com.wozipa.reptile.app.dialog.ConfigDialog;
import com.wozipa.reptile.app.tab.ConfigTabbar;
import com.wozipa.reptile.app.tab.IdSearchTabbar;
import com.wozipa.reptile.app.tab.ItemPageTabbar;
import com.wozipa.reptile.app.tab.JobListTabbar;
import com.wozipa.reptile.cookie.CookieManagerCache;
import com.wozipa.reptile.data.ConnManager;
import com.wozipa.reptile.data.db.DBConnection;
import com.wozipa.reptile.data.file.IdFileData;

public class ApplicationWindows extends ApplicationWindow{
	
	private static final Log LOG=LogFactory.getLog(ApplicationWindows.class);
	
	private static ApplicationWindows app;
	private NewAction newAction;
	private SaveAction saveAction;
	private SaveAsAction saveAsAction;
	private ExitAction exitAction;
	private JobListAction jobListAction;
	private ConfigAction configAction;
	private HelpAction helpAction;
	private AboutAction aboutAction;
	private ItemPageAction itemPageAction;
	private ClassifyAction classifyAction;
	private IdSeachAction idSeachAction;
	
	private CTabFolder content;
	//
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
		itemPageAction=new ItemPageAction();
		classifyAction=new ClassifyAction();
		idSeachAction=new IdSeachAction();
		// TODO Auto-generated constructor stub
		this.addMenuBar();
		this.addToolBar(SWT.FLAT);
		this.addStatusLine();
		//init the configuration
		AppConfiguration configuration=AppConfiguration.getConfiguration();
		Key resultPathKey=configuration.getKey(AppConfiguration.RESULT_PATH);
		String resultPath=resultPathKey.getVlaue();
		String username=configuration.getKey(AppConfiguration.COOKIE_USERNAME).getVlaue();
		String password=configuration.getKey(AppConfiguration.COOKIE_PASSWORD).getVlaue();
		if(resultPath==null || resultPath.isEmpty() || username==null || username.isEmpty() || password==null || password.isEmpty())
		{
			ConfigDialog dialog=new ConfigDialog(this.getShell());
			dialog.open();
		}
		//
		DBConnection.GetDataBase();
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
		fileMenu.add(itemPageAction);
		fileMenu.add(classifyAction);
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.add(exitAction);
		//添加任务功能
		jobMenu.add(jobListAction);
		jobMenu.add(idSeachAction);
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
		
		toolBar.add(itemPageAction);
		toolBar.add(classifyAction);
		toolBar.add(jobListAction);
		toolBar.add(idSeachAction);
		toolBar.add(configAction);
		return toolBar;
	}
		
	@Override
	protected Control createContents(Composite parent) {
		// TODO Auto-generated method stub
		//init 
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
				System.out.println("restore");
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
					configAction.setTabbar(null); 
				}
				else if(arg0.item.getClass().equals(JobListTabbar.class))
				{
					LOG.info("start to close the job list");
					jobListAction.close();
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
	
	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		LOG.info("close");
		AppConfiguration.getConfiguration().close();
		ConnManager.getInstance().close();
		return super.close();
	}

}
