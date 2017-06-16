package com.wozipa.reptile.data.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.javascript.host.Set;
import com.ibm.icu.util.BytesTrie.Result;
import com.wozipa.reptile.data.Connectin;
import com.wozipa.reptile.data.Data;
import com.wozipa.reptile.derby.DerbyDB;

public class DBConnection extends Connectin<IdDBData>{
	
	private static final Logger LOGGER=Logger.getLogger(DBConnection.class);
	
	private static final String TABLE_NAME="ids";
	private static final String COLUMN_ID="id";
	private static final String COLUMN_ENID="enId";
	private static final String COLUMN_OGID="ogId";
	private static final String COLUMN_TASK="task";
	private static final String COLUMN_RESULT="result";
	
	private static volatile DBConnection DATABASE=null;
	
	public static DBConnection GetDataBase()
	{
		if(DATABASE==null)
		{
			synchronized (DBConnection.class) {
				if(DATABASE==null)
				{
					DATABASE=new DBConnection();
				}
			}
		}
		return DATABASE;
	}
	
	private Connection connection=null;
	private Statement statement=null;
	

	private DBConnection() {
		// TODO Auto-generated constructor stub
		try {
			DerbyDB derbyDB=DerbyDB.GetDataBase();
			connection=derbyDB.getConnection();
			statement=connection.createStatement();
			if(!derbyDB.tableExist(TABLE_NAME))
			{
				createTable();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(Data data) {
		// TODO Auto-generated method stub
		
		try {
			IdDBData dbData=(IdDBData) data;
			StringBuilder sb=new StringBuilder();
			sb.append("insert into ").append(TABLE_NAME).append(" values('")
				.append(dbData.getEnId()).append("','")
				.append(dbData.getOgId()).append("','")
				.append(dbData.getResult()).append("')");
			System.out.println(sb.toString());
			statement.execute(sb.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean tableExist()
	{
		return DerbyDB.GetDataBase().tableExist(TABLE_NAME);
	}
	
	public void createTable()
	{
		String createSql="create table ids(enId varchar(20),ogId varchar(20),result varchar(100))";
		System.out.println(createSql);
		try {
			statement.execute(createSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Wozipa
	 * @date 2017-5-22
	 * @see search data fromt table ids
	 * @param id
	 * @return
	 */
	public List<IdDBData> search(String id)
	{
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select * from ").append(TABLE_NAME).append(" where enId like '%").append(id)
				.append("%' or ogId like '%").append("id").append("%'");
		List<IdDBData> datas=new ArrayList<>();
		try {
			
			ResultSet resultSet=statement.executeQuery(stringBuffer.toString());
			while(resultSet.next())
			{
				IdDBData data=new IdDBData(resultSet.getString(COLUMN_ENID),resultSet.getString(COLUMN_OGID),resultSet.getString(COLUMN_RESULT));
				datas.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author wozipa
	 * @date 2017-6-15 
	 * @see delete from where
	 * @param enId
	 * @param ogId
	 * @param resultPath
	 */
	public void delete(String enId,String ogId,String resultPath)
	{
		StringBuffer delSB=new StringBuffer();
		delSB.append("delete from ").append(TABLE_NAME).append("where ")
			.append(COLUMN_ENID).append("='").append(enId).append("' and ")
			.append(COLUMN_OGID).append("='").append(ogId).append("' and")
			.append(COLUMN_RESULT).append("='").append(resultPath).append("'");
		System.out.println(delSB.toString());
		try {
			statement.execute(delSB.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author wozipa
	 * @date 2017-6-15
	 * @see change the result path of the path 
	 * @param enId
	 * @param ogId
	 * @param old
	 * @param newPath
	 */
	public void changeResultPath(String enId,String ogId,String old,String newPath)
	{
		StringBuffer sb=new StringBuffer();
		sb.append("update table ").append(TABLE_NAME).append(" set ").append(COLUMN_RESULT).append("='").append(newPath).append("'")
				.append(" where ").append(COLUMN_ENID).append("='").append(enId).append("' and ")
				.append(COLUMN_OGID).append("='").append(ogId).append("' and")
				.append(COLUMN_RESULT).append("='").append(old).append("'");
		System.out.println(sb.toString());
		try {
			statement.execute(sb.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
