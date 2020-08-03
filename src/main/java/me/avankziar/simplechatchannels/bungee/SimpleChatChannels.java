package main.java.me.avankziar.simplechatchannels.bungee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import main.java.de.avankziar.afkrecord.bungee.AfkRecord;
import main.java.de.avankziar.punisher.bungee.PunisherBungee;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.BackgroundTask;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.ClickChatCommandExecutor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandHelper;
import main.java.me.avankziar.simplechatchannels.bungee.commands.MessageCommandExecutor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.SccCommandExecutor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.SccEditorCommandExecutor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.TabCompletionListener;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGBroadcast;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelAdmin;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelAuction;
import main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels.ARGChannelEvent;
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
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.BaseConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlSetup;
import main.java.me.avankziar.simplechatchannels.bungee.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.listener.EventChat;
import main.java.me.avankziar.simplechatchannels.bungee.listener.EventJoinLeave;
import main.java.me.avankziar.simplechatchannels.bungee.listener.ServerListener;
import main.java.me.avankziar.simplechatchannels.bungee.metrics.Metrics;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.ConvertHandler;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class SimpleChatChannels extends Plugin
{
	public static SimpleChatChannels plugin;
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
		
		yamlHandler = new YamlHandler(plugin);
		utility = new Utility(plugin);
		commandHelper = new CommandHelper(plugin);
		
		if(yamlHandler.get().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(plugin);
		} else
		{
			log.severe("MySQL is not enabled! "+pluginName+" wont work correctly!");
		}
		setupPlayers();
		backgroundtask = new BackgroundTask(plugin);
		setupPunisher();
		setupAfkRecord();
		setupStrings();
		setupCommandTree();
		ListenerSetup();
		setupBstats();
	}
	
	public void onDisable()
	{
		getProxy().getScheduler().cancel(plugin);
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
		
		PluginManager pm = getProxy().getPluginManager();
		
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
		
		pm.registerCommand(plugin, new SccCommandExecutor(plugin, scc));
		pm.registerCommand(plugin, new ClickChatCommandExecutor(plugin, clch));
		pm.registerCommand(plugin, new SccEditorCommandExecutor(plugin, scceditor));
		pm.registerCommand(plugin, new MessageCommandExecutor(plugin, msg));
		
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
	
	public void ListenerSetup()
	{
		getProxy().registerChannel("simplechatchannels:sccbungee");
		PluginManager pm = getProxy().getPluginManager();
		pm.registerListener(plugin, new EventChat(plugin));
		pm.registerListener(plugin, new EventJoinLeave(plugin));
		pm.registerListener(plugin, new ServerListener(plugin));
		pm.registerListener(plugin, new TabCompletionListener(plugin));
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
	
	public boolean PlayerToChatEditor(String playername)
	{
		if(editorplayers.contains(playername))
		{
			editorplayers.remove(playername);
			String message = "editor"+"µ"+playername+"µ"+"remove";
			utility.sendSpigotMessage("simplechatchannels:sccbungee", message);
			return false;
		} else if(!editorplayers.contains(playername))
		{
			editorplayers.add(playername);
			String message = "editor"+"µ"+playername+"µ"+"add";
			utility.sendSpigotMessage("simplechatchannels:sccbungee", message);
			return true;
		}
		return false;
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
	
	public void setupBstats()
	{
		int pluginId = 8403;
        new Metrics(this, pluginId);
	}
}