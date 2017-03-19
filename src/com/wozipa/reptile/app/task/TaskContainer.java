package com.wozipa.reptile.app.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wozipa.reptile.app.task.TaskInfo.STATE;

public class TaskContainer {
	
	private static final Log LOG=LogFactory.getLog(TaskContainer.class);
	
	private Map<String,TaskInfo> RUN_CONTAINER=new HashMap<>();
	private Map<String,TaskInfo> OVER_CONTAINER=new HashMap<>();
	
	private volatile static TaskContainer container=null;
	
	private TaskContainer(){}
	
	public static TaskContainer getInstance()
	{
		if(container==null)
		{
			synchronized (TaskContainer.class) {
				if(container==null)
				{
					container=new TaskContainer();
					TaskInfo taskInfo=new TaskInfo();
					taskInfo.setPregress(0);
					taskInfo.setTaskInfo("test","/test");
					taskInfo.setState(STATE.RUNNING);
					container.addTask("test", taskInfo);
				}
			}
		}
		return container;
	}
	
	//进行
	public synchronized void addTask(String id,TaskInfo info)
	{
		RUN_CONTAINER.put(id,info);
	}
	
	public synchronized TaskInfo getTaskInfor(String id)
	{
		if(RUN_CONTAINER.containsKey(id))
		{
			return RUN_CONTAINER.get(id);
		}
		if(OVER_CONTAINER.containsKey(id))
		{
			return OVER_CONTAINER.get(id);
		}
		return null;
	}
	
	public synchronized java.util.List<String[]> getTasks()
	{
		java.util.List<String[]> infors=new ArrayList<>();
		Set<String> idSet=RUN_CONTAINER.keySet();
		for(String id:idSet)
		{
			TaskInfo taskInfo=RUN_CONTAINER.get(id);
			infors.add(taskInfo.toText());
		}
		Set<String> overIdSet=OVER_CONTAINER.keySet();
		for(String id:overIdSet)
		{
			TaskInfo taskInfo=OVER_CONTAINER.get(id);
			infors.add(taskInfo.toText());
		}
		return infors;
	}
	
	/**
	 * @author wozipa
	 * @date 2017-3-6
	 * @see 某一个任务完成后进行的操作
	 * @param key
	 */
	public synchronized void closeTask(String key)
	{
		TaskInfo taskInfo=RUN_CONTAINER.remove(key);
		taskInfo.setPregress(1);
		taskInfo.setState(STATE.END);
		OVER_CONTAINER.put(key,taskInfo);
	}
}
