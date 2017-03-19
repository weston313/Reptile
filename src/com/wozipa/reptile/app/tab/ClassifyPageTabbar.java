package com.wozipa.reptile.app.tab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import com.wozipa.reptile.app.config.AppConfiguration;
import com.wozipa.reptile.app.tab.ItemPageTabbar.StartTaskListern;
import com.wozipa.reptile.app.task.ClassifyTaskThread;
import com.wozipa.reptile.app.task.ItemTaskThread;
import com.wozipa.reptile.item.PageFactory;

public class ClassifyPageTabbar extends CTabItem{

private static final Log LOG=LogFactory.getLog(ItemPageTabbar.class);
	
	private static final String SEPARETOR=";";
	private static final String SEPARETOR_REGEX="[,;\r\n]{1,}";
	
	private static final String[] ID_ENCODE={"无加密","倒转加密","前缀加密","随机加密","MD5加密","SHA加密"};
	
	private CTabFolder parent;
	private Composite composite;
	private int style;
	
	private Text jobIdText=null;
	private Text pagesText=null;
	private Button tmallBtn=null;
	private Button taobaoBtn=null;
	private Button alibbBtn=null;
	private Text resultPathText=null;
	private List encodeList=null;
	
	private AppConfiguration configuration=null;
	
	private String id;

	public ClassifyPageTabbar(CTabFolder parent, int style) {
		super(parent, style);
		
		this.parent=parent;
		this.style=style;
		// TODO Auto-generated constructor stub
		this.setText("分类爬取");
		this.setShowClose(true);
		composite=new Composite(parent,style);
		this.setControl(composite);
		composite.setBackground(new Color(null, new RGB(255, 255, 255)));
		composite.setLayout(new GridLayout(10, false));
		//开始初始化任务信息
		this.id=UUID.randomUUID().toString();
		configuration=AppConfiguration.getConfiguration();
		
	}
	
	/**
	 * @author wozipa
	 * @date 2017-2-25
	 * @see 常见网页爬虫内容
	 */
	public void createContent()
	{
		//
		createIdArea();
		createInputArea();
		createTypeArea();
		createEncodeArea();
		createOutputArea();
		createSubmitArea();

	}
	
	public void createIdArea()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("任务ID值");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		jobIdText=new Text(composite, SWT.BORDER|SWT.MULTI|SWT.SCROLL_LINE);
		jobIdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 9, 1));
		jobIdText.setText(this.id);
	}
	
	/**
	 * @author wozipa
	 * @date 2017-2-25
	 * @see 创建输入区域
	 */
	public void createInputArea()
	{
		Text label=new Text(composite,SWT.NONE);
		label.setText("输入分类网址");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 2));
		
		pagesText=new Text(composite, SWT.BORDER|SWT.MULTI|SWT.SCROLL_LINE);
		pagesText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 8, 2));
		//
		Button fileBtn=new Button(composite, SWT.NONE);
		fileBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,false,1,2));
		fileBtn.setText("加载文件");
		fileBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				FileDialog fileDialog=new FileDialog(composite.getShell(),SWT.OPEN);
				String path=fileDialog.getFilterPath();
				StringBuffer sb=new StringBuffer();
				String name=fileDialog.open();
				if(name==null || name.isEmpty())
				{
					return;
				}
				try {
					BufferedReader reader=new BufferedReader(new FileReader(new File(path+"/"+name)));
					while(reader.ready())
					{
						String line=reader.readLine();
						sb.append(line).append(SEPARETOR);
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
	}
	
	public void createTypeArea()
	{
		Label label=new Label(this.composite,SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 2));
		label.setText("商品分类类型");
		label.setBackground(new Color(null,new RGB(255, 255, 255)));
		
		Group group=new Group(this.composite, SWT.NONE);
		group.setBackground(new Color(null, new RGB(255, 255, 255)));
		group.setLayout(new RowLayout());
		taobaoBtn=new Button(group,SWT.RADIO | SWT.LEFT);
		taobaoBtn.setData("value",PageFactory.TAOBAO);
		taobaoBtn.setText(" 淘宝网站 ");
		taobaoBtn.setSelection(true);
		tmallBtn=new Button(group,SWT.RADIO | SWT.CENTER);
		tmallBtn.setData("value",PageFactory.TMALL);
		tmallBtn.setText(" 天猫网站 ");
		alibbBtn=new Button(group,SWT.RADIO | SWT.RIGHT);
		alibbBtn.setText("阿里巴巴网站 ");
		alibbBtn.setData("value",PageFactory.ALIBABA);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 9, 2));
	}
	
	public void createEncodeArea()
	{
		Label label=new Label(this.composite,SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 2));
		label.setText("商品ID编码");
		
		encodeList=new List(this.composite,SWT.BORDER);
		encodeList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 9, 2));
		for(int i=0;i<ID_ENCODE.length;i++)
		{
			encodeList.add(ID_ENCODE[i]);
		}
		encodeList.select(0);
	}
	
	public void createOutputArea()
	{
		Label label=new Label(composite,SWT.NONE);
		label.setText("任务路径");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 2));
		
		resultPathText=new Text(composite, SWT.BORDER|SWT.SCROLL_LINE);
		resultPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 8, 2));
		resultPathText.setText(configuration.getKey(AppConfiguration.RESULT_PATH).getVlaue());
		
		Button fileBtn=new Button(composite, SWT.NONE);
		fileBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 2));
		fileBtn.setText("选择路径");
		fileBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				DirectoryDialog dialog=new DirectoryDialog(parent.getShell());
				dialog.open();
				String path=dialog.getFilterPath();
				if(path!=null && !path.isEmpty())
				{
					resultPathText.setText(path);
				}
			}
		});
	}
	
	/**
	 * @author wozipa
	 * @date 2017-2-25
	 * @see 创建提交区域
	 */
	public void createSubmitArea()
	{
		Label label=new Label(this.composite,SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 2));
		
		Button button=new Button(this.composite, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 9, 2));
		button.setText("开始进行数据爬取");
		button.addSelectionListener(new StartTaskListern());
	}
	
	
	/**
	 * @author wozipa
	 * @date 2017-3-6
	 * @see 开始任务监听器
	 */
	public class StartTaskListern implements SelectionListener
	{

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			// TODO Auto-generated method stub
			//get the text of the input text area
			String id=jobIdText.getText();
			String pages=pagesText.getText();
			if(pages==null || pages.isEmpty())
			{
				MessageBox messageBox=new MessageBox(Display.getCurrent().getActiveShell());
				messageBox.setMessage("请输入网址");
				messageBox.open();
				return;
			}
			Set<String> set=new HashSet<>();
			String[] pagesUrl=pages.split(SEPARETOR);
			for(String pageUrl:pagesUrl)
			{
				set.add(pageUrl);
			}
			//
			String resultPath=resultPathText.getText();
			//
			String type=getPageType();
			//get the encrypt id
			int encrypt=encodeList.getSelectionIndex();
			//
			pagesUrl=set.toArray(new String[set.size()]);
			ClassifyTaskThread taskThread=new ClassifyTaskThread(id, pagesUrl,resultPath,type,encrypt);
			taskThread.start();
		}
		
		public String getPageType()
		{
			Button tmp=null;
			if(taobaoBtn.getSelection())
			{
				tmp=taobaoBtn;
			}
			else if(tmallBtn.getSelection())
			{
				tmp=tmallBtn;
			}
			else if(alibbBtn.getSelection())
			{
				tmp=alibbBtn;
			}
			return (String) tmp.getData("value");
		}
	}

}
