package com.wozipa.reptile.app.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibm.icu.text.SimpleDateFormat;

public class TaskInfo {
	
	private static final Log LOG=LogFactory.getLog(TaskInfo.class);
	
	public TaskInfo()
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.time=sdf.format(new Date());
		this.state=STATE.RUNNING;
		this.pregress=0;
	}
	
	public enum STATE{
		
		INIT("初始化中"),RUNNING("运行中"),KILLED("终止"),END("完成");
		
		private String value;
		private STATE(String _value)
		{
			this.value=_value;
		}
		
		public String getValue()
		{
			return this.value;
		}
	}
	
	public void setTaskInfo(String id,String resultPath)
	{
		this.id=id;
		this.resultPath=resultPath;	
	}
	
	public void progress(double progress)
	{
		this.pregress=progress;
	}
	
	private String id;
	private STATE state;
	private String resultPath;
	private String time;
	private double pregress;
	
	public double getPregress() {
		return pregress;
	}

	public void setPregress(double pregress) {
		this.pregress = pregress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public STATE getState() {
		return state;
	}

	public void setState(STATE state) {
		this.state = state;
	}

	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String[] toText()
	{
		List<String> fieldList=new ArrayList<>();
		fieldList.add(this.id);
		fieldList.add(this.pregress+"");
		fieldList.add(this.resultPath);
		fieldList.add(this.time);
		fieldList.add(this.state.getValue());
		return fieldList.toArray(new String[fieldList.size()]);
	}
	
	public void addProgress(double value)
	{
		pregress=pregress+value;
	}

}
