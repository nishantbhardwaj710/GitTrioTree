package com.triotree.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seleniumhq.jetty9.util.StringUtil;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class DBUtil {
	private static final Logger logger = LogManager
			.getLogger(DBUtil.class.getName());

	private static PropertyFile propertyFile = new PropertyFile();
	private static final String Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static String ConnectionString = null;
	//	private static String databaseIP = null;
	//	private static String databaseUserName = null;
	//	private static String databasePassword = null;
	private static String databaseDomain= null;
	private static String databaseAuthentication = null;
	private static String ConnectionStringAtlasRecaptcha = null;
	//
	private static final String databaseName = propertyFile.getProperty("databaseName");
	private static final String databaseIP = propertyFile.getProperty("dbIP");
	private static final String databaseUserName = propertyFile.getProperty("dbUserName");
	private static final String databasePassword = propertyFile.getProperty("dbPassword");

	
	
	

	public static String getConnectionString(String databaseName){
		if(databaseAuthentication.equalsIgnoreCase("SQL Server Authentication")){
			ConnectionString = "jdbc:sqlserver://"+databaseIP+";databaseName="+databaseName+";instance=MSSQLSERVER;"+"domain="+databaseDomain;
		}
		else if(databaseAuthentication.equalsIgnoreCase("Windows Authentication")){
			ConnectionString = "jdbc:jtds:sqlserver://"+databaseIP+";instance=MSSQLSERVER;domain="+databaseDomain+";integratedSecurity=true;DatabaseName="+databaseName;
		}
		return ConnectionString;
	}

	public static String getConnectionString(String databaseName, String dbIP){
		//		if(databaseAuthentication.equalsIgnoreCase("SQL Server Authentication")){
		ConnectionString = "jdbc:sqlserver://"+dbIP+";databaseName="+databaseName+";instance=MSSQLSERVER;"+"domain="+databaseDomain;
		//		}
		//		else if(databaseAuthentication.equalsIgnoreCase("Windows Authentication")){
		//			ConnectionString = "jdbc:jtds:sqlserver://"+dbIP+";instance=MSSQLSERVER;domain="+databaseDomain+";integratedSecurity=true;DatabaseName="+databaseName;
		//		}
		logger.info("ConnectionString::"+ConnectionString);
		return ConnectionString;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> performDatabaseQuery(String sQuery,String databaseName){
		JdbcTemplate jdbcTemplate;
		jdbcTemplate = new JdbcTemplate(getDataSource(databaseName));
		logger.info("QUERY TRIGGERED IS "+sQuery+"\n");
		ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();		
		List<Map<String, Object>> userDataList = jdbcTemplate.query(sQuery,rowMapper);		
		return userDataList;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> performDatabaseQuery(String sQuery,String databaseName, String dbIP){
		JdbcTemplate jdbcTemplate;
		//		if(databaseName.trim().equalsIgnoreCase("RodanFieldsLive")){
		//			jdbcTemplate = new JdbcTemplate(getDataSource("RodanFieldsLive", dbIP2));
		//		}
		//		else{
		//			jdbcTemplate = new JdbcTemplate(getDataSource("RFOperations", dbIP2));
		//		}
		jdbcTemplate = new JdbcTemplate(getDataSource(databaseName, dbIP));
		logger.info("QUERY TRIGGERED IS "+sQuery+"\n");
		ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();  
		List<Map<String, Object>> userDataList = jdbcTemplate.query(sQuery,rowMapper);  
		return userDataList;
	}

	@SuppressWarnings("unchecked")
	public static String performDatabaseQuery(String sQuery)
	{
		JdbcTemplate jdbcTemplate;
		jdbcTemplate = new JdbcTemplate(getDataSource(databaseName, databaseIP, databaseUserName, databasePassword));
		logger.info("QUERY TRIGGERED IS "+sQuery+"\n");
		ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();  
		List<Map<String, Object>> userDataList = jdbcTemplate.query(sQuery,rowMapper);  
		try {
			return (String) jdbcTemplate.queryForObject( sQuery,new Object[] {},String.class); 
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("exception "+e);
			return null;
		}
	}

	public static DataSource getDataSource(String databaseName) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Driver);
		dataSource.setUrl(getConnectionString(databaseName));
		dataSource.setUsername(databaseUserName);
		dataSource.setPassword(databasePassword);
		return dataSource;
	}

	public static DataSource getDataSource(String databaseName, String dbIP) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Driver);
		dataSource.setUrl(getConnectionString(databaseName, dbIP));
		dataSource.setUsername(databaseUserName);
		dataSource.setPassword(databasePassword);
		return dataSource;
	}

	public static DataSource getDataSource(String databaseName, String dbIP,String dbUserName, String dbPassword) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(Driver);
		dataSource.setUrl(getConnectionString(databaseName, dbIP));
		dataSource.setUsername(dbUserName);
		dataSource.setPassword(dbPassword);
		return dataSource;
	}

	@SuppressWarnings("unchecked")
	public static void performDatabaseQueryForUpdate(String sQuery,String databaseName){
		JdbcTemplate jdbcTemplate;
		jdbcTemplate = new JdbcTemplate(getDataSource(databaseName));
		logger.info("QUERY TRIGGERED IS "+sQuery+"\n");
		jdbcTemplate.execute(sQuery);
	}

	@SuppressWarnings("unchecked")
	public static void performDatabaseQueryForUpdate(String sQuery,String databaseName,String dbIP){
		JdbcTemplate jdbcTemplate;
		jdbcTemplate = new JdbcTemplate(getDataSource(databaseName,dbIP));
		logger.info("QUERY TRIGGERED IS "+sQuery+"\n");
		jdbcTemplate.execute(sQuery);
	}

}

