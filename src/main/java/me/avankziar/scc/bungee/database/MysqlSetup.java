package main.java.me.avankziar.scc.bungee.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;

public class MysqlSetup 
{
	private Connection conn = null;
	final private String host;
	final private int port;
	final private String database;
	final private String user;
	final private String password;
	final private boolean isAutoConnect;
	final private boolean isVerifyServerCertificate;
	final private boolean isSSLEnabled;
	
	public MysqlSetup(SimpleChatChannels plugin)
	{
		boolean adm = plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration", false);
		String path = plugin.getYamlHandler().getConfig().getString("IFHAdministrationPath");
		if(plugin.getAdministration() == null || plugin.getAdministration().getHost(path) == null)
		{
			adm = false;
		}
		host = adm ? plugin.getAdministration().getHost(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Host");
		port = adm ? plugin.getAdministration().getPort(path)
				: plugin.getYamlHandler().getConfig().getInt("Mysql.Port", 3306);
		database = adm ? plugin.getAdministration().getDatabase(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.DatabaseName");
		user = adm ? plugin.getAdministration().getUsername(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.User");
		password = adm ? plugin.getAdministration().getPassword(path)
				: plugin.getYamlHandler().getConfig().getString("Mysql.Password");
		isAutoConnect = adm ? plugin.getAdministration().isAutoReconnect(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.AutoReconnect", true);
		isVerifyServerCertificate = adm ? plugin.getAdministration().isVerifyServerCertificate(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.VerifyServerCertificate", false);
		isSSLEnabled = adm ? plugin.getAdministration().useSSL(path)
				: plugin.getYamlHandler().getConfig().getBoolean("Mysql.SSLEnabled", false);
		loadMysqlSetup();
	}
	
	public boolean connectToDatabase() 
	{
		SimpleChatChannels.log.info("Connecting to the database...");
		Connection conn = getConnection();
		if(conn != null)
		{
			SimpleChatChannels.log.info("Database connection successful!");
		} else
		{
			return false;
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
				//MIM.log.warning("Connection failed. Reconnecting...");
				reConnect();
			}
			if (!conn.isValid(3)) 
			{
				//MIM.log.warning("Connection is idle or terminated. Reconnecting...");
				reConnect();
			}
			if (conn.isClosed() == true) 
			{
				//MIM.log.warning("Connection is closed. Reconnecting...");
				reConnect();
			}
		} catch (Exception e) 
		{
			SimpleChatChannels.log.severe("Could not reconnect to Database! Error: " + e.getMessage());
		}
	}
	
	private Connection reConnect() 
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
            Properties properties = new Properties();
            properties.setProperty("user", user);
            properties.setProperty("password", password);
            properties.setProperty("autoReconnect", String.valueOf(isAutoConnect));
            properties.setProperty("verifyServerCertificate", String.valueOf(isVerifyServerCertificate));
            properties.setProperty("useSSL", String.valueOf(isSSLEnabled));
            properties.setProperty("requireSSL", String.valueOf(isSSLEnabled));
            //Connect to database
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, properties);
            return conn;
		} catch (Exception e) 
		{
			SimpleChatChannels.log.severe("Error (re-)connecting to the database! Error: " + e.getMessage());
			return null;
		}
	}
	
	private boolean baseSetup(String data) 
	{
		if (conn != null) 
		{
			PreparedStatement query = null;
		    try 
		    {
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
	
	public boolean setupDatabaseI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.CHATUSER.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " player_uuid char(36) NOT NULL UNIQUE,"
        		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
        		+ " roleplay_name TEXT,"
        		+ " roleplayrenamecooldown BIGINT,"
        		+ " mutetime BIGINT,"
        		+ " spy boolean, channelmessage boolean, lasttimejoined BIGINT, joinmessage boolean,"
        		+ " serverlocation MEDIUMTEXT);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.IGNOREOBJECT.getValue()
	        		+ "` (id int AUTO_INCREMENT PRIMARY KEY, player_uuid char(36) NOT NULL, ignore_uuid char(36) NOT NULL,"
	        		+ " ignore_name char(16) NOT NULL);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseIII() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.PERMANENTCHANNEL.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY, channel_name TEXT NOT NULL, "
        		+ " creator TEXT NOT NULL, vice MEDIUMTEXT, members MEDIUMTEXT, password TEXT, banned MEDIUMTEXT,"
        		+ " symbolextra TEXT, namecolor TEXT, chatcolor TEXT);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseIV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.ITEMJSON.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " owner TEXT, itemname TEXT, itemdisplayname TEXT, jsonstring LONGTEXT, base64 LONGTEXT);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseV() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.USEDCHANNEL.getValue()
        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
        		+ " uniqueidentifiername TEXT, player_uuid TEXT, used BOOLEAN);";
		baseSetup(data);
		return true;
	}
	
	public boolean setupDatabaseVI() 
	{
		String data = "CREATE TABLE IF NOT EXISTS `" + MysqlHandler.Type.MAIL.getValue()
	        		+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
	        		+ " sender_uuid TEXT, sender_name TEXT, reciver_uuid TEXT, reciver_name TEXT,"
	        		+ " carboncopy_uuid MEDIUMTEXT, carboncopy_name MEDIUMTEXT,"
	        		+ " sendeddate BIGINT, readeddate BIGINT, subject TEXT, rawmessage MEDIUMTEXT);";
		baseSetup(data);
		return true;
	}
}