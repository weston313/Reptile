package com.wozipa.reptile.app.task;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wozipa.reptile.app.task.TaskInfo.STATE;
import com.wozipa.reptile.shop.CategoryPage;
import com.wozipa.reptile.shop.ShopFactory;

public class ClassifyTaskThread extends Thread{
	
	private static final Log LOG=LogFactory.getLog(ClassifyTaskThread.class);
	
	private static final double THREAD_INIT=10;
	private static final double PAGES_REP=80;
	private static final double THREAD_CLOSE=10;
	
	public enum PAGETYPE{
		TMALL,TAOBAO,ALIBABA
	}
	
	private TaskContainer container=null;
	private TaskInfo taskInfo=null;
	
	private String[] classifiesPath;
	private String id;
	private String resultPath;
	private String type;
	private int encrypt;
	private double progress;
	
	private TasksProgressListern listern=null;
	private double taskProgress=0;
	
	private Map<String, ItemTaskThread> itemThreadContianer=null;
	
	public ClassifyTaskThread(String id,String[] classifiesPath,String resultPath,String type,int encrypt) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.resultPath=resultPath;
		this.classifiesPath=classifiesPath;
		this.type=type;
		this.encrypt=encrypt;
		this.progress=0;
		init();
	}
	
	public void init()
	{
		//create the workspace directory
		File workspace=new File(resultPath+"/"+id);
		if(!workspace.exists())
		{
			workspace.mkdirs();
		}
		this.resultPath=workspace.getPath();
		//
		taskInfo=new TaskInfo();
		container=TaskContainer.getInstance();
		//
		itemThreadContianer=new HashMap<>();
		//
		createTaskInfoAndSave();
		//子任务运行状态监控，需要整体任务信息
		listern=new TasksProgressListern();
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(String classifyPath:classifiesPath)
		{
			CategoryPage page=ShopFactory.GetPage(this.type);
			page.setTask(classifyPath);
			String[] goodUrls=page.getGoodUrls();
			String taskId=UUID.randomUUID().toString();
			ItemTaskThread taskThread=new ItemTaskThread(taskId, goodUrls, this.resultPath,this.type, encrypt);
			taskThread.setSubTask();
			taskThread.start();
			itemThreadContianer.put(taskId,taskThread);
		}
		//
		taskInfo.setPregress(THREAD_INIT);
		taskProgress=PAGES_REP/itemThreadContianer.size();
		LOG.info("there is "+itemThreadContianer.size()+"sub task running");
		listern.start();
		while(true)
		{
			if(itemThreadContianer.isEmpty())
			{
				close();
				break;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void close()
	{
		listern.shutdown();
		try {
			listern.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		taskInfo.setPregress(100);
		taskInfo.setState(STATE.END);
	}
	
	public void createTaskInfoAndSave()
	{
		taskInfo.setTaskInfo(this.id, this.resultPath);
		container.addTask(this.id, taskInfo);
	}
	
	public class TasksProgressListern extends Thread
	{
		private boolean doRun=true;
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(doRun){
				Set<String> taskIdSet=itemThreadContianer.keySet();
				for(String taskId:taskIdSet)
				{
					ItemTaskThread thread=itemThreadContianer.get(taskId);
					if(!thread.isAlive())
					{
						LOG.info("the task "+id+" is over");
						taskInfo.addProgress(taskProgress);
						itemThreadContianer.remove(id);
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void shutdown()
		{
			this.doRun=false;
		}
	}
}
