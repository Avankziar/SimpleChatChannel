package main.java.me.avankziar.simplechatchannels.spigot;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.de.avankziar.afkrecord.spigot.AfkRecord;
import main.java.de.avankziar.afkrecord.spigot.command.TABCompleter;
import main.java.de.avankziar.punisher.main.Punisher;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelPerma;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.ConvertHandler;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.ClickChatCommandExecutor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandHelper;
import main.java.me.avankziar.simplechatchannels.spigot.commands.MessageCommandExecutor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.SccCommandExecutor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.SccEditorCommandExecutor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGBroadcast;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelAdmin;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelAuction;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGChannelEvent;
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
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelBan;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelChangePassword;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelChatColor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelCreate;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelDelete;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelInfo;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelInherit;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelInvite;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelJoin;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelKick;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelLeave;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelNameColor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelPlayer;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelRename;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelSymbol;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelUnban;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGPermanentChannelVice;
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
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGUpdatePlayer;
import main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels.ARGWordfilter;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.BaseConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.CommandConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlSetup;
import main.java.me.avankziar.simplechatchannels.spigot.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.listener.EVENTChat;
import main.java.me.avankziar.simplechatchannels.spigot.listener.EVENTJoinLeave;
import main.java.me.avankziar.simplechatchannels.spigot.listener.ServerListener;
import main.java.me.avankziar.simplechatchannels.spigot.objects.TemporaryChannel;

public class SimpleChatChannels extends JavaPlugin
{
	private static SimpleChatChannels plugin;
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
	private ArrayList<String> players;
	
	private ArrayList<CommandConstructor> commandTree;
	private ArrayList<BaseConstructor> helpList;
	private LinkedHashMap<String, ArgumentModule> argumentMap;
	public static String baseCommandI = "scc"; //Pfad angabe + ürspungliches Commandname
	public static String baseCommandII = "clch";
	public static String baseCommandIII = "scceditor";
	public static String baseCommandIV = "msg";
	
	public static String baseCommandIName = ""; //CustomCommand name
	public static String baseCommandIIName = "";
	public static String baseCommandIIIName = "";
	public static String baseCommandIVName = "";
	
	public static String infoCommandPath = "CmdScc";
	public static String infoCommand = "/"; //InfoComamnd
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		
		editorplayers = new ArrayList<>();
		commandTree = new ArrayList<>();
		argumentMap = new LinkedHashMap<>();
		helpList = new ArrayList<>();
		
