package main.java.me.avankziar.simplechatchannels.bungee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import main.java.de.avankziar.afkrecord.bungee.AfkRecord;
import main.java.de.avankziar.punisher.bungee.PunisherBungee;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandExecutorClickChat;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandExecutorSimpleChatChannel;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandExecutorSimpleChatChannelEditor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandHelper;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGBroadcast;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGBungee;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelAdmin;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelAuction;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelCustom;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelGlobal;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelGroup;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelLocal;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelPrivateMessage;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelSupport;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelTeam;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelTrade;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelWorld;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGCustomChannelBan;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGCustomChannelChangePassword;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGCustomChannelCreate;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGCustomChannelInfo;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGCustomChannelJoin;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGCustomChannelKick;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGCustomChannelLeave;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGCustomChannelUnban;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGGrouplist;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGIgnore;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGIgnoreList;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGMute;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGOptionJoin;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGOptionSpy;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPlayerlist;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGReload;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGUnmute;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGWordfilter;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlSetup;
import main.java.me.avankziar.simplechatchannels.bungee.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.listener.EventChat;
import main.java.me.avankziar.simplechatchannels.bungee.listener.EventJoinLeave;
import main.java.me.avankziar.simplechatchannels.bungee.listener.EventTabCompletion;
import main.java.me.avankziar.simplechatchannels.bungee.listener.ServerListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class SimpleChatChannels extends Plugin
{
	public static Logger log;
	public static String pluginName = "SimpleChatChannels";
	private static YamlHandler yamlHandler;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	private static Utility utility;
	private static BackgroundTask backgroundtask;
	private static CommandHelper commandHelper;
	private PunisherBungee punisher;
	private AfkRecord afkrecord;
	
	public ArrayList<String> editorplayers;
	public static HashMap<String, CommandModule> sccarguments;
	
	public void onEnable() 
	{
		log = getLogger();
		editorplayers = new ArrayList<>();
		sccarguments = new HashMap<String, CommandModule>();
		yamlHandler = new YamlHandler(this);
		utility = new Utility(this);
		commandHelper = new CommandHelper(this);
		backgroundtask = new BackgroundTask(this);
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(this);
			mysqlSetup = new MysqlSetup(this);
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
	
	public Utility getUtility()
	{
		return utility;
	}
	
	public BackgroundTask getBackgroundTask()
	{
		return backgroundtask;
	}
	
	public CommandHelper getCommandHelper()
	{
		return commandHelper;
	}
	
	public void CommandSetup()
	{
		PluginManager pm = getProxy().getPluginManager();
		
		//CMD /scc
		new ARGBroadcast(this);
		new ARGBungee(this);
		new ARGChannelAdmin(this);
		new ARGChannelAuction(this);
		new ARGChannelCustom(this);
		new ARGChannelGlobal(this);
		new ARGChannelGroup(this);
		new ARGChannelLocal(this);
		new ARGChannelPrivateMessage(this);
		new ARGChannelSupport(this);
		new ARGChannelTeam(this);
		new ARGChannelTrade(this);
		new ARGChannelWorld(this);
		new ARGCustomChannelBan(this);
		new ARGCustomChannelChangePassword(this);
		new ARGCustomChannelCreate(this);
		new ARGCustomChannelInfo(this);
		new ARGCustomChannelJoin(this);
		new ARGCustomChannelKick(this);
		new ARGCustomChannelLeave(this);
		new ARGCustomChannelUnban(this);
		new ARGGrouplist(this);
		new ARGIgnore(this);
		new ARGIgnoreList(this);
		new ARGMute(this);
		new ARGOptionJoin(this);
		new ARGOptionSpy(this);
		new ARGPlayerlist(this);
		new ARGReload(this);
		new ARGUnmute(this);
		new ARGWordfilter(this);
		pm.registerCommand(this, new CommandExecutorSimpleChatChannel(this));
		
		//CMD /clch
		pm.registerCommand(this, new CommandExecutorClickChat(this));
		
		//CMD /scceditor
		pm.registerCommand(this, new CommandExecutorSimpleChatChannelEditor(this));
	}
	
	public void ListenerSetup()
	{
		getProxy().registerChannel("simplechatchannels:sccbungee");
		PluginManager pm = getProxy().getPluginManager();
		pm.registerListener(this, new EventChat(this));
		pm.registerListener(this, new EventJoinLeave(this));
		pm.registerListener(this, new ServerListener(this));
		pm.registerListener(this, new EventTabCompletion());
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
