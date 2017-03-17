package com.wozipa.reptile.app.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.wozipa.reptile.app.task.TaskContainer;
import com.wozipa.reptile.app.task.TaskInfo;

public class JobInfoTabbar extends CTabItem{
	
	private Composite composite=null;
	private CTabFolder parent;
	private int style;
	
	private TaskContainer container=null;
	private String id;
	private TaskInfo taskInfo;
	
	private Text idText;

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
		
	}
	
	public void setTaskId(String id)
	{
		this.id=id;
		this.container=TaskContainer.getInstance();
		this.taskInfo=container.getTaskInfor(this.id);
	}
	
	public void createContent()
	{
		if(this.id==null || this.id.isEmpty())
		{
			return;
		}
		createName();
		createTime();
		createResultPath();
		createState();
		createProgress();
		createLog();
	}
	
	public void createName()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("任务ID值");
		label.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER, false, false, 1, 1));
		//
		idText=new Text(composite, SWT.BORDER);
		idText.setText(taskInfo.getId());
		idText.setLayoutData(new GridData(SWT.FILL,SWT.CENTER,true, false, 4, 1));
	}
	
	public void createTime()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("创建时间");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		Text timeText=new Text(composite, SWT.BORDER);
		timeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true, false, 4, 1));
		timeText.setText(taskInfo.getTime());
	}
	
	public void createResultPath()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("结果路径");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		Text resultText=new Text(composite, SWT.BORDER);
		resultText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true, false, 4, 1));
		resultText.setText(taskInfo.getResultPath());
	}
	
	public void createState()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("任务状态");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		Text stateText=new Text(composite, SWT.BORDER);
		stateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true, false, 4, 1));
		stateText.setText(taskInfo.getState().getValue());
	}
	
	public void createProgress()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("进度条");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		//
		Text progressText=new Text(composite, SWT.BORDER);
		progressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,true, false, 4, 1));
		progressText.setText(taskInfo.getResultPath());
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
}
