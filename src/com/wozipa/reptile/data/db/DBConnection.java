package com.wozipa.reptile.data.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.wozipa.reptile.data.Connectin;
import com.wozipa.reptile.data.Data;
import com.wozipa.reptile.derby.DerbyDB;

public class DBConnection extends Connectin<IdDBData>{
	
	private static final Logger LOGGER=Logger.getLogger(DBConnection.class);
	
	private static final String TABLE_NAME="ids";
	private static final String COLUMN_ENID="enId";
	private static final String COLUMN_OGID="ogId";
	private static final String COLUMN_RESULT="result";
	
	private Connection connection=null;
	private Statement statement=null;
	

	public DBConnection() {
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

}
