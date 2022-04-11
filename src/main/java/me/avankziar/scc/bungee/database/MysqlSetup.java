package main.java.me.avankziar.scc.bungee.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;

public class MysqlSetup 
{
	private SimpleChatChannels plugin;
	private Connection conn = null;
	
	public MysqlSetup(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
		loadMysqlSetup();
	}
	
	public boolean loadMysqlSetup()
	{
		if(!connectToDatabase())
		{
			return false;
		}
		if(!setupDatabaseI())
		{
			return false;
		}
		if(!setupDatabaseII())
		{
			return false;
		}
		if(!setupDatabaseIII())
		{
			return false;
		}
		if(!setupDatabaseIV())
		{
			return false;
		}
		if(!setupDatabaseV())
		{
			return false;
		}
		if(!setupDatabaseVI())
		{
			return false;
		}
		return true;
	}
	
	public boolean connectToDatabase() 
	{
		SimpleChatChannels.log.info("Connecting to the database...");
		boolean bool = false;
	    try
	    {
	    	// Load new Drivers for papermc
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    	bool = true;
	    } catch (Exception e)
	    {
	    	bool = false;
	    } 
	    try
	    {
	    	if (bool == false)
	    	{
	    		// Load old Drivers for spigot
	    		Class.forName("com.mysql.jdbc.Driver");
	    	}
	        Properties properties = new Properties();
            properties.setProperty("user", plugin.getYamlHandler().getConfig().getString("Mysql.User"));
            properties.setProperty("password", plugin.getYamlHandler().getConfig().getString("Mysql.Password"));
            properties.setProperty("autoReconnect", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true) + "");
            properties.setProperty("verifyServerCertificate", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false) + "");
            properties.setProperty("useSSL", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false) + "");
            properties.setProperty("requireSSL", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false) + "");
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + plugin.getYamlHandler().getConfig().getString("Mysql.Host") 
            		+ ":" + plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306) + "/" 
            		+ plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName"), properties);
           
          } catch (ClassNotFoundException e) 
		{
        	  SimpleChatChannels.log.severe("Could not locate drivers for mysql! Error: " + e.getMessage());
            return false;
          } catch (SQLException e) 
		{
        	  SimpleChatChannels.log.severe("Could not connect to mysql database! Error: " + e.getMessage());
            return false;
          }
		SimpleChatChannels.log.info("Database connection successful!");
		return true;
	}
	
	public boolean setupDatabaseI() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameI
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		        		+ " player_uuid char(36) NOT NULL UNIQUE,"
		        		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
		        		+ " roleplay_name TEXT,"
		        		+ " roleplayrenamecooldown BIGINT,"
		        		+ " mutetime BIGINT,"
		        		+ " spy boolean, channelmessage boolean, lasttimejoined BIGINT, joinmessage boolean,"
		        		+ " serverlocation MEDIUMTEXT);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        SimpleChatChannels.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public boolean setupDatabaseII() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameII
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY, player_uuid char(36) NOT NULL, ignore_uuid char(36) NOT NULL,"
		        		+ " ignore_name char(16) NOT NULL);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        SimpleChatChannels.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public boolean setupDatabaseIII() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameIII
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY, channel_name TEXT NOT NULL, "
		        		+ " creator TEXT NOT NULL, vice MEDIUMTEXT, members MEDIUMTEXT, password TEXT, banned MEDIUMTEXT,"
		        		+ " symbolextra TEXT, namecolor TEXT, chatcolor TEXT);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        SimpleChatChannels.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public boolean setupDatabaseIV() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameIV
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		        		+ " owner TEXT, itemname TEXT, itemdisplayname TEXT, jsonstring MEDIUMTEXT, base64 MEDIUMTEXT);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        SimpleChatChannels.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public boolean setupDatabaseV() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameV
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		        		+ " uniqueidentifiername TEXT, player_uuid TEXT, used BOOLEAN);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        SimpleChatChannels.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public boolean setupDatabaseVI() 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		      try 
		      {	        
		        String data = "CREATE TABLE IF NOT EXISTS `" + plugin.getMysqlHandler().tableNameVI
		        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
		        		+ " sender_uuid TEXT, sender_name TEXT, reciver_uuid TEXT, reciver_name TEXT,"
		        		+ " carboncopy_uuid MEDIUMTEXT, carboncopy_name MEDIUMTEXT,"
		        		+ " sendeddate BIGINT, readeddate BIGINT, subject TEXT, rawmessage MEDIUMTEXT);";
		        query = conn.prepareStatement(data);
		        query.execute();
		      } catch (SQLException e) 
		      {
		        e.printStackTrace();
		        SimpleChatChannels.log.severe("Error creating tables! Error: " + e.getMessage());
		        return false;
		      } finally 
		      {
		    	  try 
		    	  {
		    		  if (query != null) 
		    		  {
		    			  query.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    		  return false;
		    	  }
		      }
		}
		return true;
	}
	
	public Connection getConnection() 
	{
		checkConnection();
		return conn;
	}
	
	public void checkConnection() 
	{
		try {
			if (conn == null) 
			{
				SimpleChatChannels.log.warning("Connection failed. Reconnecting...");
				reConnect();
			}
			if (!conn.isValid(3)) 
			{
				SimpleChatChannels.log.warning("Connection is idle or terminated. Reconnecting...");
				reConnect();
			}
			if (conn.isClosed() == true) 
			{
				SimpleChatChannels.log.warning("Connection is closed. Reconnecting...");
				reConnect();
			}
		} catch (Exception e) 
		{
			SimpleChatChannels.log.severe("Could not reconnect to Database! Error: " + e.getMessage());
		}
	}
	
	public boolean reConnect() 
	{
		boolean bool = false;
	    try
	    {
	    	// Load new Drivers for papermc
	    	Class.forName("com.mysql.cj.jdbc.Driver");
	    	bool = true;
	    } catch (Exception e)
	    {
	    	bool = false;
	    } 
	    try
	    {
	    	if (bool == false)
	    	{
	    		// Load old Drivers for spigot
	    		Class.forName("com.mysql.jdbc.Driver");
	    	}            
            long start = 0;
			long end = 0;
			
		    start = System.currentTimeMillis();
		    SimpleChatChannels.log.info("Attempting to establish a connection to the MySQL server!");
            Properties properties = new Properties();
            properties.setProperty("user", plugin.getYamlHandler().getConfig().getString("Mysql.User"));
            properties.setProperty("password", plugin.getYamlHandler().getConfig().getString("Mysql.Password"));
            properties.setProperty("autoReconnect", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true) + "");
            properties.setProperty("verifyServerCertificate", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false) + "");
            properties.setProperty("useSSL", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false) + "");
            properties.setProperty("requireSSL", 
            		plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false) + "");
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + plugin.getYamlHandler().getConfig().getString("Mysql.Host") 
            		+ ":" + plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306) + "/" 
            		+ plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName"), properties);
		    end = System.currentTimeMillis();
		    SimpleChatChannels.log.info("Connection to MySQL server established!");
		    SimpleChatChannels.log.info("Connection took " + ((end - start)) + "ms!");
            return true;
		} catch (Exception e) 
		{
			SimpleChatChannels.log.severe("Error re-connecting to the database! Error: " + e.getMessage());
			return false;
		}
	}
	
	public void closeConnection() 
	{
		try
		{
			SimpleChatChannels.log.info("Closing database connection...");
			conn.close();
			conn = null;
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}