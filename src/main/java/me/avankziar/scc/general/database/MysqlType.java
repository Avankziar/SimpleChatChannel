package main.java.me.avankziar.scc.general.database;

import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.general.objects.ItemJson;
import main.java.me.avankziar.scc.general.objects.Mail;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.general.objects.UsedChannel;

public enum MysqlType
{
	CHATUSER("sccPlayerData", new ChatUser(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " player_uuid char(36) NOT NULL UNIQUE,"
    		+ " player_name varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,"
    		+ " roleplay_name TEXT,"
    		+ " roleplayrenamecooldown BIGINT,"
    		+ " mutetime BIGINT,"
    		+ " spy boolean, channelmessage boolean, lasttimejoined BIGINT, joinmessage boolean,"
    		+ " serverlocation MEDIUMTEXT);"),
	IGNOREOBJECT("sccIgnorelist", new IgnoreObject(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY, player_uuid char(36) NOT NULL, ignore_uuid char(36) NOT NULL,"
    		+ " ignore_name char(16) NOT NULL);"),
	PERMANENTCHANNEL("sccPermanentChannels", new PermanentChannel(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY, channel_name TEXT NOT NULL, "
    		+ " creator TEXT NOT NULL, vice MEDIUMTEXT, members MEDIUMTEXT, password TEXT, banned MEDIUMTEXT,"
    		+ " symbolextra TEXT, namecolor TEXT, chatcolor TEXT);"),
	ITEMJSON("sccItemJson", new ItemJson(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " owner TEXT, itemname TEXT, itemdisplayname TEXT, jsonstring LONGTEXT, base64 LONGTEXT);"),
	USEDCHANNEL("sccPlayerUsedChannels", new UsedChannel(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " uniqueidentifiername TEXT, player_uuid TEXT, used BOOLEAN);"),
	MAIL("sccMails", new Mail(), "ALL",
			"CREATE TABLE IF NOT EXISTS `%%tablename%%"
			+ "` (id int AUTO_INCREMENT PRIMARY KEY,"
    		+ " sender_uuid TEXT, sender_name TEXT, reciver_uuid TEXT, reciver_name TEXT,"
    		+ " carboncopy_uuid MEDIUMTEXT, carboncopy_name MEDIUMTEXT,"
    		+ " sendeddate BIGINT, readeddate BIGINT, subject TEXT, rawmessage MEDIUMTEXT);")
	;
	
	private MysqlType(String tableName, Object object, String usedOnServer, String setupQuery)
	{
		this.tableName = tableName;
		this.object = object;
		this.usedOnServer = usedOnServer;
		this.setupQuery = setupQuery.replace("%%tablename%%", tableName);
	}
	
	private final String tableName;
	private final Object object;
	private final String usedOnServer;
	private final String setupQuery;

	public String getValue()
	{
		return tableName;
	}
	
	public Object getObject()
	{
		return object;
	}
	
	public String getUsedOnServer()
	{
		return usedOnServer;
	}
	
	public String getSetupQuery()
	{
		return setupQuery;
	}
}