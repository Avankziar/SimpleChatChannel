package main.java.de.avankziar.simplechatchannels.spigot;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.de.avankziar.afkrecord.spigot.AfkRecord;
import main.java.de.avankziar.punisher.main.Punisher;
import main.java.de.avankziar.simplechatchannels.spigot.commands.CMDClickChat;
import main.java.de.avankziar.simplechatchannels.spigot.commands.CMDSimpleChatChannels;
import main.java.de.avankziar.simplechatchannels.spigot.commands.TABCompleter;
import main.java.de.avankziar.simplechatchannels.spigot.database.MysqlInterface;
import main.java.de.avankziar.simplechatchannels.spigot.database.MysqlSetup;
import main.java.de.avankziar.simplechatchannels.spigot.database.YamlHandler;
import main.java.de.avankziar.simplechatchannels.spigot.listener.EVENTChat;
import main.java.de.avankziar.simplechatchannels.spigot.listener.EVENTJoinLeave;
import main.java.de.avankziar.simplechatchannels.spigot.listener.ServerListener;

public class SimpleChatChannels extends JavaPlugin
{
	public static Logger log;
	public static String pluginName = "SimpleChatChannels";
	private static YamlHandler yamlHandler;
	private static MysqlSetup databaseHandler;
	private static MysqlInterface mysqlinterface;
	private static BackgroundTask backgroundtask;
	private static Utility utility;
	private Punisher punisher;
	private AfkRecord afkrecord;
	
	public void onEnable()
	{
		log = getLogger();
		yamlHandler = new YamlHandler(this);
		utility = new Utility(this);
		if(yamlHandler.get().getString("mysql.status").equalsIgnoreCase("true"))
		{
			mysqlinterface = new MysqlInterface(this);
			databaseHandler = new MysqlSetup(this);
		} else
		{
			log.severe("MySQL is not set in the Plugin "+pluginName+"!");
			Bukkit.getPluginManager().getPlugin("SimpleChatChannels").getPluginLoader().disablePlugin(this);
			return;
		}
		setupPunisher();
		setupAfkRecord();
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
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
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundtask;
	}
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public void CommandSetup()
	{
		getCommand("scc").setExecutor(new CMDSimpleChatChannels(this));
		getCommand("clickchat").setExecutor(new CMDClickChat(this));
		getCommand("scc").setTabCompleter(new TABCompleter());
	}
	
	public void ListenerSetup()
	{
		PluginManager pm = getServer().getPluginManager();
		getServer().getMessenger().registerIncomingPluginChannel(this, "simplechatchannels:sccbungee", new ServerListener(this));
		getServer().getMessenger().registerOutgoingPluginChannel(this, "simplechatchannels:sccbungee");
		pm.registerEvents(new EVENTChat(this), this);
		pm.registerEvents(new EVENTJoinLeave(this), this);
	}
	
	public Punisher getPunisher()
	{
		return punisher;
	}
	
	private boolean setupPunisher()
	{
		if (getServer().getPluginManager().getPlugin("Punisher") == null) 
        {
        	punisher = null;
            return false;
        }
        punisher = Punisher.getPlugin();
        return true;
	}
	
	private boolean setupAfkRecord()
	{
		if(getServer().getPluginManager().getPlugin("AfkRecord") == null)
		{
			afkrecord = null;
			return false;
		}
		afkrecord = AfkRecord.getPlugin();
		return true;
	}
	
	public AfkRecord getAfkRecord()
	{
		return afkrecord;
	}
}
