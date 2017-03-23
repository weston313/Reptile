package com.wozipa.reptile.app.tab;

import javax.swing.text.TabableView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.wozipa.reptile.app.ApplicationWindows;
import com.wozipa.reptile.app.task.TaskContainer;
import com.wozipa.reptile.app.task.TaskInfo;

public class JobInfoTabbar extends CTabItem{
	
	private static final Log LOG=LogFactory.getLog(JobInfoTabbar.class);
	
	private Composite composite=null;
	private CTabFolder parent;
	private int style;
	
	private TaskContainer container=null;
	private String id;
	private TaskInfo taskInfo;
	private JobInfoListern listern;
	
	private Text idText;
	private Text timeText;
	private Text stateText;
	private Text progressText;
	private Text resultText;
	private Text logText;

	public JobInfoTabbar(CTabFolder parent, int style) {
		super(parent, style);
		this.parent=parent;
		this.style=style;
		this.setText("任务详情");
		this.setShowClose(true);
		//
		composite=new Composite(parent, style);
		this.setControl(composite);
		composite.setBackground(new Color(null,new RGB(255, 255, 255)));
		GridLayout layout=new GridLayout();
		composite.setLayout(new GridLayout(5,false));
		//
		this.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				// TODO Auto-generated method stub
				if(listern!=null)
				{
					listern.shutdown();
				}	
			}
		});
	}
	
	public void setTaskId(String id)
	{
		this.id=id;
		this.container=TaskContainer.getInstance();
		
	}
	
	public void createContent()
	{
		if(this.id==null || this.id.isEmpty())
		{
			return;
		}
		this.taskInfo=container.getTaskInfor(this.id);
		if(taskInfo==null)
		{
			MessageBox box=new MessageBox(ApplicationWindows.GetApp().getShell(),SWT.ERROR);
			box.setMessage("无法获取该任务的相关信息");
			box.open();
			dispose();
		}
		//
		createName();
		createTime();
		createResultPath();
		createState();
		createProgress();
		createLog();
		//add information to job
		loadData();
		//
		listern=new JobInfoListern(this);
		listern.start();
	}
	
	public void loadData()
	{
		idText.setText(id);
		if(taskInfo!=null)
		{
			timeText.setText(taskInfo.getTime());
			resultText.setText(taskInfo.getResultPath());
			stateText.setText(taskInfo.getState().getValue());
			progressText.setText(Double.toString(taskInfo.getPregress()));
		}
	}
	
	public void createName()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("任务ID值");
		label.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER, false, false, 1, 1));
		//
		idText=new Text(composite, SWT.BORDER);
		idText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true, false, 4, 1));
	}
	
	public void createTime()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("创建时间");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		timeText=new Text(composite, SWT.BORDER);
		timeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true, false, 4, 1));
	}
	
	public void createResultPath()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("结果路径");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		resultText=new Text(composite, SWT.BORDER);
		resultText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true, false, 4, 1));
	}
	
	public void createState()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("任务状态");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		stateText=new Text(composite, SWT.BORDER);
		stateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true, false, 4, 1));
	}
	
	public void createProgress()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("进度条");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		progressText=new Text(composite, SWT.BORDER);
		progressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true, false, 4, 1));
	}
	
	public void createLog()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("任务信息");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		Text logText=new Text(composite, SWT.BORDER);
		logText.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true, true, 4, 3));
		logText.setText("日志系统还正在开发中");
	}
	
	public class JobInfoListern extends Thread
	{
		private boolean doRun=true;
		private JobInfoTabbar tabbar=null;
		
		public JobInfoListern(JobInfoTabbar jobInfoTabbar) {
			// TODO Auto-generated constructor stub
			tabbar=jobInfoTabbar;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(doRun)
			{
				tabbar.getDisplay().syncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						TaskInfo taskInfo=container.getTaskInfor(id);
						if(taskInfo!=null)
						{
							progressText.setText(taskInfo.getPregress()+"");
							stateText.setText(taskInfo.getState().getValue());
						}
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			LOG.info("the job information listern is closing");
		}
		
		public void shutdown()
		{
			
			this.doRun=true;
		}
	}
}
