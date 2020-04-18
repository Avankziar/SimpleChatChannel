package main.java.me.avankziar.simplechatchannels.spigot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.de.avankziar.afkrecord.spigot.AfkRecord;
import main.java.de.avankziar.punisher.main.Punisher;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandHelper;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.commands.MultipleCommandExecutor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.TABCompletion;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGBroadcast;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGBungee;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelAdmin;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelAuction;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelGlobal;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelGroup;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelLocal;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelPrivateMessage;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelSupport;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelTeam;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelTemp;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelTrade;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelWorld;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGGrouplist;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGIgnore;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGIgnoreList;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGMute;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGOptionJoin;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGOptionSpy;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPlayerlist;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGReload;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelBan;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelChangePassword;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelCreate;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelInfo;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelInvite;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelJoin;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelKick;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelLeave;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGTemporaryChannelUnban;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGUnmute;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGWordfilter;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlSetup;
import main.java.me.avankziar.simplechatchannels.spigot.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.listener.EVENTChat;
import main.java.me.avankziar.simplechatchannels.spigot.listener.EVENTJoinLeave;
import main.java.me.avankziar.simplechatchannels.spigot.listener.ServerListener;

public class SimpleChatChannels extends JavaPlugin
{
	public static Logger log;
	public static String pluginName = "SimpleChatChannels";
	private static YamlHandler yamlHandler;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	private static BackgroundTask backgroundtask;
	private static CommandHelper commandHelper;
	private static Utility utility;
	private Punisher punisher;
	private AfkRecord afkrecord;
	public static ArrayList<String> editorplayers;
	public static HashMap<String, CommandModule> sccarguments;
	
	public void onEnable()
	{
		log = getLogger();
		editorplayers = new ArrayList<>();
		sccarguments = new HashMap<String, CommandModule>();
		yamlHandler = new YamlHandler(this);
		backgroundtask = new BackgroundTask(this);
		commandHelper = new CommandHelper(this);
		utility = new Utility(this);
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(this);
			mysqlSetup = new MysqlSetup(this);
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
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			if (mysqlSetup.getConnection() != null) 
			{
				//backgroundtask.onShutDownDataSave();
				mysqlSetup.closeConnection();
			}
		}
		
		log.info(pluginName + " is disabled!");
	}
	
	public YamlHandler getYamlHandler() 
	{
		return yamlHandler;
	}
	
	public MysqlSetup getMysqlSetup() 
	{
		return mysqlSetup;
	}
	
	public MysqlHandler getMysqlHandler()
	{
		return mysqlHandler;
	}
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundtask;
	}
	
	public CommandHelper getCommandHelper()
	{
		return commandHelper;
	}
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public void CommandSetup()
	{
		new ARGBroadcast(this);
		new ARGBungee(this);
		new ARGChannelAdmin(this);
		new ARGChannelAuction(this);
		new ARGChannelTemp(this);
		new ARGChannelGlobal(this);
		new ARGChannelGroup(this);
		new ARGChannelLocal(this);
		new ARGChannelPrivateMessage(this);
		new ARGChannelSupport(this);
		new ARGChannelTeam(this);
		new ARGChannelTrade(this);
		new ARGChannelWorld(this);
		new ARGGrouplist(this);
		new ARGIgnore(this);
		new ARGIgnoreList(this);
		new ARGMute(this);
		new ARGOptionJoin(this);
		new ARGOptionSpy(this);
		new ARGPlayerlist(this);
		new ARGReload(this);
		new ARGTemporaryChannelBan(this);
		new ARGTemporaryChannelChangePassword(this);
		new ARGTemporaryChannelCreate(this);
		new ARGTemporaryChannelInfo(this);
		new ARGTemporaryChannelInvite(this);
		new ARGTemporaryChannelJoin(this);
		new ARGTemporaryChannelKick(this);
		new ARGTemporaryChannelLeave(this);
		new ARGTemporaryChannelUnban(this);
		new ARGUnmute(this);
		new ARGWordfilter(this);
		getCommand("scc").setExecutor(new MultipleCommandExecutor(this));
		getCommand("clickchat").setExecutor(new MultipleCommandExecutor(this));
		getCommand("scc").setTabCompleter(new TABCompletion());
		getCommand("scceditor").setExecutor(new MultipleCommandExecutor(this));
	}
	
	public boolean reload()
	{
		if(!yamlHandler.loadYamlHandler())
		{
			return false;
		}
		if(!utility.loadUtility())
		{
			return false;
		}
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlSetup.closeConnection();
			if(!mysqlHandler.loadMysqlHandler())
			{
				return false;
			}
			if(!mysqlSetup.loadMysqlSetup())
			{
				return false;
			}
		} else
		{
			return false;
		}
		return true;
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
		//utility.existMethod(afkrecord.getClass(), "isAfk", Utility.AFKRECORDISAFK);
		//utility.existMethod(afkrecord.getClass(), "softSave", Utility.AFKRECORDSOFTSAVE);
		return true;
	}
	
	public AfkRecord getAfkRecord()
	{
		return afkrecord;
	}
}
