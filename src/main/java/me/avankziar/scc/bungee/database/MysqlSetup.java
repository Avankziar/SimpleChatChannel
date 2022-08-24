package main.java.me.avankziar.scc.bungee.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;

public class MysqlSetup 
{
	private String host;
	private int port;
	private String database;
	private String user;
	private String password;
	private boolean isAutoConnect;
	private boolean isVerifyServerCertificate;
	private boolean isSSLEnabled;
	
	public MysqlSetup(SimpleChatChannels plugin)
	{
		boolean adm = plugin.getYamlHandler().getConfig().getBoolean("useIFHAdministration", false);
		if(plugin.getAdministration() == null)
		{
			adm = false;
		}
		String path = plugin.getYamlHandler().getConfig().getString("IFHAdministrationPath");
		
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
		return reConnect();
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
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, properties);
            return conn;
		} catch (Exception e) 
		{
			SimpleChatChannels.log.severe("Error (re-)connecting to the database! Error: " + e.getMessage());
			return null;
		}
	}
	
	private boolean baseSetup(String data) 
	{
		try (Connection conn = getConnection(); PreparedStatement query = conn.prepareStatement(data))
		{
			query.execute();
		} catch (SQLException e) 
		{
			SimpleChatChannels.log.log(Level.WARNING, "Could not build data source. Or connection is null", e);
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