package main.java.me.avankziar.scc.spigot;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.me.avankziar.scc.database.YamlManager;
import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.ChatTitle;
import main.java.me.avankziar.scc.spigot.assistance.BackgroundTask;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.ClickChatCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.MailCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.MessageCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.RCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.ReCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.SccCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.SccEditorCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.TABCompletion;
import main.java.me.avankziar.scc.spigot.commands.WCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.mail.ARGForward;
import main.java.me.avankziar.scc.spigot.commands.mail.ARGLastReceivedMails;
import main.java.me.avankziar.scc.spigot.commands.mail.ARGLastSendedMails;
import main.java.me.avankziar.scc.spigot.commands.mail.ARGRead;
import main.java.me.avankziar.scc.spigot.commands.mail.ARGSend;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGBook;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGBroadcast;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGChannel;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGChannelGui;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGIgnore;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGIgnoreList;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGItem;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGItem_Rename;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGItem_Replacers;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGMute;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGOption;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGOption_Channel;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGOption_Join;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGOption_Spy;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGPerformance;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGUnmute;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Ban;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_ChangePassword;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Channels;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_ChatColor;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Create;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Delete;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Info;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Inherit;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Invite;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Join;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Kick;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Leave;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_NameColor;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Player;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Rename;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Symbol;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Unban;
import main.java.me.avankziar.scc.spigot.commands.scc.pc.ARGPermanentChannel_Vice;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_Ban;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_ChangePassword;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_Create;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_Info;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_Invite;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_Join;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_Kick;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_Leave;
import main.java.me.avankziar.scc.spigot.commands.scc.tc.ARGTemporaryChannel_Unban;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.commands.tree.BaseConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;
import main.java.me.avankziar.scc.spigot.database.MysqlSetup;
import main.java.me.avankziar.scc.spigot.database.YamlHandler;
import main.java.me.avankziar.scc.spigot.guihandling.GuiListener;
import main.java.me.avankziar.scc.spigot.guihandling.GuiPreListener;
import main.java.me.avankziar.scc.spigot.handler.ChatHandler;
import main.java.me.avankziar.scc.spigot.ifh.BaseComponentToBungeeAPI;
import main.java.me.avankziar.scc.spigot.ifh.MessageToBungeeAPI;
import main.java.me.avankziar.scc.spigot.listener.ChatListener;
import main.java.me.avankziar.scc.spigot.listener.JoinLeaveListener;
import main.java.me.avankziar.scc.spigot.listener.LocationUpdateListener;
import main.java.me.avankziar.scc.spigot.listener.ServerListener;
import main.java.me.avankziar.scc.spigot.metrics.Metrics;
import main.java.me.avankziar.scc.spigot.objects.BypassPermission;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;

public class SimpleChatChannels extends JavaPlugin
{
	private static SimpleChatChannels plugin;
	public static Logger log;
	public static String pluginName = "SimpleChatChannels";
	private static YamlHandler yamlHandler;
	private static YamlManager yamlManager;
	private static MysqlSetup mysqlSetup;
	private static MysqlHandler mysqlHandler;
	private static BackgroundTask backgroundtask;
	private static Utility utility;
	
	private static MessageToBungeeAPI mtb;
	private static BaseComponentToBungeeAPI bctb;
	
	public ArrayList<String> editorplayers = new ArrayList<>();
	private ArrayList<String> players = new ArrayList<>();
	public static ArrayList<String> onlinePlayers = new ArrayList<>();
	public static LinkedHashMap<String, ArrayList<String>> rePlayers = new LinkedHashMap<>();
	public static LinkedHashMap<String, String> rPlayers = new LinkedHashMap<>();
	
	private ArrayList<CommandConstructor> commandTree = new ArrayList<>();
	private ArrayList<BaseConstructor> helpList = new ArrayList<>();
	private LinkedHashMap<String, ArgumentModule> argumentMap = new LinkedHashMap<>();
	
	public static String baseCommandI = "scc"; //Pfad angabe + ürspungliches Commandname
	public static String baseCommandII = "clch";
	public static String baseCommandIII = "scceditor";
	public static String baseCommandIV = "msg";
	public static String baseCommandV = "re";
	public static String baseCommandVI = "r";
	public static String baseCommandVII = "w";
	public static String baseCommandVIII = "mail";
	
