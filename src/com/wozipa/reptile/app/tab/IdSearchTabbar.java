package com.wozipa.reptile.app.tab;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import com.wozipa.reptile.data.ConnManager;
import com.wozipa.reptile.data.file.FileConnection;
import com.wozipa.reptile.data.file.IdFileData;

public class IdSearchTabbar extends CTabItem{
	
	private static final Log LOG=LogFactory.getLog(IdSearchTabbar.class);
	
	private CTabFolder parent;
	private int style;
	private Composite composite;
	
	private Text input;
	private Text output;

	public IdSearchTabbar(CTabFolder parent, int style) {
		super(parent, style);
		this.parent=parent;
		this.style=style;
		this.setText("ID功能");
		this.setShowClose(true);
		this.composite=new Composite(parent,style);
		this.setControl(composite);
		//
		composite.setLayout(new GridLayout(3, false));
		composite.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
	}
	
	public void createContent()
	{
		createLabel(composite, 3, "ID解析",true);
		createInputArea();
		createResultArea();
//		createLabel(composite, 3,"ID数据操作",true);
		
	}
	
	private void createLabel(Composite composite,int width,String text,boolean span) {
		// TODO Auto-generated method stub
		Label label=new Label(composite, SWT.NONE);
		label.setText(text);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, span, false, width, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
	}
	
	public void createInputArea()
	{
		Label label=new Label(composite, SWT.NONE);
		label.setText("输入ID");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		input=new Text(composite,SWT.BORDER);
		input.setLayoutData(new GridData(SWT.FILL,SWT.CENTER, true, false, 1, 1));
		//
		Button button=new Button(composite, SWT.BUTTON1);
		button.setText("进行获取");
		button.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,false,false,1,1));
		
		button.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				String id=input.getText();
				if(id==null || id.isEmpty())
				{
					MessageBox box=new MessageBox(parent.getShell());
					box.setMessage("请输入商品的ID值");
					box.open();
				}
				FileConnection connectin=(FileConnection) ConnManager.getInstance().getConnection(IdFileData.class);
				if(connectin.existedId(id))
				{
					output.setText(connectin.getGoodId(id));
				}
			}
		});
	}
	
	public void createResultArea()
	{
		Label label=new Label(composite, SWT.NONE);
		label.setText("商品ID");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		output=new Text(composite,SWT.BORDER);
		output.setLayoutData(new GridData(SWT.FILL,SWT.CENTER, true, false, 2, 1));
	}
	
	public void createCopyArea()
	{
		Label label=new Label(composite, SWT.NONE);
		label.setText("输入ID");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		label.setBackground(new Color(null, new RGB(255, 255, 255)));
		//
		input=new Text(composite,SWT.BORDER);
		input.setLayoutData(new GridData(SWT.FILL,SWT.CENTER, true, false, 1, 1));
		//
		Button button=new Button(composite, SWT.BUTTON1);
		button.setText("进行获取");
		button.setLayoutData(new GridData(SWT.LEFT,SWT.CENTER,false,false,1,1));
		
		button.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				String id=input.getText();
				if(id==null || id.isEmpty())
				{
					MessageBox box=new MessageBox(parent.getShell());
					box.setMessage("请输入商品的ID值");
					box.open();
				}
				FileConnection connectin=(FileConnection) ConnManager.getInstance().getConnection(IdFileData.class);
				if(connectin.existedId(id))
				{
					output.setText(connectin.getGoodId(id));
				}
			}
		});
	}
}
