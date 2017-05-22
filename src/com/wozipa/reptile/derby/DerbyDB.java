package com.wozipa.reptile.derby;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.omg.CORBA.PRIVATE_MEMBER;

import com.wozipa.reptile.util.ReptileUtils;

public class DerbyDB {
	
	private static final Logger LOGGER=Logger.getLogger(DerbyDB.class);

	private static final String DB_URL_PREFIX="jdbc:derby:";
	public static final String DB_NAME="reptile";
	
	private static volatile DerbyDB DB=null;
	
	public static DerbyDB GetDataBase()
	{
		if(DB==null)
		{
			synchronized (DerbyDB.class) {
				if(DB==null)
				{
					DB=new DerbyDB();
				}
			}	
		}
		return DB;
	}
	
	private Connection connection;
	
	private DerbyDB()
	{
		initDatabse();
	}
	
	public Connection getConnection()
	{
		return connection;
	}
	
	public void initDatabse()
	{
		String dataPath=ReptileUtils.GetDataPath();
		String derbyUrl=DB_URL_PREFIX+dataPath+"/"+DB_NAME+";create=true";
		System.out.println(derbyUrl);
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			connection=DriverManager.getConnection(derbyUrl);
			connection.setAutoCommit(true);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author wozipa
	 * @date 2017-5-22 
	 * @see get the table existed or not
	 * @param table
	 * @return
	 */
	public boolean tableExist(String table)
	{
		try {
			DatabaseMetaData metaData=connection.getMetaData();
			ResultSet resultSet=metaData.getTables(null, null, null, new String[]{"TABLE"});
			Set<String> hashSet=new HashSet<>();
			while(resultSet.next())
			{
				hashSet.add(resultSet.getString("TABLE_NAME").toLowerCase());
			}
			if(hashSet.contains(table))
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}	
}