		yamlHandler = new YamlHandler(this);
		utility = new Utility(this);
		commandHelper = new CommandHelper(this);
		
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(this);
		} else
		{
			log.severe("MySQL is not set in the Plugin "+pluginName+"!");
			Bukkit.getPluginManager().getPlugin("SimpleChatChannels").getPluginLoader().disablePlugin(this);
			return;
		}
		setupPlayers();
		backgroundtask = new BackgroundTask(this);
		setupPunisher();
		setupAfkRecord();
		setupStrings();
		setupCommandTree();
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
	
	public static SimpleChatChannels getPlugin()
	{
		return plugin;
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
	
	private void setupStrings()
	{
		//Hier baseCommands
		baseCommandIName = plugin.getYamlHandler().getCom().getString(baseCommandI+".Name");
		baseCommandIIName = plugin.getYamlHandler().getCom().getString(baseCommandII+".Name");
		baseCommandIIIName = plugin.getYamlHandler().getCom().getString(baseCommandIII+".Name");
		baseCommandIVName = plugin.getYamlHandler().getCom().getString(baseCommandIV+".Name");
		
		//Zuletzt infoCommand deklarieren
		infoCommand += baseCommandIName;
	}
	
	private void setupCommandTree()
	{
		LinkedHashMap<Integer, ArrayList<String>> pcPlayerMap = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> pcMap = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> tcMap = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> playerMap = new LinkedHashMap<>();
		ArrayList<String> pcarray = new ArrayList<>();
		for(PermanentChannel pc : PermanentChannel.getPermanentChannel())
		{
			pcarray.add(pc.getName());
		}
		Collections.sort(pcarray);
		pcPlayerMap.put(1, pcarray);
		pcMap.put(1, pcarray);
		ArrayList<String> playerarray = getPlayers();
		Collections.sort(playerarray);
		pcPlayerMap.put(2, playerarray);
		playerMap.put(1, playerarray);
		ArrayList<String> tcarray = new ArrayList<>();
		for(TemporaryChannel tc : TemporaryChannel.getCustomChannel())
		{
			tcarray.add(tc.getName());
		}
		Collections.sort(tcarray);
		tcMap.put(1, tcarray);
		
		ArgumentConstructor broadcast = new ArgumentConstructor(yamlHandler, baseCommandI+"_broadcast", 0, 1, 1024, null);
		
		ArgumentConstructor admin = new ArgumentConstructor(yamlHandler, baseCommandI+"_admin", 0, 0, 0, null);
		ArgumentConstructor auction = new ArgumentConstructor(yamlHandler, baseCommandI+"_auction", 0, 0, 0, null);
		ArgumentConstructor event = new ArgumentConstructor(yamlHandler, baseCommandI+"_event", 0, 0, 0, null);
		ArgumentConstructor global = new ArgumentConstructor(yamlHandler, baseCommandI+"_global", 0, 0, 0, null);
		ArgumentConstructor group = new ArgumentConstructor(yamlHandler, baseCommandI+"_group", 0, 0, 0, null);
		ArgumentConstructor local = new ArgumentConstructor(yamlHandler, baseCommandI+"_local", 0, 0, 0, null);
		ArgumentConstructor perma = new ArgumentConstructor(yamlHandler, baseCommandI+"_perma", 0, 0, 0, null);
		ArgumentConstructor pn = new ArgumentConstructor(yamlHandler, baseCommandI+"_pm", 0, 0, 0, null);
		ArgumentConstructor support = new ArgumentConstructor(yamlHandler, baseCommandI+"_support", 0, 0, 0, null);
		ArgumentConstructor team = new ArgumentConstructor(yamlHandler, baseCommandI+"_team", 0, 0, 0, null);
		ArgumentConstructor temp = new ArgumentConstructor(yamlHandler, baseCommandI+"_temp", 0, 0, 0, null);
		ArgumentConstructor trade = new ArgumentConstructor(yamlHandler, baseCommandI+"_trade", 0, 0, 0, null);
		ArgumentConstructor world = new ArgumentConstructor(yamlHandler, baseCommandI+"_world", 0, 0, 0, null);
		
		ArgumentConstructor grouplist = new ArgumentConstructor(yamlHandler, baseCommandI+"_grouplist", 0, 0, 1, null);
		
		ArgumentConstructor ignore = new ArgumentConstructor(yamlHandler, baseCommandI+"_ignore", 0, 1, 1, playerMap);
		ArgumentConstructor ignorelist = new ArgumentConstructor(yamlHandler, baseCommandI+"_ignorelist", 0, 0, 0, null);
		
		ArgumentConstructor mute = new ArgumentConstructor(yamlHandler, baseCommandI+"_mute", 0, 1, 2, null);
		
		ArgumentConstructor join = new ArgumentConstructor(yamlHandler, baseCommandI+"_join", 0, 0, 0, null);
		ArgumentConstructor spy = new ArgumentConstructor(yamlHandler, baseCommandI+"_spy", 0, 0, 0, null);
		
		ArgumentConstructor pcban = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcban", 0, 2, 2, pcPlayerMap);
		ArgumentConstructor pcchangepassword = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcchangepassword", 0, 2, 2, pcMap);
		ArgumentConstructor pcchannels = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcchannels", 0, 0, 0, null);
		ArgumentConstructor pcchatcolor = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcchatcolor", 0, 2, 2, pcMap);
		ArgumentConstructor pccreate = new ArgumentConstructor(yamlHandler, baseCommandI+"_pccreate", 0, 1, 2, null);
		ArgumentConstructor pcdelete = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcdelete", 0, 1, 2, pcMap);
		ArgumentConstructor pcinfo = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcinfo", 0, 0, 1, pcMap);
		ArgumentConstructor pcinherit = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcinherit", 0, 2, 2, pcPlayerMap);
		ArgumentConstructor pcinvite = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcinvite", 0, 2, 2, pcPlayerMap);
		ArgumentConstructor pcjoin = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcjoin", 0, 1, 2, pcMap);
		ArgumentConstructor pckick = new ArgumentConstructor(yamlHandler, baseCommandI+"_pckick", 0, 2, 2, pcPlayerMap);
		ArgumentConstructor pcleave = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcleave", 0, 1, 2, pcMap);
		ArgumentConstructor pcnamecolor = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcnamecolor", 0, 2, 2, pcMap);
		ArgumentConstructor pcplayer = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcplayer", 0, 0, 1, playerMap);
		ArgumentConstructor pcrename = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcrename", 0, 2, 2, pcMap);
		ArgumentConstructor pcsymbol = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcsymbol", 0, 2, 2, pcMap);
		ArgumentConstructor pcunban = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcunban", 0, 2, 2, pcPlayerMap);
		ArgumentConstructor pcvice = new ArgumentConstructor(yamlHandler, baseCommandI+"_pcvice", 0, 2, 2, pcPlayerMap);
		
		ArgumentConstructor playerlist = new ArgumentConstructor(yamlHandler, baseCommandI+"_playerlist", 0, 0, 1, null);
		ArgumentConstructor reload = new ArgumentConstructor(yamlHandler, baseCommandI+"_reload", 0, 0, 0, null);
		
		ArgumentConstructor tcban = new ArgumentConstructor(yamlHandler, baseCommandI+"_tcban", 0, 1, 1, playerMap);
		ArgumentConstructor tcchangepassword = new ArgumentConstructor(yamlHandler, baseCommandI+"_tcchangepassword", 0, 1, 1, null);
		ArgumentConstructor tccreate = new ArgumentConstructor(yamlHandler, baseCommandI+"_tccreate", 0, 1, 2, null);
		ArgumentConstructor tcinfo = new ArgumentConstructor(yamlHandler, baseCommandI+"_tcinfo", 0, 1, 1, null);
		ArgumentConstructor tcinvite = new ArgumentConstructor(yamlHandler, baseCommandI+"_tcinvite", 0, 1, 1, playerMap);
		ArgumentConstructor tcjoin = new ArgumentConstructor(yamlHandler, baseCommandI+"_tcjoin", 0, 1, 2, tcMap);
		ArgumentConstructor tckick = new ArgumentConstructor(yamlHandler, baseCommandI+"_tckick", 0, 1, 1, playerMap);
		ArgumentConstructor tcleave = new ArgumentConstructor(yamlHandler, baseCommandI+"_tcleave", 0, 0, 0, null);
		ArgumentConstructor tcunban = new ArgumentConstructor(yamlHandler, baseCommandI+"_tcunban", 0, 1, 1, playerMap);
		
		ArgumentConstructor unmute = new ArgumentConstructor(yamlHandler, baseCommandI+"_unmute", 0, 1, 1, null);
		ArgumentConstructor updateplayer = new ArgumentConstructor(yamlHandler, baseCommandI+"_updateplayer", 0, 1, 1, playerMap);
		ArgumentConstructor wordfilter = new ArgumentConstructor(yamlHandler, baseCommandI+"_wordfilter", 0, 1, 1, null);
		
		CommandConstructor scc = new CommandConstructor(plugin, baseCommandI,
				broadcast,
				admin, auction, event, global, group, local, perma, pn, support, team, temp, trade, world,
				grouplist, ignore, ignorelist, mute, join, spy,
				pcban, pcchangepassword, pcchannels, pcchatcolor, pccreate, pcdelete, pcinfo, pcinherit, pcinvite,
				pcjoin, pckick, pcleave, pcnamecolor, pcplayer, pcrename, pcsymbol, pcunban, pcvice,
				playerlist, reload,
				tcban, tcchangepassword, tccreate, tcinfo, tcinvite, tcjoin, tckick, tcleave, tcunban,
				unmute, updateplayer, wordfilter);
		
		CommandConstructor clch = new CommandConstructor(plugin, baseCommandII); 
		
		CommandConstructor scceditor = new CommandConstructor(plugin, baseCommandIII); 
		
		CommandConstructor msg = new CommandConstructor(plugin, baseCommandIV);
		
		registerCommand(scc.getPath(), scc.getName());
		getCommand(scc.getName()).setExecutor(new SccCommandExecutor(plugin, scc));
		getCommand(scc.getName()).setTabCompleter(new TABCompleter());
		
		registerCommand(scceditor.getPath(), scceditor.getName());
		getCommand(scceditor.getName()).setExecutor(new SccEditorCommandExecutor(plugin, scceditor));
		
		registerCommand(clch.getPath(), clch.getName());
		getCommand(clch.getName()).setExecutor(new ClickChatCommandExecutor(plugin, clch));
		
		registerCommand(msg.getPath(), msg.getName());
		getCommand(
				msg.getName())
		.setExecutor(new MessageCommandExecutor(
				plugin,
				msg));
		getCommand(msg.getName()).setTabCompleter(new TABCompleter());
		
		addingHelps(scc, broadcast,
				admin, auction, event, global, group, local, perma, pn, support, team, temp, trade, world,
				grouplist, ignore, ignorelist, mute, join, spy,
				pcban, pcchangepassword, pcchannels, pcchatcolor, pccreate, pcdelete, pcinfo, pcinherit, pcinvite,
				pcjoin, pckick, pcleave, pcnamecolor, pcplayer, pcrename, pcsymbol, pcunban, pcvice,
				playerlist, reload,
				tcban, tcchangepassword, tccreate, tcinfo, tcinvite, tcjoin, tckick, tcleave, tcunban,
				unmute, updateplayer, wordfilter,
				clch, scceditor);
		
		new ARGBroadcast(plugin, broadcast);
		
		new ARGChannelAdmin(plugin, admin);
		new ARGChannelAuction(plugin, auction);
		new ARGChannelEvent(plugin, event);
		new ARGChannelGlobal(plugin, global);
		new ARGChannelGroup(plugin, group);
		new ARGChannelLocal(plugin, local);
		new ARGChannelPerma(plugin, perma);
		new ARGChannelPrivateMessage(plugin, pn);
		new ARGChannelSupport(plugin, support);
		new ARGChannelTeam(plugin, team);
		new ARGChannelTemp(plugin, temp);
		new ARGChannelTrade(plugin, trade);
		new ARGChannelWorld(plugin, world);
		
		new ARGGrouplist(plugin, grouplist);
		new ARGIgnore(plugin, ignore);
		new ARGIgnoreList(plugin, ignorelist);
		
		new ARGMute(plugin, mute);
		new ARGOptionJoin(plugin, join);
		new ARGOptionSpy(plugin, spy);
		
		new ARGPermanentChannelBan(plugin, pcban);
		new ARGPermanentChannelChangePassword(plugin, pcchangepassword);
		new ARGPermanentChannelChannels(plugin, pcchannels);
		new ARGPermanentChannelChatColor(plugin, pcchatcolor);
		new ARGPermanentChannelCreate(plugin, pccreate);
		new ARGPermanentChannelDelete(plugin, pcdelete);
		new ARGPermanentChannelInfo(plugin, pcinfo);
		new ARGPermanentChannelInherit(plugin, pcinherit);
		new ARGPermanentChannelInvite(plugin, pcinvite);
		new ARGPermanentChannelJoin(plugin, pcjoin);
		new ARGPermanentChannelKick(plugin, pckick);
		new ARGPermanentChannelLeave(plugin, pcleave);
		new ARGPermanentChannelNameColor(plugin, pcnamecolor);
		new ARGPermanentChannelPlayer(plugin, pcplayer);
		new ARGPermanentChannelRename(plugin, pcrename);
		new ARGPermanentChannelSymbol(plugin, pcsymbol);
		new ARGPermanentChannelUnban(plugin, pcunban);
		new ARGPermanentChannelVice(plugin, pcvice);
		
		new ARGPlayerlist(plugin, playerlist);
		new ARGReload(plugin, reload);
		
		new ARGTemporaryChannelBan(plugin, tcban);
		new ARGTemporaryChannelChangePassword(plugin, tcchangepassword);
		new ARGTemporaryChannelCreate(plugin, tccreate);
		new ARGTemporaryChannelInfo(plugin, tcinfo);
		new ARGTemporaryChannelInvite(plugin, tcinvite);
		new ARGTemporaryChannelJoin(plugin, tcjoin);
		new ARGTemporaryChannelKick(plugin, tckick);
		new ARGTemporaryChannelLeave(plugin, tcleave);
		new ARGTemporaryChannelUnban(plugin, tcunban);
		
		new ARGUpdatePlayer(plugin, updateplayer);
		new ARGUnmute(plugin, unmute);
		new ARGWordfilter(plugin, wordfilter);
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
	
	public ArrayList<BaseConstructor> getHelpList()
	{
		return helpList;
	}
	
	public void addingHelps(BaseConstructor... objects)
	{
		for(BaseConstructor bc : objects)
		{
			helpList.add(bc);
		}
	}
	
	public ArrayList<CommandConstructor> getCommandTree()
	{
		return commandTree;
	}
	
	public CommandConstructor getCommandFromPath(String commandpath)
	{
		CommandConstructor cc = null;
		for(CommandConstructor coco : getCommandTree())
		{
			if(coco.getPath().equalsIgnoreCase(commandpath))
			{
				cc = coco;
				break;
			}
		}
		return cc;
	}
	
	public CommandConstructor getCommandFromCommandString(String command)
	{
		CommandConstructor cc = null;
		for(CommandConstructor coco : getCommandTree())
		{
			if(coco.getName().equalsIgnoreCase(command))
			{
				cc = coco;
				break;
			}
		}
		return cc;
	}

	public LinkedHashMap<String, ArgumentModule> getArgumentMap()
	{
		return argumentMap;
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
	
	public ArrayList<String> getPlayers()
	{
		return players;
	}

	public void setPlayers(ArrayList<String> players)
	{
		this.players = players;
	}
	
	public void setupPlayers()
	{
		ArrayList<ChatUser> cu = ConvertHandler.convertListI(
				plugin.getMysqlHandler().getTop(MysqlHandler.Type.CHATUSER,
						"`id`", true, 0,
						plugin.getMysqlHandler().lastID(MysqlHandler.Type.CHATUSER)));
		ArrayList<String> cus = new ArrayList<>();
		for(ChatUser chus : cu) 
		{
			cus.add(chus.getName());	
		}
		setPlayers(cus);
	}
	
	public void registerCommand(String... aliases) 
	{
		PluginCommand command = getCommand(aliases[0], plugin);
	 
		command.setAliases(Arrays.asList(aliases));
		getCommandMap().register(plugin.getDescription().getName(), command);
	}
	 
	private static PluginCommand getCommand(String name, SimpleChatChannels plugin) 
	{
		PluginCommand command = null;
	 
		try {
			Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
			c.setAccessible(true);
	 
			command = c.newInstance(name, plugin);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	 
		return command;
	}
	 
	private static CommandMap getCommandMap() 
	{
		CommandMap commandMap = null;
	 
		try {
			if (Bukkit.getPluginManager() instanceof SimplePluginManager) 
			{
				Field f = SimplePluginManager.class.getDeclaredField("commandMap");
				f.setAccessible(true);
	 
				commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	 
		return commandMap;
	}
}
