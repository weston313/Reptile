package com.wozipa.reptile.app.tab;

import java.awt.event.MouseAdapter;

import org.apache.bcel.generic.IINC;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.wozipa.reptile.app.task.TaskContainer;

public class JobListTabbar extends CTabItem{
	
	private static final Log LOG=LogFactory.getLog(CTabItem.class);
	
	private TaskContainer container=null;
	
	private CTabFolder parent;
	private int style;
	private Composite composite;
	
	private Table jobTable;

	public JobListTabbar(CTabFolder parent, int style) {
		super(parent, style);
		this.parent=parent;
		this.style=style;
		this.setText("任务列表信息");
		this.setShowClose(true);
		//
		composite=new Composite(parent,style);
		this.setControl(composite);
		composite.setBackground(new Color(null,new RGB(255, 255, 255)));
		GridLayout layout=new GridLayout();
		layout.numColumns=1;
		layout.makeColumnsEqualWidth=true;
		composite.setLayout(layout);
		//
		container=TaskContainer.getInstance();
	}
	
	/**
	 * @author wozipa
	 * @date 2017-2-26
	 * @see create the content of tabbar
	 */
	public void createContent()
	{
		createListArea();
		loadJobInfos();
	}
	
	/**
	 * @author wozipa
	 * @date 2017-2-25
	 * @see 创建任务列表
	 */
	public void createListArea()
	{
		
		GridData tableGD=createGD(GridData.FILL,GridData.FILL);
		jobTable=new Table(this.composite,SWT.MULTI);
		jobTable.setHeaderVisible(true);
		jobTable.setLinesVisible(true);
		jobTable.setLayoutData(tableGD);
		//create the list of the column
		TableColumn idColumn=new TableColumn(jobTable,SWT.NONE);
		idColumn.setText("任务ID");
		idColumn.pack();
		//
		TableColumn progressColumn=new TableColumn(jobTable,SWT.NONE);
		progressColumn.setText("进度条");
		progressColumn.pack();
		//
		TableColumn resultPathColumn=new TableColumn(jobTable, SWT.NONE);
		resultPathColumn.setText("结果路径");
		resultPathColumn.pack();
		//
		TableColumn timeColumn=new TableColumn(jobTable, SWT.NONE);
		timeColumn.setText("开始时间");
		timeColumn.pack();
		//
		TableColumn stateColumn=new TableColumn(jobTable, SWT.NONE);
		stateColumn.setText("任务状态");
		stateColumn.pack();
		//
		TableColumn stopColumn=new TableColumn(jobTable, SWT.NONE);
		stopColumn.setText("停止任务");
		stopColumn.pack();
		//
		jobTable.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent arg0) {
				// TODO Auto-generated method stub
				 TableColumn[] columns = jobTable.getColumns();
                 int clientWidth = jobTable.getBounds().width;
                for(int i=0;i<columns.length;i++){
                    columns[i].setWidth((clientWidth)/columns.length);
                }
                //
			}
		});
		//
		jobTable.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				int index=jobTable.getSelectionIndex();
				System.out.println(index);
				TableItem item=jobTable.getItem(index);
				String id=item.getText(0);
				JobInfoTabbar tabbar=new JobInfoTabbar(parent,style);
				tabbar.setTaskId(id);
				tabbar.createContent();
			}
		});
	}
	
	/**
	 * @author wozipa
	 * @date 2017-2-26
	 * @see 创建griddata数据
	 * @param w
	 * @param h
	 * @return
	 */
	public GridData createGD(int w,int h)
	{
		GridData gridData=new GridData();
		gridData.horizontalAlignment=w;
		gridData.verticalAlignment=h;
		gridData.grabExcessHorizontalSpace=true;
		gridData.grabExcessVerticalSpace=true;
		return gridData;
	}
	
	public void loadJobInfos()
	{
		jobTable.clearAll();
		java.util.List<String[]> infors=container.getTasks();
		for(int i=0;i<infors.size();i++)
		{
			TableItem item=new TableItem(jobTable,SWT.NONE,i);
			item.setText(infors.get(i));
		}
		
	}

}
