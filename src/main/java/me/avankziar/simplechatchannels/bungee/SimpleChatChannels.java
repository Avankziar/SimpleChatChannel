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
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelAdmin;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelAuction;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelGlobal;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelGroup;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelLocal;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelPerma;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelPrivateMessage;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelSupport;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelTeam;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelTemp;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelTrade;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelWorld;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGGrouplist;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGIgnore;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGIgnoreList;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGMute;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGOptionJoin;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGOptionSpy;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelBan;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelChangePassword;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelChatColor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelCreate;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelDelete;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelInfo;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelInherit;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelInvite;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelJoin;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelKick;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelLeave;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelNameColor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelPlayer;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelRename;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelSymbol;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelUnban;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPermanentChannelVice;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGPlayerlist;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGReload;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelBan;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelChangePassword;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelCreate;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelInfo;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelInvite;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelJoin;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelKick;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelLeave;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGTemporaryChannelUnban;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGUnmute;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGUpdatePlayer;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGWordfilter;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlSetup;
import main.java.me.avankziar.simplechatchannels.bungee.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
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
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(this);
			mysqlSetup = new MysqlSetup(this);
		} else
		{
			log.severe("MySQL is not enabled! "+pluginName+" wont work correctly!");
		}
		backgroundtask = new BackgroundTask(this);
		setupPunisher();
		setupAfkRecord();
		CommandSetup();
		ListenerSetup();
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(this);
		//HandlerList.unregisterAll();
		
		for(PermanentChannel pc : PermanentChannel.getPermanentChannel())
		{
			getMysqlHandler().updateDataIII(pc, pc.getName(), "channel_name");
			getMysqlHandler().updateDataIII(pc, String.join(";", pc.getVice()), "vice");
			getMysqlHandler().updateDataIII(pc, String.join(";", pc.getMembers()), "members");
			getMysqlHandler().updateDataIII(pc, pc.getPassword(), "password");
			getMysqlHandler().updateDataIII(pc, String.join(";", pc.getBanned()), "banned");
		}
		
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
		new ARGChannelAdmin(this);
		new ARGChannelAuction(this);
		new ARGChannelGlobal(this);
		new ARGChannelGroup(this);
		new ARGChannelLocal(this);
		new ARGChannelPerma(this);
		new ARGChannelPrivateMessage(this);
		new ARGChannelSupport(this);
		new ARGChannelTeam(this);
		new ARGChannelTemp(this);
		new ARGChannelTrade(this);
		new ARGChannelWorld(this);	
		new ARGGrouplist(this);
		new ARGIgnore(this);
		new ARGIgnoreList(this);
		new ARGMute(this);
		new ARGOptionJoin(this);
		new ARGOptionSpy(this);
		new ARGPermanentChannelBan(this);
		new ARGPermanentChannelChangePassword(this);
		new ARGPermanentChannelChannels(this);
		new ARGPermanentChannelChatColor(this);
		new ARGPermanentChannelCreate(this);
		new ARGPermanentChannelDelete(this);
		new ARGPermanentChannelInfo(this);
		new ARGPermanentChannelInherit(this);
		new ARGPermanentChannelInvite(this);
		new ARGPermanentChannelJoin(this);
		new ARGPermanentChannelKick(this);
		new ARGPermanentChannelLeave(this);
		new ARGPermanentChannelNameColor(this);
		new ARGPermanentChannelPlayer(this);
		new ARGPermanentChannelRename(this);
		new ARGPermanentChannelSymbol(this);
		new ARGPermanentChannelUnban(this);
		new ARGPermanentChannelVice(this);
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
		new ARGUpdatePlayer(this);
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
		pm.registerListener(this, new EventTabCompletion(this));
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
		//utility.existMethod(afkrecord.getClass(), "isAfk", utility.AFKRECORDISAFK);
		//utility.existMethod(afkrecord.getClass(), "softSave", utility.AFKRECORDSOFTSAVE);
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
	
	public boolean PlayerToChatEditor(String playername)
	{
		if(editorplayers.contains(playername))
		{
			editorplayers.remove(playername);
			return false;
		} else if(!editorplayers.contains(playername))
		{
			editorplayers.add(playername);
			return true;
		}
		return false;
	}
}