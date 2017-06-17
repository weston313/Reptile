package com.wozipa.reptile.app.task;

import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.wozipa.reptile.item.Page;
import com.wozipa.reptile.item.PageFactory;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.WritableFont.FontName;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONObject;

public class TaskThread extends Thread{
	
	private static final Log LOG=LogFactory.getLog(TaskThread.class);
	
	public enum PAGETYPE{
		TMALL,TAOBAO,ALIBABA
	}
	
	private TaskContainer container=null;
	private TaskInfo taskInfo=null;
	
	private String id;
	private String[] pagesUrl;
	private String resultPath;
	private String type;
	private int encrypt;
	
	public TaskThread(String id,String[] pagesUrl,String resultPath,String type,int encrypt) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.type=type;
		this.pagesUrl=pagesUrl;
		this.resultPath=resultPath;
		this.encrypt=encrypt;
		this.container=TaskContainer.getInstance();
	}
	
	public WritableSheet initlizeHeader(WritableWorkbook workbook)
	{
		LOG.info("start to initlize the excel file");
		//
		WritableFont wf = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,
                UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
        WritableCellFormat wcf = new WritableCellFormat(wf);
        try {
			wcf.setVerticalAlignment(VerticalAlignment.JUSTIFY);
			wcf.setAlignment(Alignment.LEFT);
	        //sheet.setRowView(1, 500);
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //
        WritableSheet sheet = workbook.createSheet("第一页", 0);
        for(int i=0;i<9;i++)
        {
    		CellView cv = new CellView();  
    		cv.setAutosize(true);//自动宽度  
    		cv.setSize(18); //最小宽度  
    		sheet.setColumnView(i,cv);
        }
		
		try {
			sheet.addCell(new Label(0, 0, "序号", wcf));
			sheet.addCell(new Label(1, 0, "URL", wcf));
			sheet.addCell(new Label(2, 0, "ID", wcf));
			sheet.addCell(new Label(3, 0, "宝贝名称", wcf));
			sheet.addCell(new Label(4, 0, "宝贝价格", wcf));
			sheet.addCell(new Label(5, 0, "尺码", wcf));
			sheet.addCell(new Label(6, 0, "颜色", wcf));
			sheet.addCell(new Label(7, 0, "平台", wcf));
			sheet.addCell(new Label(8, 0, "采集时间", wcf));
			return sheet;
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//start to run the thread
		String filePath=this.resultPath+"/"+"结果.xls";
		WritableWorkbook workbook=null;
		try {
			workbook = Workbook.createWorkbook(new File(filePath));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		WritableSheet sheet=initlizeHeader(workbook);
		LOG.info("start the task "+this.id);
		createTaskInfoAndSave();
		//
		LOG.info(pagesUrl.length);
		for(int i=0;i<this.pagesUrl.length;i++)
		{
			String pageUrl=pagesUrl[i];
			PAGETYPE type=getPageType(pageUrl);
			LOG.info(this.type);
			Page page=PageFactory.GetPage(this.type,this.id);
			page.setTask(pageUrl, this.resultPath,encrypt);
			page.startGenerate();
			writeResult(sheet,i, page);
		}
		//准备结束任务
		try {
			workbook.write();
			workbook.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//结束任务
		close();
	}
	
	public void writeResult(WritableSheet sheet,int index,Page page)
	{
		 WritableFont wf = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,
                 UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
         WritableCellFormat wcf = new WritableCellFormat(wf);
         try {
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf.setAlignment(Alignment.JUSTIFY);
	        //sheet.setRowView(1, 500);
		} catch (WriteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         
		if(page==null)
		{
			LOG.info("the page is null");
			return;
		}
		try {
			if(sheet==null)
			{
				LOG.info("the sheet is null");
			}
			int row=index+1;
			sheet.addCell(new Label(0,row,index+"",wcf));
			sheet.addCell(new Label(1,row,page.getPageUrl(),wcf));
			sheet.addCell(new Label(2,row,page.getID(),wcf));
			sheet.addCell(new Label(3,row,page.getTitle(),wcf));
			sheet.addCell(new Label(4,row,page.getPrice(),wcf));
			sheet.addCell(new Label(5,row,page.getSize(),wcf));
			sheet.addCell(new Label(6,row,page.getColor(),wcf));
			sheet.addCell(new Label(7,row,page.getPlatform(),wcf));
			sheet.addCell(new Label(8,row,page.getDate(),wcf));
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @see 获取一个页面的网页类型
	 * @return
	 */
	public PAGETYPE getPageType(String pageUrl)
	{
		String page=pageUrl.split("\\?")[0];
		if(page.contains("tmall"))
		{
			return PAGETYPE.TMALL;
		}
		else if(page.contains("taobao"))
		{
			return PAGETYPE.TAOBAO;
		}
		else if(page.contains("1688"))
		{
			return PAGETYPE.ALIBABA;
		}
		return null;
	}

	public void close() {
		container.closeTask(this.id);
	}
	
	public void createTaskInfoAndSave()
	{
		taskInfo=new TaskInfo();
		taskInfo.setTaskInfo(this.id, this.resultPath);
		container.addTask(this.id, taskInfo);
	}
	
	/**
	 * @author wozipa
	 * @date 2017-3-6
	 * @see 转换成json对象
	 * @return
	 */
	public JSONObject toJSON()
	{
		return null;
	}
	
}
