package main.java.de.avankziar.simplechatchannels.bungee;

import java.util.ArrayList;
import java.util.logging.Logger;

import main.java.de.avankziar.afkrecord.bungee.AfkRecord;
import main.java.de.avankziar.punisher.bungee.PunisherBungee;
import main.java.de.avankziar.simplechatchannels.bungee.commands.CMDClickChat;
import main.java.de.avankziar.simplechatchannels.bungee.commands.CMDSimpleChatChannel;
import main.java.de.avankziar.simplechatchannels.bungee.commands.CMDSimpleChatChannelEditor;
import main.java.de.avankziar.simplechatchannels.bungee.database.MysqlInterface;
import main.java.de.avankziar.simplechatchannels.bungee.database.MysqlSetup;
import main.java.de.avankziar.simplechatchannels.bungee.database.YamlHandler;
import main.java.de.avankziar.simplechatchannels.bungee.listener.EVENTJoinLeave;
import main.java.de.avankziar.simplechatchannels.bungee.listener.EVENTTabComplete;
import main.java.de.avankziar.simplechatchannels.bungee.listener.ServerListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class SimpleChatChannels extends Plugin
{
	public static Logger log;
	public static String pluginName = "SimpleChatChannels";
	private static YamlHandler yamlHandler;
	private static MysqlSetup databaseHandler;
	private static MysqlInterface mysqlinterface;
	private static Utility utility;
	private static BackgroundTask backgroundtask;
	private PunisherBungee punisher;
	private AfkRecord afkrecord;
	public ArrayList<String> editorplayers = new ArrayList<>();
	
	public void onEnable() 
	{
		log = getLogger();
		yamlHandler = new YamlHandler(this);
		utility = new Utility(this);
		backgroundtask = new BackgroundTask(this);
		if(yamlHandler.get().getString("mysql.status").equalsIgnoreCase("true"))
		{
			mysqlinterface = new MysqlInterface(this);
			databaseHandler = new MysqlSetup(this);
		} else
		{
			log.severe("MySQL is not enabled! "+pluginName+" wont work correctly!");
		}
		setupPunisher();
		setupAfkRecord();
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);
		//HandlerList.unregisterAll();
		if(yamlHandler.get().getString("mysql.status").equalsIgnoreCase("true"))
		{
			if (databaseHandler.getConnection() != null) 
			{
				//backgroundtask.onShutDownDataSave();
				databaseHandler.closeConnection();
			}
		}
		
		log.info(pluginName + " is disabled!");
	}
	
	public YamlHandler getYamlHandler() 
	{
		return yamlHandler;
	}
	
	public MysqlSetup getDatabaseHandler() 
	{
		return databaseHandler;
	}
	
	public MysqlInterface getMysqlInterface()
	{
		return mysqlinterface;
	}
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundtask;
	}
	
	public void CommandSetup()
	{
		PluginManager pm = getProxy().getPluginManager();
		pm.registerCommand(this, new CMDSimpleChatChannel(this));
		pm.registerCommand(this, new CMDSimpleChatChannelEditor(this));
		pm.registerCommand(this, new CMDClickChat(this));
	}
	
	public void ListenerSetup()
	{
		getProxy().registerChannel("simplechatchannels:sccbungee");
		PluginManager pm = getProxy().getPluginManager();
		pm.registerListener(this, new main.java.de.avankziar.simplechatchannels.bungee.listener.EVENTChat(this));
		pm.registerListener(this, new EVENTJoinLeave(this));
		pm.registerListener(this, new ServerListener(this));
		pm.registerListener(this, new EVENTTabComplete());
	}
	
	private boolean setupPunisher()
    {
        if (getProxy().getPluginManager().getPlugin("Punisher") == null) 
        {
        	punisher = null;
            return false;
        }
        punisher = PunisherBungee.getPlugin();
        return true;
    }
	
	private boolean setupAfkRecord()
	{
		if(getProxy().getPluginManager().getPlugin("AfkRecord")==null)
		{
			punisher = null;
            return false;
		}
		afkrecord = AfkRecord.getPlugin();
		return true;
	}
	
	public PunisherBungee getPunisher()
	{
		return punisher;
	}
	
	public AfkRecord getAfkRecord()
	{
		return afkrecord;
	}
}