	public static String baseCommandIName = ""; //CustomCommand name
	public static String baseCommandIIName = "";
	public static String baseCommandIIIName = "";
	public static String baseCommandIVName = "";
	public static String baseCommandVName = "";
	public static String baseCommandVIName = "";
	public static String baseCommandVIIName = "";
	public static String baseCommandVIIIName = "";
	
	public static String infoCommandPath = "CmdScc";
	public static String infoCommand = "/"; //InfoComamnd
	
	public static ArrayList<ChatTitle> chatTitlesPrefix = new ArrayList<>();
	public static ArrayList<ChatTitle> chatTitlesSuffix = new ArrayList<>();
	/**
	 * The Channel without entrysymbol.
	 */
	public static Channel nullChannel = null;
	/**
	 * The other channels, which has all entrysymbols.
	 */
	public static LinkedHashMap<String, Channel> channels = new LinkedHashMap<>();
	
	public void onEnable()
	{
		plugin = this;
		log = getLogger();
		
		//https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=SCC
		log.info(" ███████╗ ██████╗ ██████╗ | API-Version: "+plugin.getDescription().getAPIVersion());
		log.info(" ██╔════╝██╔════╝██╔════╝ | Author: "+plugin.getDescription().getAuthors().toString());
		log.info(" ███████╗██║     ██║      | Plugin Website: "+plugin.getDescription().getWebsite());
		log.info(" ╚════██║██║     ██║      | Depend Plugins: "+plugin.getDescription().getDepend().toString());
		log.info(" ███████║╚██████╗╚██████╗ | SoftDepend Plugins: "+plugin.getDescription().getSoftDepend().toString());
		log.info(" ╚══════╝ ╚═════╝ ╚═════╝ | LoadBefore: "+plugin.getDescription().getLoadBefore().toString());
		
		yamlHandler = new YamlHandler(this);
		utility = new Utility(plugin);
		
		if(yamlHandler.getConfig().getBoolean("Mysql.Status", false))
		{
			mysqlHandler = new MysqlHandler(plugin);
			mysqlSetup = new MysqlSetup(this);
		} else
		{
			log.severe("MySQL is not set in the Plugin "+pluginName+"!");
			Bukkit.getPluginManager().getPlugin("SimpleChatChannels").getPluginLoader().disablePlugin(this);
			return;
		}
		PluginSettings.initSettings(plugin);
		ChatHandler.initPrivateChatColors();
		setupPlayers();
		setupChannels();
		setupChatTitles();
		setupEmojis();
		backgroundtask = new BackgroundTask(this);
		setupStrings();
		setupCommandTree();
		ListenerSetup();		
		BypassPermission.init(plugin);
		setupBstats();
		setupIFH();
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		if(yamlHandler.getConfig().getBoolean("Mysql.Status", false))
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
	
	public YamlManager getYamlManager()
	{
		return yamlManager;
	}
	
	public void setYamlManager(YamlManager yamlManager)
	{
		SimpleChatChannels.yamlManager = yamlManager;
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
	
	public Utility getUtility()
	{
		return utility;
	}
	
	private void setupStrings()
	{
		//Hier baseCommands
		baseCommandIName = plugin.getYamlHandler().getCommands().getString(baseCommandI+".Name");
		baseCommandIIName = plugin.getYamlHandler().getCommands().getString(baseCommandII+".Name");
		baseCommandIIIName = plugin.getYamlHandler().getCommands().getString(baseCommandIII+".Name");
		baseCommandIVName = plugin.getYamlHandler().getCommands().getString(baseCommandIV+".Name");
		baseCommandVName = plugin.getYamlHandler().getCommands().getString(baseCommandV+".Name");
		baseCommandVIName = plugin.getYamlHandler().getCommands().getString(baseCommandVI+".Name");
		baseCommandVIIName = plugin.getYamlHandler().getCommands().getString(baseCommandVII+".Name");
		
		//Zuletzt infoCommand deklarieren
		infoCommand += baseCommandIName;
	}
	
	private void setupCommandTree()
	{
		LinkedHashMap<Integer, ArrayList<String>> pcPlayerMap = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> pcMap = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> tcMap = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> playerMap = new LinkedHashMap<>();
		LinkedHashMap<Integer, ArrayList<String>> channelMap = new LinkedHashMap<>();
		
		ArrayList<String> channelarray = new ArrayList<>();
		for(Channel channel : channels.values())
		{
			channelarray.add(channel.getUniqueIdentifierName());
		}
		channelMap.put(1, channelarray);
		
		ArrayList<String> pcarray = new ArrayList<>();
		for(PermanentChannel pc : PermanentChannel.getPermanentChannel())
		{
			pcarray.add(pc.getName());
		}
		Collections.sort(pcarray);
		pcPlayerMap.put(1, pcarray);
		pcMap.put(2, pcarray);
		ArrayList<String> playerarray = getPlayers();
		Collections.sort(playerarray);
		pcPlayerMap.put(3, playerarray);
		playerMap.put(2, playerarray);
		ArrayList<String> tcarray = new ArrayList<>();
		for(TemporaryChannel tc : TemporaryChannel.getCustomChannel())
		{
			tcarray.add(tc.getName());
		}
		Collections.sort(tcarray);
		tcMap.put(1, tcarray);
		
		//INFO:SCC
		ArgumentConstructor book = new ArgumentConstructor(baseCommandI+"_book", 0, 1, 2, false, null);
		PluginSettings.settings.addCommands(KeyHandler.SCC_BOOK, book.getCommandString());
		ArgumentConstructor broadcast = new ArgumentConstructor(baseCommandI+"_broadcast", 0, 1, 9999, true, null);
		ArgumentConstructor channel = new ArgumentConstructor(baseCommandI+"_channel", 0, 1, 1, false, channelMap);
		ArgumentConstructor channelgui = new ArgumentConstructor(baseCommandI+"_channelgui", 0, 0, 0, false, null);

		ArgumentConstructor ignore = new ArgumentConstructor(baseCommandI+"_ignore", 0, 1, 1, false, playerMap);
		PluginSettings.settings.addCommands(KeyHandler.SCC_IGNORE, ignore.getCommandString());
		ArgumentConstructor ignorelist = new ArgumentConstructor(baseCommandI+"_ignorelist", 0, 0, 1, false,  null);
		
		ArgumentConstructor item_rename = new ArgumentConstructor(baseCommandI+"_item_rename", 1, 3, 3, false, null);
		ArgumentConstructor item_replacers = new ArgumentConstructor(baseCommandI+"_item_replacers", 1, 1, 1, false, null);
		ArgumentConstructor item = new ArgumentConstructor(baseCommandI+"_item", 0, 0, 0, false, null, 
				item_rename, item_replacers);
		
		ArgumentConstructor mute = new ArgumentConstructor(baseCommandI+"_mute", 0, 1, 999, false, null);
		ArgumentConstructor unmute = new ArgumentConstructor(baseCommandI+"_unmute", 0, 1, 1, false, null);
		
		ArgumentConstructor performance = new ArgumentConstructor(baseCommandI+"_performance", 0, 0, 0, true, null);
		
		ArgumentConstructor option_channel = new ArgumentConstructor(baseCommandI+"_option_channel", 1, 1, 1, false, null);
		ArgumentConstructor option_join = new ArgumentConstructor(baseCommandI+"_option_join", 1, 1, 1, false, null);
		ArgumentConstructor option_spy = new ArgumentConstructor(baseCommandI+"_option_spy", 1, 1, 1, false, null);
		ArgumentConstructor option = new ArgumentConstructor(baseCommandI+"_option", 0, 0, 0, false, null,
				option_channel, option_join, option_spy);
		
		ArgumentConstructor updateplayer = new ArgumentConstructor(baseCommandI+"_updateplayer", 0, 1, 1, false, playerMap);
		
		ArgumentConstructor pc_ban = new ArgumentConstructor(baseCommandI+"_pc_ban", 1, 3, 3, false, pcPlayerMap);
		ArgumentConstructor pc_changepassword = new ArgumentConstructor(baseCommandI+"_pc_changepassword", 1, 3, 3, false, pcMap);
		ArgumentConstructor pc_channels = new ArgumentConstructor(baseCommandI+"_pc_channels", 1, 1, 1, false, null);
		ArgumentConstructor pc_chatcolor = new ArgumentConstructor(baseCommandI+"_pc_chatcolor", 1, 3, 3, false, pcMap);
		ArgumentConstructor pc_create = new ArgumentConstructor(baseCommandI+"_pc_create", 1, 2, 3, false, null);
		ArgumentConstructor pc_delete = new ArgumentConstructor(baseCommandI+"_pc_delete", 1, 2, 3, false, pcMap);
		PluginSettings.settings.addCommands(KeyHandler.SCC_PC_DELETE, pc_delete.getCommandString());
		ArgumentConstructor pc_info = new ArgumentConstructor(baseCommandI+"_pc_info", 1, 1, 2, false, pcMap);
		PluginSettings.settings.addCommands(KeyHandler.SCC_PC_INFO, pc_info.getCommandString());
		ArgumentConstructor pc_inherit = new ArgumentConstructor(baseCommandI+"_pc_inherit", 1, 3, 3, false, pcPlayerMap);
		ArgumentConstructor pc_invite = new ArgumentConstructor(baseCommandI+"_pc_invite", 1, 3, 3, false, pcPlayerMap);
		ArgumentConstructor pc_join = new ArgumentConstructor(baseCommandI+"_pc_join", 1, 2, 3, false, pcMap);
		PluginSettings.settings.addCommands(KeyHandler.SCC_PC_JOIN, pc_join.getCommandString());
		ArgumentConstructor pc_kick = new ArgumentConstructor(baseCommandI+"_pc_kick", 1, 3, 3, false, pcPlayerMap);
		ArgumentConstructor pc_leave = new ArgumentConstructor(baseCommandI+"_pc_leave", 1, 2, 3, false, pcMap);
		PluginSettings.settings.addCommands(KeyHandler.SCC_PC_LEAVE, pc_leave.getCommandString());
		ArgumentConstructor pc_namecolor = new ArgumentConstructor(baseCommandI+"_pc_namecolor", 1, 3, 3, false, pcMap);
		ArgumentConstructor pc_player = new ArgumentConstructor(baseCommandI+"_pc_player", 1, 1, 2, false, playerMap);
		ArgumentConstructor pc_rename = new ArgumentConstructor(baseCommandI+"_pc_rename", 1, 3, 3, false, pcMap);
		ArgumentConstructor pc_symbol = new ArgumentConstructor(baseCommandI+"_pc_symbol", 1, 3, 3, false, pcMap);
		ArgumentConstructor pc_unban = new ArgumentConstructor(baseCommandI+"_pc_unban", 1, 3, 3, false, pcPlayerMap);
		ArgumentConstructor pc_vice = new ArgumentConstructor(baseCommandI+"_pc_vice", 1, 3, 3, false, pcPlayerMap);
		ArgumentConstructor pc = new ArgumentConstructor(baseCommandI+"_pc", 0, 0, 0, false, null,
				pc_ban, pc_changepassword, pc_channels, pc_chatcolor, pc_create, pc_delete, pc_info, pc_inherit, pc_invite, pc_join,
				pc_kick, pc_leave, pc_namecolor, pc_player, pc_rename, pc_symbol, pc_unban, pc_vice);
		
		ArgumentConstructor tc_ban = new ArgumentConstructor(baseCommandI+"_tc_ban", 1, 2, 2, false, playerMap);
		ArgumentConstructor tc_changepassword = new ArgumentConstructor(baseCommandI+"_tc_changepassword", 1, 2, 2, false, null);
		ArgumentConstructor tc_create = new ArgumentConstructor(baseCommandI+"_tc_create", 1, 2, 3, false, null);
		ArgumentConstructor tc_info = new ArgumentConstructor(baseCommandI+"_tc_info", 1, 2, 2, false, null);
		ArgumentConstructor tc_invite = new ArgumentConstructor(baseCommandI+"_tc_invite", 2, 2, 2, false, playerMap);
		ArgumentConstructor tc_join = new ArgumentConstructor(baseCommandI+"_tc_join", 1, 2, 3, false, tcMap);
		PluginSettings.settings.addCommands(KeyHandler.SCC_TC_JOIN, tc_join.getCommandString());
		ArgumentConstructor tc_kick = new ArgumentConstructor(baseCommandI+"_tc_kick", 1, 2, 2, false, playerMap);
		ArgumentConstructor tc_leave = new ArgumentConstructor(baseCommandI+"_tc_leave", 1, 1, 1, false, null);
		ArgumentConstructor tc_unban = new ArgumentConstructor(baseCommandI+"_tc_unban", 1, 2, 2, false, playerMap);
		ArgumentConstructor tc = new ArgumentConstructor(baseCommandI+"_tc", 0, 0, 0, false, null,
				tc_ban, tc_changepassword, tc_create, tc_info, tc_invite, tc_join, tc_kick, tc_leave, tc_unban);
		
		CommandConstructor scc = new CommandConstructor(baseCommandI, true,
				book, broadcast, channel, channelgui,
				ignore, ignorelist, 
				item,
				mute, option, performance, unmute, updateplayer,
				pc, tc);
		
		CommandConstructor clch = new CommandConstructor(baseCommandII, true); 
		
		CommandConstructor scceditor = new CommandConstructor(baseCommandIII, false); 
		
		CommandConstructor msg = new CommandConstructor(baseCommandIV, false);
		PluginSettings.settings.addCommands(KeyHandler.MSG, msg.getCommandString());
		
		CommandConstructor re = new CommandConstructor(baseCommandV, false);
		
		CommandConstructor r = new CommandConstructor(baseCommandVI, false);
		
		CommandConstructor w = new CommandConstructor(baseCommandVII, true);
		
		registerCommand(scc.getPath(), scc.getName());
		getCommand(scc.getName()).setExecutor(new SccCommandExecutor(plugin, scc));
		getCommand(scc.getName()).setTabCompleter(new TABCompletion(plugin));
		
		registerCommand(scceditor.getPath(), scceditor.getName());
		getCommand(scceditor.getName()).setExecutor(new SccEditorCommandExecutor(plugin, scceditor));
		
		registerCommand(clch.getPath(), clch.getName());
		getCommand(clch.getName()).setExecutor(new ClickChatCommandExecutor(plugin, clch));
		
		registerCommand(msg.getPath(), msg.getName());
		getCommand(msg.getName()).setExecutor(new MessageCommandExecutor(plugin, msg));
		getCommand(msg.getName()).setTabCompleter(new TABCompletion(plugin));
		
		registerCommand(re.getPath(), re.getName());
		getCommand(re.getName()).setExecutor(new ReCommandExecutor(plugin, re));
		getCommand(re.getName()).setTabCompleter(new TABCompletion(plugin));
		
		registerCommand(r.getPath(), r.getName());
		getCommand(r.getName()).setExecutor(new RCommandExecutor(plugin, r));
		getCommand(r.getName()).setTabCompleter(new TABCompletion(plugin));
		
		registerCommand(w.getPath(), w.getName());
		getCommand(w.getName()).setExecutor(new WCommandExecutor(plugin, w));
		getCommand(w.getName()).setTabCompleter(new TABCompletion(plugin));
				
		addingHelps(scc,
				book, broadcast, channel, channelgui,
				ignore, ignorelist, 
				item, item_rename, item_replacers,
				mute, unmute, performance,
				option, option_channel, option_join, option_spy, updateplayer,
				pc, pc_ban, pc_changepassword, pc_channels, pc_chatcolor, pc_create, pc_delete, pc_info, pc_inherit, pc_invite,
					pc_join, pc_kick, pc_leave, pc_namecolor, pc_player, pc_rename, pc_symbol, pc_unban, pc_vice,
				tc_ban, tc_changepassword, tc_create, tc_info, tc_invite, tc_join, tc_kick, tc_leave, tc_unban,
			clch, scceditor, 
			msg, re, r, w);
				
		//All Commands which are deactivated, if scc is active on bungeecord
		if(!PluginSettings.settings.isBungee())
		{
			new ARGChannel(plugin, channel);
			new ARGIgnore(plugin, ignore);
			new ARGIgnoreList(plugin, ignorelist);
			new ARGMute(plugin, mute);
			new ARGUnmute(plugin, unmute);
			
			new ARGPerformance(plugin, performance);
			
			new ARGOption(plugin, option);
			new ARGOption_Channel(plugin, option_channel);
			new ARGOption_Join(plugin, option_join);
			new ARGOption_Spy(plugin, option_spy);
			
			new ARGPermanentChannel(plugin, pc);
			new ARGPermanentChannel_Ban(plugin, pc_ban);
			new ARGPermanentChannel_Unban(plugin, pc_unban);
			new ARGPermanentChannel_ChangePassword(plugin, pc_changepassword);
			new ARGPermanentChannel_Channels(plugin, pc_channels);
			new ARGPermanentChannel_ChatColor(plugin, pc_chatcolor);
			new ARGPermanentChannel_Create(plugin, pc_create);
			new ARGPermanentChannel_Delete(plugin, pc_delete);
			new ARGPermanentChannel_Info(plugin, pc_info);
			new ARGPermanentChannel_Inherit(plugin, pc_inherit);
			new ARGPermanentChannel_Invite(plugin, pc_invite);
			new ARGPermanentChannel_Join(plugin, pc_join);
			new ARGPermanentChannel_Kick(plugin, pc_kick);
			new ARGPermanentChannel_Leave(plugin, pc_leave);
			new ARGPermanentChannel_NameColor(plugin, pc_namecolor);
			new ARGPermanentChannel_Player(plugin, pc_player);
			new ARGPermanentChannel_Rename(plugin, pc_rename);
			new ARGPermanentChannel_Symbol(plugin, pc_symbol);
			new ARGPermanentChannel_Vice(plugin, pc_vice);
			
			new ARGTemporaryChannel(plugin, tc);
			new ARGTemporaryChannel_Ban(plugin, tc_ban);
			new ARGTemporaryChannel_Unban(plugin, tc_unban);
			new ARGTemporaryChannel_ChangePassword(plugin, tc_changepassword);
			new ARGTemporaryChannel_Create(plugin, tc_create);
			new ARGTemporaryChannel_Info(plugin, tc_info);
			new ARGTemporaryChannel_Invite(plugin, tc_invite);
			new ARGTemporaryChannel_Join(plugin, tc_join);
			new ARGTemporaryChannel_Kick(plugin, tc_kick);
			new ARGTemporaryChannel_Leave(plugin, tc_leave);
		}
		new ARGBook(plugin, book);
		new ARGBroadcast(plugin, broadcast);
		new ARGChannelGui(plugin, channelgui);
		new ARGItem(plugin, item);
		new ARGItem_Rename(plugin, item_rename);
		new ARGItem_Replacers(plugin, item_replacers);
		
		if(plugin.getYamlHandler().getConfig().getBoolean("Use.Mail", true))
		{			
			//INFO:Mail
			ArgumentConstructor mail_forward = new ArgumentConstructor(baseCommandVIII+"_forward", 0, 2, 2, true, null);
			PluginSettings.settings.addCommands(KeyHandler.MAIL_FORWARD, mail_forward.getCommandString());
			ArgumentConstructor mail_lastreceivedmails = new ArgumentConstructor(baseCommandVIII+"_lastreceivedmails", 0, 0, 2, false, null);
			PluginSettings.settings.addCommands(KeyHandler.MAIL_LASTRECEIVEDMAILS, mail_lastreceivedmails.getCommandString());
			ArgumentConstructor mail_lastsendedmails = new ArgumentConstructor(baseCommandVIII+"_lastsendedmails", 0, 0, 2, false, null);
			PluginSettings.settings.addCommands(KeyHandler.MAIL_LASTSENDEDMAILS, mail_lastsendedmails.getCommandString());
			ArgumentConstructor mail_read = new ArgumentConstructor(baseCommandVIII+"_read", 0, 1, 1, false, null);
			PluginSettings.settings.addCommands(KeyHandler.MAIL_READ, mail_read.getCommandString());
			ArgumentConstructor mail_send = new ArgumentConstructor(baseCommandVIII+"_send", 0, 3, 999, true, null);
			PluginSettings.settings.addCommands(KeyHandler.MAIL_SEND, mail_send.getCommandString());
			
			
			CommandConstructor mail = new CommandConstructor(baseCommandVIII, true,
					mail_read, mail_send, mail_lastreceivedmails);
			PluginSettings.settings.addCommands(KeyHandler.MAIL, mail.getCommandString());
			
			registerCommand(mail.getPath(), mail.getName());
			getCommand(mail.getName()).setExecutor(new MailCommandExecutor(plugin, mail));
			getCommand(mail.getName()).setTabCompleter(new TABCompletion(plugin));
			
			addingHelps(mail,
							mail_forward, mail_lastreceivedmails, mail_lastsendedmails, mail_read, mail_send);
			
			new ARGForward(plugin, mail_forward);
			new ARGLastReceivedMails(plugin, mail_lastreceivedmails);
			new ARGLastSendedMails(plugin, mail_lastsendedmails);
			new ARGRead(plugin, mail_read);
			new ARGSend(plugin, mail_send);
		}
	}
	
	public void ListenerSetup()
	{
		PluginManager pm = getServer().getPluginManager();
		getServer().getMessenger().registerIncomingPluginChannel(plugin, StaticValues.SCC_TOSPIGOT, new ServerListener(plugin));
		getServer().getMessenger().registerOutgoingPluginChannel(plugin, StaticValues.SCC_TOBUNGEE);
		pm.registerEvents(new ChatListener(plugin), plugin);
		pm.registerEvents(new JoinLeaveListener(plugin), plugin);
		pm.registerEvents(new LocationUpdateListener(), plugin);
		
		pm.registerEvents(new GuiPreListener(), plugin);
		pm.registerEvents(new GuiListener(plugin), plugin);
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
	
	private void setupChatTitles()
	{
		YamlConfiguration cti = yamlHandler.getChatTitle();
		for(String key : cti.getKeys(false))
		{
			if(cti.get(key+".UniqueIdentifierName") == null 
					|| cti.get(key+".IsPrefix") == null 
					|| cti.get(key+".InChatName") == null
					|| cti.get(key+".InChatColorCode") == null
					|| cti.get(key+".Hover") == null
					|| cti.get(key+".Permission") == null
					|| cti.get(key+".Weight") == null)
			{
				continue;
			}
			ChatTitle ct = new ChatTitle(cti.getString(key+".UniqueIdentifierName"),
					cti.getBoolean(key+".IsPrefix"),
					cti.getString(key+".InChatName"),
					cti.getString(key+".InChatColorCode"),
					cti.getString(key+".Hover"),
					cti.getString(key+".Permission"),
					cti.getInt(key+".Weight"));
			if(ct.isPrefix())
			{
				chatTitlesPrefix.add(ct);
			} else
			{
				chatTitlesSuffix.add(ct);
			}
		}
		chatTitlesPrefix.sort(Comparator.comparing(ChatTitle::getWeight));
		Collections.reverse(chatTitlesPrefix);
		chatTitlesSuffix.sort(Comparator.comparing(ChatTitle::getWeight));
		Collections.reverse(chatTitlesSuffix);
	}
	
	private void setupChannels()
	{
		YamlConfiguration cha = yamlHandler.getChannels();
		for(String key : cha.getKeys(false))
		{
			if(cha.get(key+".UniqueIdentifierName") == null
					|| cha.get(key+".Symbol") == null
					|| cha.get(key+".InChatName") == null
					|| cha.get(key+".InChatColorMessage") == null
					|| cha.get(key+".Permission") == null
					|| cha.get(key+".JoinPart") == null
					|| cha.get(key+".ChatFormat") == null)
			{
				continue;
			}
			LinkedHashMap<String, String> serverReplacerMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> serverCommandMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> serverHoverMap = new LinkedHashMap<>();
			if(cha.get(key+".ServerConverter") != null)
			{
				for(String s : cha.getStringList(key+".ServerConverter"))
				{
					if(!s.contains(";"))
					{
						continue;
					}
					String[] split = s.split(";");
					if(split.length != 4)
					{
						continue;
					}
					serverReplacerMap.put(split[0], split[1]);
					serverCommandMap.put(split[0], split[2]);
					serverHoverMap.put(split[0], split[3]);
				}
			}
			LinkedHashMap<String, String> worldReplacerMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> worldCommandMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> worldHoverMap = new LinkedHashMap<>();
			if(cha.get(key+".WorldConverter") != null)
			{
				for(String s : cha.getStringList(key+".WorldConverter"))
				{
					if(!s.contains(";"))
					{
						continue;
					}
					String[] split = s.split(";");
					if(split.length != 4)
					{
						continue;
					}
					worldReplacerMap.put(split[0], split[1]);
					worldCommandMap.put(split[0], split[2]);
					worldHoverMap.put(split[0], split[3]);
				}
			}
			Channel c = new Channel(
					cha.getString(key+".UniqueIdentifierName"),
					cha.getString(key+".Symbol"),
					cha.getString(key+".InChatName"),
					cha.getString(key+".InChatColorMessage"),
					cha.getString(key+".Permission"),
					cha.getString(key+".JoinPart"),
					cha.getString(key+".ChatFormat"),
					cha.getBoolean(key+".UseSpecificServer", false),
					cha.getBoolean(key+".UseSpecificsWorld", false),
					cha.getInt(key+".UseBlockRadius", 0),
					cha.getLong(key+".MinimumTimeBetweenMessages", 500),
					cha.getLong(key+".MinimumTimeBetweenSameMessages", 1000),
					cha.getDouble(key+".PercentOfSimiliarityOrLess", 75.0),
					cha.getString(key+".TimeColor", "&r"),
					cha.getString(key+".PlayernameCustomColor", "&r"),
					cha.getString(key+".OtherPlayernameCustomColor", "&r"),
					cha.getString(key+".SeperatorBetweenPrefix", ""),
					cha.getString(key+".SeperatorBetweenSuffix", ""),
					cha.getString(key+".MentionSound", "ENTITY_WANDERING_TRADER_REAPPEARED"),
					serverReplacerMap, serverCommandMap, serverHoverMap,
					worldReplacerMap, worldCommandMap, serverHoverMap,
					cha.getBoolean(key+".UseColor", false),
					cha.getBoolean(key+".UseItemReplacer", false),
					cha.getBoolean(key+".UseBookReplacer", false),
					cha.getBoolean(key+".UseRunCommandReplacer", false),
					cha.getBoolean(key+".UseSuggestCommandReplacer", false),
					cha.getBoolean(key+".UseWebsiteReplacer", false),
					cha.getBoolean(key+".UseEmojiReplacer", false),
					cha.getBoolean(key+".UseMentionReplacer", false),
					cha.getBoolean(key+".UsePositionReplacer", false));
			if(key.equalsIgnoreCase("permanent") || key.equalsIgnoreCase("temporary")
					|| key.equalsIgnoreCase("private"))
			{
				if(key.equalsIgnoreCase("permanent"))
				{
					c.setUniqueIdentifierName("Permanent");
					if(c.getSymbol().equalsIgnoreCase("NULL"))
					{
						c.setSymbol(".");
					}
				} else if(key.equalsIgnoreCase("temporary"))
				{
					c.setUniqueIdentifierName("Temporary");
					if(c.getSymbol().equalsIgnoreCase("NULL"))
					{
						c.setSymbol(";");
					}
				} else
				{
					c.setUniqueIdentifierName("Private");
					c.setUseMentionReplacer(false);
				}
				channels.put(c.getSymbol(), c);
			} else
			{
				if(c.getSymbol().equalsIgnoreCase("null") && nullChannel == null)
				{
					nullChannel = c;
				} else
				{
					channels.put(c.getSymbol(), c);
				}
			}
		}
	}
	
	public Channel getChannel(String uniqueIdentifierName)
	{
		for(Channel ch : channels.values())
		{
			if(ch.getUniqueIdentifierName().equals(uniqueIdentifierName))
			{
				return ch;
			}
		}
		if(nullChannel.getUniqueIdentifierName().equals(uniqueIdentifierName))
		{
			return nullChannel;
		} 
		return null;
	}
	
	public void setupEmojis()
	{
		for(String e : yamlHandler.getEmojis().getKeys(false))
		{
			String emoji = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Emoji.Start")
					+ e
					+ plugin.getYamlHandler().getConfig().getString("ChatReplacer.Emoji.End");
			ChatHandler.emojiList.put(emoji, yamlHandler.getEmojis().getString(e));
		}
	}
	
	private void setupIFH()
	{      
        if (plugin.getServer().getPluginManager().isPluginEnabled("InterfaceHub")) 
		{
			mtb = new MessageToBungeeAPI();
            plugin.getServer().getServicesManager().register(
            		main.java.me.avankziar.interfacehub.spigot.interfaces.MessageToBungee.class,
            		mtb,
            		this,
                    ServicePriority.Normal);
            log.info(pluginName + " detected InterfaceHub. Hooking with MessageToBungee!");
            
            bctb = new BaseComponentToBungeeAPI();
            plugin.getServer().getServicesManager().register(
            		main.java.me.avankziar.interfacehub.spigot.interfaces.BaseComponentToBungee.class,
            		bctb,
            		this,
                    ServicePriority.Normal);
            log.info(pluginName + " detected InterfaceHub. Hooking with BaseComponentToBungee!");
        }
	}
	
	public void setupBstats()
	{
		int pluginId = 8402;
        new Metrics(this, pluginId);
	}
}
