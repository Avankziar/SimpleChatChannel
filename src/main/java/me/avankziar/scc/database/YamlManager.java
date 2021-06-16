package main.java.me.avankziar.scc.database;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.database.FileHandler.ISO639_2B;
import main.java.me.avankziar.scc.spigot.guihandling.GUIApi.SettingsLevel;
import main.java.me.avankziar.scc.spigot.guihandling.GuiValues;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<ISO639_2B, ArrayList<String>> configMap = new LinkedHashMap<>();
	private static LinkedHashMap<ISO639_2B, ArrayList<String>> commandsMap = new LinkedHashMap<>();
	private static LinkedHashMap<ISO639_2B, ArrayList<String>> languageMap = new LinkedHashMap<>();
	private static LinkedHashMap<ISO639_2B, ArrayList<String>> chatTitleMap = new LinkedHashMap<>();
	private static LinkedHashMap<ISO639_2B, ArrayList<String>> channelsMap = new LinkedHashMap<>();
	private static LinkedHashMap<ISO639_2B, ArrayList<String>> emojisMap = new LinkedHashMap<>();
	private static LinkedHashMap<ISO639_2B, ArrayList<String>> wordFilterMap = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<ISO639_2B, ArrayList<String>>> guiMap = new LinkedHashMap<>();
	
	public YamlManager(boolean spigot)
	{
		initConfig();
		/*initCommands();
		initLanguage();
		initChatTitle();
		initChannels();
		initEmojis();
		if(spigot)
		{
			initGuis();
		}
		initWordFilter();*/
	}
	
	public ISO639_2B getLanguageType()
	{
		return languageType;
	}

	public void setLanguageType(ISO639_2B languageType)
	{
		this.languageType = languageType;
	}
	
	public ISO639_2B getDefaultLanguageType()
	{
		return defaultLanguageType;
	}
	
	public LinkedHashMap<ISO639_2B, ArrayList<String>> getConfigMap()
	{
		return configMap;
	}
	
	public LinkedHashMap<ISO639_2B, ArrayList<String>> getCommandsMap()
	{
		return commandsMap;
	}

	public LinkedHashMap<ISO639_2B, ArrayList<String>> getLanguageMap()
	{
		return languageMap;
	}

	public LinkedHashMap<ISO639_2B, ArrayList<String>> getChatTitleMap()
	{
		return chatTitleMap;
	}

	public LinkedHashMap<ISO639_2B, ArrayList<String>> getChannelsMap()
	{
		return channelsMap;
	}

	public LinkedHashMap<ISO639_2B, ArrayList<String>> getEmojisMap()
	{
		return emojisMap;
	}

	public LinkedHashMap<ISO639_2B, ArrayList<String>> getWordFilterMap()
	{
		return wordFilterMap;
	}

	public LinkedHashMap<ISO639_2B, ArrayList<String>> getGuiMap(String key)
	{
		return guiMap.get(key);
	}

	public void add(LinkedHashMap<ISO639_2B, ArrayList<String>> map, ISO639_2B language, String s)
	{
		ArrayList<String> list = (map.get(language) != null) ? map.get(language) : new ArrayList<>();
		list.add(s);
		if(map.get(language) != null)
		{
			map.replace(language, list);
		} else
		{
			map.put(language, list);
		}
	}
	
	public void add(LinkedHashMap<ISO639_2B, ArrayList<String>> map, String key,
			Object s, Object sII)
	{
		add(map, ISO639_2B.GER, key+s);
		add(map, ISO639_2B.ENG, key+sII);
	}
	
	public void add(LinkedHashMap<ISO639_2B, ArrayList<String>> map, String key)
	{
		add(map, ISO639_2B.GER, key);
		add(map, ISO639_2B.ENG, key);
	}
	
	public void initConfig() //INFO:Config
	{
		add(configMap, ISO639_2B.GER, "#          + ----------------------------------------------------------------------------- +");
		add(configMap, ISO639_2B.GER, "# Welcome to the Config of SimpleChatChannels.");
		add(configMap, ISO639_2B.GER, "# If you wish to see the wiki, see https://github.com/Avankziar/SimpleChatChannel/wiki");
		add(configMap, ISO639_2B.GER, "# For a direct support, see on my discord https://discord.gg/JkWGRxw");
		add(configMap, ISO639_2B.GER, "# Please note that I am only a hobby developer and therefore still have a RL main job^^.");
		add(configMap, ISO639_2B.GER, "# So it may happen that I can't help you directly.");
		add(configMap, ISO639_2B.GER, "# But if you leave a message and explain your problem, I will try to answer you the same day (CEST).");
		add(configMap, ISO639_2B.GER, "#          + ----------------------------------------------------------------------------- +");
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "# Language defines the output of the plugin.");
		add(configMap, ISO639_2B.GER, "# Only English and German (ENG and GER) are supported out of the box. For the other languages see");
		add(configMap, ISO639_2B.GER, "# For the other languages see https://user-images.githubusercontent.com/48923414/103370012-fab64d80-4acb-11eb-9b3f-3e7a133d0e4c.JPG");
		add(configMap, ISO639_2B.GER, "Language: 'ENG'");
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "# The Servername (only Spigotserver) how the server a named in BungeeCord.");
		add(configMap, ISO639_2B.GER, "Server: 'hub'");
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "# For Spigotserver to sync with the Bungeecordserver Plugin.");
		add(configMap, ISO639_2B.GER, "IsBungeeActive: "+true);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "# Mysql and Stuff...");
		add(configMap, ISO639_2B.GER, "Mysql:");
		add(configMap, ISO639_2B.GER, "  Status: "+false);
		add(configMap, ISO639_2B.GER, "  Host: '127.0.0.1'");
		add(configMap, ISO639_2B.GER, "  Port: "+3306);
		add(configMap, ISO639_2B.GER, "  DatabaseName: 'mydatabase'");
		add(configMap, ISO639_2B.GER, "  SSLEnabled: "+false);
		add(configMap, ISO639_2B.GER, "  AutoReconnect: "+true);
		add(configMap, ISO639_2B.GER, "  VerifyServerCertificate: "+false);
		add(configMap, ISO639_2B.GER, "  User: 'admin'");
		add(configMap, ISO639_2B.GER, "  Password: 'not_0123456789'");
		add(configMap, ISO639_2B.GER, "  TableNameI: 'simplechatchannelsPlayerData'");
		add(configMap, ISO639_2B.GER, "  TableNameII: 'simplechatchannelsIgnorelist'");
		add(configMap, ISO639_2B.GER, "  TableNameIII: 'simplechatchannelsPermanentChannels'");
		add(configMap, ISO639_2B.GER, "  TableNameIV: 'simplechatchannelsItemJson'");
		add(configMap, ISO639_2B.GER, "  TableNameV: 'simplechatchannelsPlayerUsedChannels'");
		add(configMap, ISO639_2B.GER, "  TableNameVI: 'simplechatchannelsMails'");
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "# Determines whether the commands of the respective category should be loaded at all.");
		add(configMap, ISO639_2B.GER, "Use:");
		add(configMap, ISO639_2B.GER, "  Mail: "+true);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "Mail:");
		add(configMap, ISO639_2B.GER, "  # Which Channel in the channel.yml is used for Mail parsing.");
		add(configMap, ISO639_2B.GER, "  UseChannelForMessageParser: 'Global'");
		add(configMap, ISO639_2B.GER, "  # How to display the name for the console in a console to player mail.");
		add(configMap, ISO639_2B.GER, "  ConsoleReplacerInSendedMails: 'Console'");
		add(configMap, ISO639_2B.GER, "  # Which character is used, to seperate the cc in a mail.");
		add(configMap, ISO639_2B.GER, "  CCSeperator: '@'");
		add(configMap, ISO639_2B.GER, "  SubjectMessageSeperator: '<>'");
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "PrivateChannel:");
		add(configMap, ISO639_2B.GER, "  # Use your server multiple and dynamic Colors for multiple player chatpartner.");
		add(configMap, ISO639_2B.GER, "  # If false, it will use the color which is set in the private channel in channel.yml");
		add(configMap, ISO639_2B.GER, "  UseDynamicColor: "+true);
		add(configMap, ISO639_2B.GER, "  # The dynamic colors. In minecraft the hex color arent so differnt for the human eye.");
		add(configMap, ISO639_2B.GER, "  # So I have select only 10 colors. You can use more^^");
		add(configMap, ISO639_2B.GER, "  DynamicColorPerPlayerChat:");
		add(configMap, ISO639_2B.GER, "  - '&#F5A9F2'");
		add(configMap, ISO639_2B.GER, "  - '&#F7819F'");
		add(configMap, ISO639_2B.GER, "  - '&#FA58F4'");
		add(configMap, ISO639_2B.GER, "  - '&#FE2E64'");
		add(configMap, ISO639_2B.GER, "  - '&#FF00FF'");
		add(configMap, ISO639_2B.GER, "  - '&#DF013A'");
		add(configMap, ISO639_2B.GER, "  - '&#B404AE'");
		add(configMap, ISO639_2B.GER, "  - '&#8A0829'");
		add(configMap, ISO639_2B.GER, "  - '&#D0A9F5'");
		add(configMap, ISO639_2B.GER, "  - '&#9A2EFE'");
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "PermanentChannel: ");
		add(configMap, ISO639_2B.GER, "  # The amount of permanentchannel to owner by a player. (Memberstatus dont count!)");
		add(configMap, ISO639_2B.GER, "  AmountPerPlayer: "+1);
		add(configMap, ISO639_2B.GER, "  # The invite cooldown in seconds.");
		add(configMap, ISO639_2B.GER, "  InviteCooldown: "+60);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "TemporaryChannel:");
		add(configMap, ISO639_2B.GER, "  # The invite cooldown in seconds");
		add(configMap, ISO639_2B.GER, "  InviteCooldown: "+60);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "BroadCast:");
		add(configMap, ISO639_2B.GER, "  # Which Channel is use to parse the broadcast message.");
		add(configMap, ISO639_2B.GER, "  UsingChannel: 'Global'");
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "Mute: ");
		add(configMap, ISO639_2B.GER, "  # If someone is muted, should it display globally?");
		add(configMap, ISO639_2B.GER, "  SendGlobal: "+true);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "  # The global switch, if sound is used.");
		add(configMap, ISO639_2B.GER, "MsgSoundUsage: "+true);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "  # If true, the players muss turn off the Joinmessage themself by join, if they wish to dont see them.");
		add(configMap, ISO639_2B.GER, "  # If false, the players muss turn off the Joinmessage themself by join, if they wish to see them.");
		add(configMap, ISO639_2B.GER, "  # Only crucial for the very first join on the server!");
		add(configMap, ISO639_2B.GER, "JoinMessageDefaultValue: "+true);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "CleanUp:");
		add(configMap, ISO639_2B.GER, "  # True, if the cleanup should start. (Starts only by start of the server, to save resources.");
		add(configMap, ISO639_2B.GER, "  RunAutomaticByRestart: "+true);
		add(configMap, ISO639_2B.GER, "  # Delete player if there last join is older than x days.");
		add(configMap, ISO639_2B.GER, "  DeletePlayerWhichJoinIsOlderThanDays: "+120);
		add(configMap, ISO639_2B.GER, "  # Only READED mail will be deleted after x days. Aka the readed date, not the sended date ;)");
		add(configMap, ISO639_2B.GER, "  DeleteReadedMailWhichIsOlderThanDays: "+365);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "# Here are display all ingame shortcuts and parsing display for the chat.");
		add(configMap, ISO639_2B.GER, "# Generally are path with >Replace< the replace parser for an input.");
		add(configMap, ISO639_2B.GER, "# Start, End and Seperator determind how to write a shourtcut.");
		add(configMap, ISO639_2B.GER, "# If a separator is present, a value is expected after it.");
		add(configMap, ISO639_2B.GER, "# General example:");
		add(configMap, ISO639_2B.GER, "# Chatinput: ' cmd/warp+spawn' [Note: + are a replacer char for the spacebar aka ' ']");
		add(configMap, ISO639_2B.GER, "# Parseoutput: ' &7[&fClickCmd: /warp spawn&7]' with a SuggestCommand if you click on it with '/warp spawn'");
		add(configMap, ISO639_2B.GER, "ChatReplacer:");
		add(configMap, ISO639_2B.GER, "  Command:");
		add(configMap, ISO639_2B.GER, "    RunCommandStart: 'cmd//'");
		add(configMap, ISO639_2B.GER, "    SuggestCommandStart: 'cmd/'");
		add(configMap, ISO639_2B.GER, "    CommandStartReplacer: '&7[&fClickCmd: /'");
		add(configMap, ISO639_2B.GER, "    CommandEndReplacer: '&7]'");
		add(configMap, ISO639_2B.GER, "    SpaceReplacer: '+'");
		add(configMap, ISO639_2B.GER, "  # Here it must be considered only that an item must be entered first by /scc item,");
		add(configMap, ISO639_2B.GER, "  # into the gui and then the name with /scc item rename <old name> <new name>,");
		add(configMap, ISO639_2B.GER, "  # must be renamed, so that one can write here the value behind the Seperator.");
		add(configMap, ISO639_2B.GER, "  Item:");
		add(configMap, ISO639_2B.GER, "    Start: '<item'");
		add(configMap, ISO639_2B.GER, "    Seperator: ':'");
		add(configMap, ISO639_2B.GER, "    End: '>'");
		add(configMap, ISO639_2B.GER, "# What was valid for the items also applies here.");
		add(configMap, ISO639_2B.GER, "  Book:");
		add(configMap, ISO639_2B.GER, "    Start: '<book'");
		add(configMap, ISO639_2B.GER, "    Seperator: ':'");
		add(configMap, ISO639_2B.GER, "    End: '>'");
		add(configMap, ISO639_2B.GER, "# The value, aka the path from the emoji.yml, muss be between Start and End.");
		add(configMap, ISO639_2B.GER, "  Emoji:");
		add(configMap, ISO639_2B.GER, "    Start: ':|'");
		add(configMap, ISO639_2B.GER, "    End: '|:'");
		add(configMap, ISO639_2B.GER, "# To mention a player, for example 'Avankziar', it muss be written ' @@Avankziar '");
		add(configMap, ISO639_2B.GER, "# You can also write ' @@Avank ' or an even shorter word.");
		add(configMap, ISO639_2B.GER, "# But if you write ' @@A ' and there are several players that start with A,");
		add(configMap, ISO639_2B.GER, "# it will take the player it fits best. Aka Alphabetical.");
		add(configMap, ISO639_2B.GER, "  Mention:");
		add(configMap, ISO639_2B.GER, "    Start: '@@'");
		add(configMap, ISO639_2B.GER, "    # The Color, which is the mention player. Only seeable for player, which are mention in the same message!");
		add(configMap, ISO639_2B.GER, "    Color: '&4'");
		add(configMap, ISO639_2B.GER, "    # The Sound which are played, if you where mention. Spigot server muss be present.");
		add(configMap, ISO639_2B.GER, "    SoundEnum: 'ENTITY_WANDERING_TRADER_REAPPEARED'");
		add(configMap, ISO639_2B.GER, "  Position:");
		add(configMap, ISO639_2B.GER, "    # The chatinput to disply the own position.");
		add(configMap, ISO639_2B.GER, "    Replacer: '<pos>'");
		add(configMap, ISO639_2B.GER, "    Replace: '&7[&9%server% &d%world% &e%x% %y% %z%&7]'");
		add(configMap, ISO639_2B.GER, "  # If you wish to use a new Line in the same Message, use '<previous line message> ~!~ <New line message>'");
		add(configMap, ISO639_2B.GER, "  NewLine: '~!~'");
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "# From here on only spigot values!");
		add(configMap, ISO639_2B.GER, "Gui:");
		add(configMap, ISO639_2B.GER, "  # The following two values determine how the Boolean false and true can be displayed more nicely.");
		add(configMap, ISO639_2B.GER, "  ActiveTerm: '&a✔'");
		add(configMap, ISO639_2B.GER, "  DeactiveTerm: '&c✖'");
		add(configMap, ISO639_2B.GER, "  # Determines how many rows the Channels Gui should have.");
		add(configMap, ISO639_2B.GER, "  Channels:");
		add(configMap, ISO639_2B.GER, "    RowAmount: "+6);
		add(configMap, ISO639_2B.GER, "");
		add(configMap, ISO639_2B.GER, "# Dont touch. This is a intern List!");
		add(configMap, ISO639_2B.GER, "GuiList:");
		add(configMap, ISO639_2B.GER, "- 'CHANNELS'");
	}
	
	//INFO:Commands
	public void initCommands()
	{
		comBypass();
		commandsInput("scc", "scc", "scc.cmd.scc.scc", 
				"/scc [pagenumber]", "/scc ",
				"'&c/scc [Seitenzahl] &f| Infoseite für alle Befehle.",
				"'&c/scc [pagenumber] &f| Info page for all commands.");
		commandsInput("scceditor", "scceditor", "scc.cmd.scceditor", 
				"/scceditor", "/scceditor ",
				"'&c/scceditor &f| ChatEditor Toggle.",
				"'&c/scceditor &f| ChatEditor toggle.");
		commandsInput("clch", "clch", "scc.cmd.clch", 
				"/clch [pagenumber]", "/clch ",
				"'&c/clch <Spielername> <Zahl> <Nachricht...> &f| Sendet einen Klickbaren Chat für den Spieler. Geeignet für Citizen / Denizen plugin.",
				"'&c/clch <player name> <number> <message...> &f| Sends a clickable chat for the player. Suitable for Citizen / Denizen plugin.");
		commandsInput("msg", "msg", "scc.cmd.msg", 
				"/msg <player> <message...>", "/msg ",
				"'&c/msg <Spielernamen> <Nachricht> &f| Schreibt dem Spieler privat. Alle Spieler, welche online sind, werden als Vorschlag angezeigt.",
				"'&c/msg <player name> <message> &f| Write to the player privately. All players who are online will be displayed as a suggestion.");
		commandsInput("re", "re", "scc.cmd.re", 
				"/re <player> <message...>", "/re ",
				"'&c/re <Spielernamen> <Nachricht> &f| Schreibt dem Spieler privat. Alle Spieler mit denen man schon geschrieben hat, werden als Vorschlag angezeigt.",
				"'&c/re <player name> <message> &f| Write to the player privately. All players with whom you have already written are displayed as suggestions.");
		commandsInput("r", "r", "scc.cmd.r", 
				"/r <message...>", "/r ",
				"'&c/r <message...> &f| Schreibt dem Spieler, welcher einem selbst zuletzt privat geschrieben hat.",
				"'&c/r <message...> &f| Write to the player who last wrote to you privately.");
		commandsInput("w", "w", "scc.cmd.w", 
				"/w <player>", "/w ",
				"'&c/w [Spielername] &f| Consolenbefehl für Privatnachrichten an Spieler.",
				"'&c/w [playername] &f| Consolecommand for private message to player.");
		String path = "scc_";
		String basePermission = "scc.cmd.scc";
		//INFO:Argument Start
		argumentInput(path+"book", "book", basePermission,
				"/scc book <Itemname>", "/scc book ",
				"'&c/scc book <Itemname> &f| Öffnet das Buch vom ItemReplacer.",
				"'&c/scc book <Itemname> &f| Open the book from ItemReplacer.");
		argumentInput(path+"broadcast", "broadcast", basePermission,
				"/scc broadcast <message...>", "/scc broadcast ",
				"'&c/scc broadcast <Nachricht> &f| Zum Senden einer Broadcast Nachricht. Falls Bungeecord aktiviert ist, kann man auch von Spigot als Console, bungeecordübergreifend dies an alle Spieler senden.",
				"'&c/scc broadcast <message> &f| To send a broadcast message. If bungeecord is enabled, you can also send this to all players from Spigot as a console, across bungeecords.");
		argumentInput(path+"channel", "channel", basePermission,
				"/scc channel <channel>", "/scc channel ",
				"'&c/scc channel &f| Zum An- & Ausstellen des angegebenen Channels.",
				"'&c/scc channel &f| To turn the specified channel on & off.");
		argumentInput(path+"channelgui", "channelgui", basePermission,
				"/scc channelgui ", "/scc channelgui ",
				"'&c/scc channelgui &f| Öffnet ein Menü, wo die Channels aus und eingestellt werden können.",
				"'&c/scc channelgui &f| Opens a menu where the channels can be selected and set.");
		argumentInput(path+"ignore", "ignore", basePermission,
				"/scc ignore <player>", "/scc ignore ",
				"'&c/scc ignore <Spielername> &f| Zum Einsetzten oder Aufheben des Ignores für den Spieler.",
				"'&c/scc ignore <playername> &f| To set or remove the ignore for the player.");
		argumentInput(path+"ignorelist", "ignorelist", basePermission,
				"/scc ignorelist [playername]", "/scc ignorelist ",
				"'&c/scc ignorelist [Spielername] &f| Zum Anzeigen aller Spieler auf der Ignoreliste.",
				"'&c/scc ignorelist [playername] &f| To show all players on the ignore list.");
		argumentInput(path+"mute", "mute", basePermission,
				"/scc mute <playername> [values...]", "/scc mute ",
				"'&c/scc mute <Spielername> [Werte...] &f| Stellt den Spieler für die angegebene Zeit stumm. Bei keinem Wert ist es permanent. Möglich addidative Werte sind: (Format xxx:<Zahl>) y=Jahre, M=Monate, d=Tage, H=Stunden, m=Minuten, s=Sekunden.",
				"'&c/scc mute <playername> [values...] &f| Mutes the player for the specified time. With no value, it is permanent. Possible addidative values are: (format xxx:<number>) y=years, M=months, d=days, H=hours, m=minutes, s=seconds.");
		argumentInput(path+"performance", "performance", basePermission,
				"/scc performance ", "/scc performance ",
				"'&c/scc performance &f| Zeigt die MysqlPerformances des Plugins an.",
				"'&c/scc performance &f| Displays the MysqlPerformances of the plugin.");
		argumentInput(path+"unmute", "unmute", basePermission,
				"/scc unmute <playername", "/scc unmute ",
				"'&c/scc unmute <Spielername> &f| Zum Wiedererlangen der Schreib-Rechte.",
				"'&c/scc unmute <playername> &f| To regain write permissions.");
		argumentInput(path+"updateplayer", "updateplayer", basePermission,
				"/scc updateplayer <playername>", "/scc updateplayer ",
				"'&c/scc updateplayer <Spielername> &f| Updatet die Zugangsrechte des Spielers für alle Channels.",
				"'&c/scc updateplayer <playername> &f| Updates the player's access rights for all channels.");
		
		argumentInput(path+"option", "option", basePermission,
				"/scc option ", "/scc option ",
				"'&c/scc option &f| Zwischenbefehl",
				"'&c/scc option &f| Intermediate command");
		basePermission = "scc.cmd.scc.option";
		argumentInput(path+"option_channel", "channel", basePermission,
				"/scc option channel ", "/scc option channel ",
				"'&c/scc option channel &f| Aktiviert oder deaktiviert ob man beim Joinen seine aktiven Channels sieht.",
				"'&c/scc option channel &f| Enables or disables whether you can see your active channels when joining.");
		argumentInput(path+"option_join", "join", basePermission,
				"/scc option join", "/scc option join",
				"'&c/scc option join &f| Aktiviert oder deaktiviert ob man die Joinnachricht anderer Spieler sieht.",
				"'&c/scc option join &f| Enables or disables whether you can see the join message of other players.");
		argumentInput(path+"option_spy", "spy", basePermission,
				"/scc option spy ", "/scc option spy ",
				"'&c/scc option spy &f| Aktiviert oder deaktiviert ob man Nachrichten sehen kann, welche einem sonst vorborgen wären.",
				"'&c/scc option spy &f| Enables or disables whether you can see messages that would otherwise be hidden from you.");
		basePermission = "scc.cmd.scc.item.";
		argumentInput(path+"item", "item", basePermission,
				"/scc item ", "/scc item ",
				"'&c/scc item &f| Öffnet das Menü, wo man die Items für den Replacer einstellen kann.",
				"'&c/scc item &f| Opens the menu where you can set the items for the replacer.");
		argumentInput(path+"item_rename", "rename", basePermission,
				"/scc item rename <oldname> <newname> ", "/scc item rename ",
				"'&c/scc item rename <oldname> <newname> &f| Benennt das Item, welches auf den alten Namen hört, um.",
				"'&c/scc item rename <oldname> <newname> &f| Renames the item that goes by the old name.");
		argumentInput(path+"item_replacers", "replacers", basePermission,
				"/scc item replacers ", "/scc item replacers ",
				"'&c/scc item replacers &f| Zeigt alle Möglichen Replacer im Chat an, sowie dessen Item als Hover.",
				"'&c/scc item replacers &f| Displays all possible replacers in the chat, as well as their item as a hover.");
		//INFO:PermanentChannel
		basePermission = "scc.cmd.scc";
		argumentInput(path+"pc", "permanentchannel", basePermission,
				"/scc permanentchannel ", "/scc permanentchannel ",
				"'&c/scc permanentchannel &f| Zwischenbefehl",
				"'&c/scc permanentchannel &f| Intermediate command");
		basePermission = "scc.cmd.scc.pc";
		argumentInput(path+"pc_ban", "ban", basePermission,
				"/scc permanentchannel ban <channelname> <playername> ", "/scc permanentchannel ban",
				"'&c/scc permanentchannel ban <Channelname> <Spielername> &f| Bannt einen Spieler von einem permanenten Channel.",
				"'&c/scc permanentchannel ban <channelname> <playername> &f| Bans a player from a permanent channel.");
		argumentInput(path+"pc_unban", "unban", basePermission,
				"/scc permanentchannel unban <channelname> <playername>", "/scc permanentchannel unban ",
				"'&c/scc permanentchannel <Channelname> <Spielername> &f| Unbannt einen Spieler von einem permanenten Channel.",
				"'&c/scc permanentchannel <channelname> <playername> &f| Unbans a player from a permanent channel.");
		argumentInput(path+"pc_changepassword", "changepassword", basePermission,
				"/scc permanentchannel changepassword <channelname> <password>", "/scc permanentchannel changepassword ",
				"'&c/scc permanentchannel changepassword <Channelname> <Passwort> &f| Ändert das Passwort von einem permanenten Channel.",
				"'&c/scc permanentchannel changepassword <channelname> <password> &f| Changes the password of a permanent channel.");
		argumentInput(path+"pc_channels", "channels", basePermission,
				"/scc permanentchannel channels <channel> ", "/scc permanentchannel channels ",
				"'&c/scc permanentchannel channels <Channel> &f| Zeigt alle Channels an mit Infobefehl.",
				"'&c/scc permanentchannel channels <channel> &f| Shows all channels with info command.");
		argumentInput(path+"pc_chatcolor", "chatcolor", basePermission,
				"/scc permanentchannel chatcolor <channelname> <color> ", "/scc permanentchannel chatcolor ",
				"'&c/scc permanentchannel chatcolor <Channelname> <Farbe> &f| Ändert die Farbe des permanenten Channel für den Chat.",
				"'&c/scc permanentchannel chatcolor <channelname> <color> &f| Changes the color of the permanent channel for the chat.");
		argumentInput(path+"pc_create", "create", basePermission,
				"/scc permanentchannel create <channelname> [password] ", "/scc permanentchannel create ",
				"'&c/scc permanentchannel create <Channelname> [Passwort] &f| Erstellt einen permanenten Channel. Optional mit Passwort.",
				"'&c/scc permanentchannel create <channelname> [password] &f| Creates a permanent channel. Optionally with password.");
		argumentInput(path+"pc_delete", "delete", basePermission,
				"/scc permanentchannel delete <channelname> ", "/scc permanentchannel delete ",
				"'&c/scc permanentchannel delete <Channelname> &f| Löscht den Channel.",
				"'&c/scc permanentchannel delete <channelname> &f| Delete the channel.");
		argumentInput(path+"pc_info", "info", basePermission,
				"/scc permanentchannel info [channelname] ", "/scc permanentchannel info ",
				"'&c/scc permanentchannel info [Channelname] &f| Zeigt alle Infos zum permanenten Channel an.",
				"'&c/scc permanentchannel info [channelname] &f| Displays all info about the permanent channel.");
		argumentInput(path+"pc_inherit", "inherit", basePermission,
				"/scc permanentchannel inherit <channelname> <playername> ", "/scc permanentchannel inherit ",
				"'&c/scc permanentchannel inherit <Channelname> <Spielername> &f| Lässt den Spieler den Channel als Ersteller beerben.",
				"'&c/scc permanentchannel inherit <channelname> <playername> &f| Lets the player inherit the channel as creator.");
		argumentInput(path+"pc_invite", "invite", basePermission,
				"/scc permanentchannel invite <channelname> <playername>", "/scc permanentchannel invite ",
				"'&c/scc permanentchannel invite <Channelname> <Spielername> &f| Lädt einen Spieler in den permanenten Channel ein.",
				"'&c/scc permanentchannel invite <channelname> <playername> &f| Invites a player to the permanent Channel.");
		argumentInput(path+"pc_join", "join", basePermission,
				"/scc permanentchannel join <channelname> [password] ", "/scc permanentchannel join ",
				"'&c/scc permanentchannel join <Channelname> [Passwort] &f| Betritt einen permanenten Channel.",
				"'&c/scc permanentchannel join <channelname> [password] &f| Enter a permanent channel.");
		argumentInput(path+"pc_kick", "kick", basePermission,
				"/scc permanentchannel kick <channelname> <playername> ", "/scc permanentchannel kick ",
				"'&c/scc permanentchannel kick <Channelname> <Spielername> &f| Kickt einen Spieler von einem permanenten Channel.",
				"'&c/scc permanentchannel kick <channelname> <playername> &f| Kicks a player from a permanent channel.");
		argumentInput(path+"pc_leave", "leave", basePermission,
				"/scc permanentchannel leave <channelname> ", "/scc permanentchannel leave ",
				"'&c/scc permanentchannel leave <Channelname> &f| Verlässt einen permanenten Channel.",
				"'&c/scc permanentchannel leave <channelname> &f| Leaves a permanent channel.");
		argumentInput(path+"pc_namecolor", "namecolor", basePermission,
				"/scc permanentchannel namecolor <channelname> <color> ", "/scc permanentchannel namecolor ",
				"'&c/scc permanentchannel namecolor <Channelname> <Farbe> &f| Ändert die Farbe des permanenten Channelpräfix.",
				"'&c/scc permanentchannel namecolor <channelname> <color> &f| Changes the color of the permanent Channelprefix.");
		argumentInput(path+"pc_player", "player", basePermission,
				"/scc permanentchannel player [playername] ", "/scc permanentchannel player ",
				"'&c/scc permanentchannel player [Spielername] &f| Zeigt alle permanenten Channels, wo der Spieler beigetreten ist an.",
				"'&c/scc permanentchannel player [playername] &f| Displays all permanent channels where the player has joined.");
		argumentInput(path+"pc_rename", "rename", basePermission,
				"/scc permanentchannel rename <channelname> <newname>", "/scc permanentchannel rename ",
				"'&c/scc permanentchannel rename <Channelname> <Neuer Name> &f| Ändert den Namen des permanenten Channel.",
				"'&c/scc permanentchannel rename <channelname> <newname> &f| Changes the name of the permanent Channel.");
		argumentInput(path+"pc_symbol", "symbol", basePermission,
				"/scc permanentchannel symbol <channelname> <symbols>", "/scc permanentchannel symbol ",
				"'&c/scc permanentchannel symbol <Channelname> <Symbole> &f| Ändert das Zugangssymbol des Channels.",
				"'&c/scc permanentchannel symbol <channelname> <symbols> &f| Changes the access icon of the channel.");
		argumentInput(path+"pc_vice", "vice", basePermission,
				"/scc permanentchannel vice <channelname> <playername> ", "/scc permanentchannel vice ",
				"'&c/scc permanentchannel vice <Channelname> <Spielername> &f| Befördert oder degradiert einen Spieler innerhalb des permanenten Channels.",
				"'&c/scc permanentchannel vice <channelname> <playername> &f| Promotes or demotes a player within the permanent Channel.");
		//INFO:TemporaryChannel
		basePermission = "scc.cmd.scc";
		argumentInput(path+"tc", "temporarychannel", basePermission,
				"/scc temporarychannel ", "/scc temporarychannel ",
				"'&c/scc temporarychannel &f| Zwischenbefehl",
				"'&c/scc temporarychannel &f| Intermediate command");
		basePermission = "scc.cmd.scc.  ";
		argumentInput(path+"tc_ban", "ban", basePermission,
				"/scc temporarychannel ban <playername> ", "/scc temporarychannel ban ",
				"'&c/scc temporarychannel ban <Spielername> &f| Bannt einen Spieler von einem temporären Channel.",
				"'&c/scc temporarychannel ban <playername> &f| Bans a player from a temporary channel.");
		argumentInput(path+"tc_unban", "unban", basePermission,
				"/scc temporarychannel unban <playername> ", "/scc temporarychannel unban ",
				"'&c/scc temporarychannel unban <Spielername> &f| Entbannt einen Spieler von einem temporären Channel.",
				"'&c/scc temporarychannel unban <playername> &f| Unbans a player from a temporary channel.");
		argumentInput(path+"tc_changepassword", "changepassword", basePermission,
				"/scc temporarychannel changepassword <password> ", "/scc temporarychannel changepassword ",
				"'&c/scc temporarychannel changepassword <Passwort> &f| Ändert das Passwort von einem temporären Channel.",
				"'&c/scc temporarychannel changepassword <password> &f| Changes the password of a temporary channel.");
		argumentInput(path+"tc_create", "create", basePermission,
				"/scc temporarychannel create <channelname> [password] ", "/scc temporarychannel create ",
				"'&c/scc temporarychannel create <Channelname> [Passwort] &f| Erstellt einen temporären Channel. Optional mit Passwort.",
				"'&c/scc temporarychannel create <channelname> [password] &f| Creates a temporary channel. Optionally with password.");
		argumentInput(path+"tc_info", "info", basePermission,
				"/scc temporarychannel info ", "/scc temporarychannel info ",
				"'&c/scc temporarychannel info &f| Zeigt alle Informationen bezüglich des temporären Channels an.",
				"'&c/scc temporarychannel info &f| Displays all information related to the temporary channel.");
		argumentInput(path+"tc_invite", "invite", basePermission,
				"/scc temporarychannel invite <playername> ", "/scc temporarychannel invite ",
				"'&c/scc temporarychannel invite <Spielername> &f| Lädt einen Spieler in den eigenen temporären Channel ein.",
				"'&c/scc temporarychannel invite <playername> &f| Invites a player to the own temporary channel.");
		argumentInput(path+"tc_join", "join", basePermission,
				"/scc temporarychannel join <channelname> [password] ", "/scc temporarychannel join ",
				"'&c/scc temporarychannel join <Channelname> [Passwort] &f| Betritt einem temporären Channel.",
				"'&c/scc temporarychannel join <channelname> [password] &f| Enter a temporary channel.");
		argumentInput(path+"tc_kick", "kick", basePermission,
				"/scc temporarychannel kick <playername> ", "/scc temporarychannel kick ",
				"'&c/scc temporarychannel kick <Spielername> &f| Kickt einen Spieler von einem temporären Channel.",
				"'&c/scc temporarychannel kick <playername> &f| Kicks a player from a temporary channel.");
		argumentInput(path+"tc_leave", "leave", basePermission,
				"/scc temporarychannel leave ", "/scc temporarychannel leave ",
				"'&c/scc temporarychannel leave &f| Verlässt einen temporären Channel.",
				"'&c/scc temporarychannel leave &f| Leaves a temporary channel.");
		commandsInput("mail", "mail", "scc.cmd.mail.mail", 
				"/mail [page]", "/mail ",
				"'&c/mail [Seitenzahl] &f| Zeigt alle ungelesene Mails mit Klick- und HoverEvents.",
				"'&c/mail [pagen] &f| Shows all unread mails with click and hover events.");
		path = "mail_";
		basePermission = "scc.cmd.mail";
		argumentInput(path+"lastreceivedmails", "lastreceivedmails", basePermission,
				"/mail lastreceivedmails [page] [playername] ", "/mail lastreceivedmails ",
				"'&c/mail lastreceivedmails [Seitenzahl] [Spielername] &f| Zeigt die letzte empfangende Mails.",
				"'&c/mail lastreceivedmails [page] [playername] &f| Show the last received mails.");
		argumentInput(path+"lastsendedmails", "lastsendedmails", basePermission,
				"/mail lastsendedmails [page] [playername] ", "/mail lastsendedmails ",
				"'&c/mail lastsendedmails [Seitenzahl] [Spielername] &f| Zeigt die letzte gesendeten Mails.",
				"'&c/mail lastsendedmails [page] [playername] &f| Show the last sended mails.");
		argumentInput(path+"forward", "forward", basePermission,
				"/mail forward <id> ", "/mail forward ",
				"'&c/mail forward <id> <Spielername> &f| Leitet die Mail an den Spieler weiter.",
				"'&c/mail forward <id> <playername> &f| Forwards the mail to the player.");
		argumentInput(path+"read", "read", basePermission,
				"/mail read <id> ", "/mail read ",
				"'&c/mail read <id> &f| Liest die Mail.",
				"'&c/mail read <id> &f| Read the mail.");
		argumentInput(path+"send", "send", basePermission,
				"/mail send <reciver, multiple seperate with @> <subject...> <seperator> <message...> ", "/mail send ",
				"'&c/mail send <Empfänger, mehrere getrennt mit @> <Betreff...> <Trennwert> <Nachricht...> &f| Schreibt eine Mail.",
				"'&c/mail send <reciver, multiple seperate with @> <subject...> <seperator> <message...> &f| Write a mail.");
		/*argumentInput(path+"", "", basePermission,
				"/scc ", "/scc ",
				"'&c/scc &f| ",
				"'&c/scc &f| ");*/
	}
	
	private void comBypass() //INFO:ComBypass
	{
		add(commandsMap, ISO639_2B.GER, "Bypass:");
		add(commandsMap, ISO639_2B.GER, "  Color:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.color'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.color'");
		add(commandsMap, ISO639_2B.GER, "  Item:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.item'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.item'");
		add(commandsMap, ISO639_2B.GER, "  Book:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.book'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.book'");
		add(commandsMap, ISO639_2B.GER, "  RunnCommand:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.runcommand'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.runcommand'");
		add(commandsMap, ISO639_2B.GER, "  SuggestCommand:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.suggestcommand'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.suggestcommand'");
		add(commandsMap, ISO639_2B.GER, "  Website:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.website'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.website'");
		add(commandsMap, ISO639_2B.GER, "  Emoji:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.emoji'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.emoji'");
		add(commandsMap, ISO639_2B.GER, "  Mention:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.mention'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.mention'");
		add(commandsMap, ISO639_2B.GER, "  Position:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.position'");
		add(commandsMap, ISO639_2B.GER, "    Bypass: 'scc.bypass.position'");
		add(commandsMap, ISO639_2B.GER, "  Sound:");
		add(commandsMap, ISO639_2B.GER, "    Channel: 'scc.channel.sound'");
		add(commandsMap, ISO639_2B.GER, "  Ignore: 'scc.bypass.ignore'");
		add(commandsMap, ISO639_2B.GER, "  OfflineChannel: 'scc.bypass.offlinechannel'");
		add(commandsMap, ISO639_2B.GER, "  PermanentChannel: 'scc.bypass.permanentchannel'");
		add(commandsMap, ISO639_2B.GER, "  BookOther: 'scc.bypass.bookother'");
		add(commandsMap, ISO639_2B.GER, "  Mail:");
		add(commandsMap, ISO639_2B.GER, "    ReadOther: 'scc.bypass.mail.readother'");
		add(commandsMap, ISO639_2B.GER, "  ItemReplacerStorage: 'scc.custom.itemreplacerstorage.'");
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		add(commandsMap, ISO639_2B.GER, path+":");
		add(commandsMap, ISO639_2B.GER, "  Name: '"+name+"'");
		add(commandsMap, ISO639_2B.GER, "  Permission: '"+basePermission+"'");
		add(commandsMap, ISO639_2B.GER, "  Suggestion: '"+suggestion+"'");
		add(commandsMap, ISO639_2B.GER, "  CommandString: '"+commandString+"'");
		add(commandsMap, "  HelpInfo: ",
				"'"+helpInfoGerman+"'",
				"'"+helpInfoEnglish+"'");
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		add(commandsMap, ISO639_2B.GER, path+":");
		add(commandsMap, ISO639_2B.GER, "  Argument: '"+argument+"'");
		add(commandsMap, ISO639_2B.GER, "  Permission: '"+basePermission+"."+argument+"'");
		add(commandsMap, ISO639_2B.GER, "  Suggestion: '"+suggestion+"'");
		add(commandsMap, ISO639_2B.GER, "  CommandString: '"+commandString+"'");
		add(commandsMap, "  HelpInfo: ",
				"'"+helpInfoGerman+"'",
				"'"+helpInfoEnglish+"'");
	}
	
	public void initLanguage() //INFO:Languages
	{
		add(languageMap, "GeneralError: ",
				"'&cGenereller Fehler!'",
				"'&cGeneral Error!'");
		add(languageMap, "InputIsWrong: ",
				"'&cDeine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!'",
				"'&cYour input is incorrect! Click here on the text to get more information!'");
		add(languageMap, "NoPermission: ",
				"'&cDu hast dafür keine Rechte!'",
				"'&cYou have no rights for this!'");
		add(languageMap, "PlayerNotExist: ",
				"'&cDer Spieler existiert nicht!'",
				"'&cThe player does not exist!'");
		add(languageMap, "PlayerNotOnline: ",
				"'&cDer Spieler ist nicht online!'",
				"'&cThe player is not online!'");
		add(languageMap, "NotNumber: ",
				"'&cEiner oder einige der Argumente muss eine Zahl sein!'",
				"'&cOne or some of the arguments must be a number!'");
		
		add(languageMap, "BaseInfo:");
		add(languageMap, "  Headline: ",
				"'&e=====&7[&cSimpleChatChannels&7]&e====='",
				"'&e=====&7[&cSimpleChatChannels&7]&e====='");
		add(languageMap, "  Next: ",
				"'&e&nnächste Seite &e==>'",
				"'&e&nnext page &e==>'");
		add(languageMap, "  Past: ",				
				"'&e<== &nvorherige Seite'",
				"'&e<== &npast page'");
		
		add(languageMap, "JoinListener: ");
		add(languageMap, "  Comma: ",
				"'&b, '",
				"'&b, '");
		add(languageMap, "  YouMuted: ",
				"'&cDu bist zurzeit gemutet!'",
				"'&cYou are muted!'");
		add(languageMap, "  Pretext: ",
				"'&bAktive Channels: '",
				"'&bActive Channels: '");
		add(languageMap, "  Spy: ",
				"'&4Spy",
				"'&4Spy");
		add(languageMap, "  Join: ",
				"'&7[&a+&7] &e%player%'",
				"'&7[&a+&7] &e%player%'");
		add(languageMap, "  HasNewMail: ",
				"'&eDu hast &f%count% &eneue Mails!'",
				"'&eYou have &f%count% &enew mails!'");
		add(languageMap, "  Welcome: ",
				"'&9.:|°*`&bWillkommen %player%&9´*°|:.'",
				"'&9.:|°*`&bWelcome %player%&9´*°|:.'");
		add(languageMap, "  Leave: ",
				"'&7[&c-&7] &e%player%'",
				"'&7[&c-&7] &e%player%'");
		
		//INFO:ChatListener
		add(languageMap, "ChatListener:");
		add(languageMap, "  NoChannelIsNullChannel: ",
				"'&cDeine Chateingabe kann in kein Channel gepostet werden, da kein Channel passt und auch kein Channel ohne Eingangssymbol existiert!'",
				"'&cYour chat entry canot be posted in any channel, because no channel fits and also no channel exists without an entry symbol!'");
		add(languageMap, "  NotATemporaryChannel: ", 
				"'&cDu bist in keinem Temporären Channel!'",
				"'&cYou are not in a temporary channel!'");
		add(languageMap, "  NotAPermanentChannel: ", 
				"'&cDu bist in keinem Permanenten Channel!'",
				"'&cYou are not in a permanent channel!'");
		add(languageMap, "  SymbolNotKnow: ",
				"'&cDer Permanente Channel &c%symbol% &cexistiert nicht.'",
				"'&cThe &f%symbol% &cpermanent channel does not exist.'");
		add(languageMap, "  ChannelIsOff: ", 
				"'&7Du hast diesen Channel ausgeschaltet. Bitte schalte ihn zum Benutzen wieder an.'",
				"'&7You have turned off this channel. Please turn it on again to use it.'");
		add(languageMap, "  ContainsBadWords: ", 
				"'&cEiner deiner geschriebenen Wörter ist im Wortfilter enthalten, bitte unterlasse solche Ausdrücke!'",
				"'&cOne of your written words is included in the word filter, please refrain from such expressions!'");
		add(languageMap, "  YouAreMuted: ", 
				"'&cDu bist für &f%time% &cgemutet!'",
				"'&cYou are muted for &f%time%&c!'");
		add(languageMap, "  PleaseWaitALittle: ", 
				"'&cBitte warte noch bis &f%time%&c, bis du in im Channel &r%channel% &cwieder etwas schreibst.'",
				"'&cPlease wait until &f%time%&c to write something again in the channel &r%channel%&c.'");
		add(languageMap, "  PleaseWaitALittleWithSameMessage: ", 
				"'&cBitte warte noch bis &f%time%&c, bis du in im Channel &r%channel% &cwieder die selbe Nachricht schreibst.'",
				"'&cPlease wait until &f%time% &cto write again the same message in the channel &r%channel%&c.'");
		add(languageMap, "  PlayerIgnoreYou: ", 
				"'&cDer Spieler &f%player% &cignoriert dich!'",
				"'&cThe player &f%player% &cignores you!'");
		add(languageMap, "  PlayerIgnoreYouButYouBypass: ", 
				"'&cDer Spieler &f%player% &cignoriert dich, jedoch konntest du das umgehen!'",
				"'&cThe &f%player% &cignores you, however you were able to bypass that!'");
		add(languageMap, "  PlayerHasPrivateChannelOff: ", 
				"'&cDer Spieler &f%player% &chat privat Nachrichten deaktiviert!'",
				"'&cThe player &f%player% &chas private messaging disabled!'");
		add(languageMap, "  StringTrim: ", 
				"'&cBitte schreibe Nachrichten mit Inhalt.'",
				"'&cPlease write messages with content.'");
		add(languageMap, "  ItemIsMissing: ", 
				"'&7[&fNicht gefunden&7]'",
				"'&7[&fNot found&7]'");
		add(languageMap, "  PrivateChatHove: r",
				"'&dKlick hier um im Privaten mit &f%player% &dzu schreiben.'",
				"'&dClick here to write in private with &f%player% &d.'");
		add(languageMap, "  ChannelHover: ",
				"%channelcolor%Klick hier um im %channel% Channel zu schreiben.'",
				"%channelcolor%Click here to write in the %channel% channel.'");
		add(languageMap, "  CommandRunHover: ",
				"'&4Klick hier um den Befehl auszuführen.'",
				"'&4Click here to execute the command.'");
		add(languageMap, "  CommandSuggestHover: ",
				"'&eKlick hier um den Befehl in der Chatzeile zu erhalten.'",
				"'&eClick here to get the command in the chat line.'");
		add(languageMap, " Website:");
		add(languageMap, "    Replacer: ", 
				"'&fWeb&7seite",
				"'&fweb&7site");
		add(languageMap, "    NotAllowReplacer: ",
				"'&f[&7Zensiert&f]'",
				"'&f[&7Censord&f]'");
		add(languageMap, "    Hover: ", 
				"'&eKlicke hier um diese Webseite zu öffnen.~!~&b'",
				"'&eClick here to open this web page.~!~&b'");
		add(languageMap, "    NotAllowHover: ", 
				"'&cIn diesem Channel ist das Posten von Webseiten nicht erlaubt.'",
				"'&cPosting web pages is not allowed in this channel.'");
		add(languageMap, "  Mention:");
		add(languageMap, "    YouAreMentionHover: ", 
				"'&eDu wurdest von &f%player% &eerwähnt!'",
				"'&eYou have been &f%player% &mentioned!'");
		add(languageMap, "  Emoji:");
		add(languageMap, "    Hover: ", 
				"'&eDieses Emoji wurde mit &f%emoji% &egeneriert!'",
				"'&eThis emoji was generated with &f%emoji%&e!'");
		//INFO:MSG
		add(languageMap, "CmdMsg:");
		add(languageMap, "  PrivateChannelsNotActive: ", 
				"'&cDer PrivateChannel ist global deaktiviert!'",
				"'&cThe PrivateChannel is globally disabled!'");
		add(languageMap, "  PleaseEnterAMessage: ", 
				"'&cBitte schreibe eine Nachricht mit Inhalt!'",
				"'&cPlease write a message with content!'");
		add(languageMap, "  YouHaveNoPrivateMessagePartner: ", 
				"'&cDu hast mit keinem Spieler geschrieben!'",
				"'&cYou have not written with any player!'");
		//INFO:Editor
		add(languageMap, "CmdEditor:");
		add(languageMap, "  Active: ", 
				"'&eDer ChatEditor ist aktive. &cDu kannst nun nicht mehr am normalen Chat teilnehmen.'",
				"'&eThe ChatEditor is active. &cYou can no longer participate in the normal chat.'");
		add(languageMap, "  Deactive: ", 
				"'&eDer ChatEditor ist deaktive. &aDu kannst nun am normalen Chat teilnehmen.'",
				"'&eThe ChatEditor is deactive. &aYou can now participate in the normal chat.'");
		
		//INFO:Mail
		add(languageMap, "CmdMail:");
		add(languageMap, "  Base:");
		add(languageMap, "    NoUnreadMail: ", 
				"'&cDu hast keine ungelesenen Mails!'",
				"'&cYou have no unread mails!'");
		add(languageMap, "    Read:");
		add(languageMap, "      Click: ", 
				"'&7[&bRead&7]'",
				"'&7[&bRead&7]'");
		add(languageMap, "      Hover: ", 
				"'&eKlick hier um die Mail zu lesen.'",
				"'&eClick here to read the mail.'");
		add(languageMap, "    SendPlus:");
		add(languageMap, "      Click: ", 
				"'&7[&cReply&7]'",
				"'&7[&cReply&7]'");
		add(languageMap, "      Hover: ", 
				"'&eKlick hier um eine Antwort an alle, Verfasser sowie CC, zu schreiben.'",
				"'&eClick here to write a reply to all, authors as well as CC.'");
		add(languageMap, "    SendMinus:");
		add(languageMap, "      Click: ", 
				"'&7[&cReply&4All&7]'",
				"'&7[&cReply&4All&7]'");
		add(languageMap, "      Hover: ", 
				"'&eKlick hier um eine Antwort nur an Verfasser zu schreiben.'",
				"'&eClick here to write a reply to author only.'");
		add(languageMap, "    Forward:");
		add(languageMap, "      Click", 
				"'&7[&dFwd&7]",
				"'&7[&dFwd&7]");
		add(languageMap, "      Hover: ", 
				"'&eKlick hier um die Mail weiterzuleiten.'",
				"'&eClick here to forward the mail.'");
		add(languageMap, "    Subject:");
		add(languageMap, "      Text: ", 
				" &6{&f%sender%&6} &d>> &r%subject%'",
				" &6{&f%sender%&6} &d>> &r%subject%'");
		add(languageMap, "      Hover: ", 
				"'&eGesendet am &f%sendeddate%~!~&cCC: &f%cc%'",
				"'&eSended on the &f%sendeddate%~!~&cCC: &f%cc%'");
		add(languageMap, "    Headline: ", 
				"'&e===== &b%mailscount% &fUngelesene Nachrichten&e====='",
				"'&e===== &b%mailscount% &fUnreaded messages&e====='");
		//Forward
		add(languageMap, "  Forward:");
		add(languageMap, "    CCHasAlreadyTheMail: ", 
				"'&cDer Spieler hat diese Mail schon bekommen!'",
				"'&cThe player has already received this mail!'");
		add(languageMap, "  Send:");
		add(languageMap, "    HasNewMail: ", 
				"'&7[&bMail&7] &eDer Spieler &f%player% &ehat dir eine Mail weitergeleitet!'",
				"'&7[&bMail&7] &eThe &f%player% &ehas forwarded you a mail!'");
		//LastMails
		add(languageMap, "  LastReceivedMails:");
		add(languageMap, "    Headline: ", 
				"'&e=====&cSeite %page% &fder letzten empfangenen Mails von &b%player%&e====='",
				"'&e=====&cSeite %page% &fthe last received mails von &b%player%&e====='");
		add(languageMap, "  LastSendedMails:");
		add(languageMap, "    Headline: ", 
				"'&e=====&cSeite %page% &fder letzten gesendeten Mails von &b%player%&e====='",
				"'&e=====&cSeite %page% &fthe last sended mails von &b%player%&e====='");
		//Read
		add(languageMap, "    MailNotExist: ", 
				"'&cDiese Mail existiert nicht!'",
				"'&cThis mail does not exist!'");
		add(languageMap, "    CannotReadOthersMails: ", 
				"'&cDu darfst diese Mail nicht lesen, das sie nicht für dich adressiert ist!'",
				"'&cYou must not read this mail, it is not addressed to you!'");
		add(languageMap, "    NoChannelIsNullChannel: ",
				"'&cDeine Mail kann nicht verarbeitet werden, da der Channel, welche für das Verarbeiten der Mailnachricht nicht existiert!'",
				"'&cYour mail cannot be processed, because the channel which is used for processing the mail message does not exist!'");
		add(languageMap, "    Headline: ", 
				"'&e==========&7[&cMail &f%id%&7]&e=========='",
				"'&e==========&7[&cMail &f%id%&7]&e=========='");
		add(languageMap, "    Sender: ", 
				"'&cVon: &f%sender%'",
				"'&cFrom: &f%sender%'");
		add(languageMap, "    CC: ", 
				"'&cCC: &7[&f%cc%&7]'",
				"'&cCC: &7[&f%cc%&7]'");
		add(languageMap, "    Subject: ", 
				"'&cBetreff: &r%subject%'",
				"'&cSubject: &r%subject%'");
		add(languageMap, "    Bottomline: ", 
				"'&e==========&7[&cMail Ende&7]&e=========='",
				"'&e==========&7[&cMail End&7]&e=========='");
		//Send
		add(languageMap, "  Send:");
		add(languageMap, "    PlayerNotExist: ", 
				"'&cEiner der angegebenen Empfänger existiert nicht!'",
				"'&cOne of the specified recipients does not exist!'");
		add(languageMap, "    OneWordMinimum: ", 
				"'&cBitte gebe mindestens 1 Wort als Nachricht an!'",
				"'&cPlease enter at least 1 word as message!'");
		add(languageMap, "    Sended: ", 
				"'&eDu hast eine Mail geschrieben.!'",
				"'&eYou have written an mail!'");
		add(languageMap, "    SendedHover: ", 
				"'&eBetreff: &r%subject%~!~&cCC: &r%cc%'",
				"'&eSubject: &r%subject%~!~&cCC: &r%cc%'");
		add(languageMap, "    HasNewMail: ", 
				"'&7[&bMail&7] &eDu hast eine neue Mail!'",
				"'&7[&bMail&7] &eYou have a new mail!'");
		add(languageMap, "    Hover: ", 
				"'&eKlick auf die Nachricht um all deine neuen Mails zu sehen!'",
				"'&eClick on the message to see all your new mails!'");
		
		//INFO:Scc
		add(languageMap, "CmdScc:");
		add(languageMap, "  OtherCmd: ", 
				"'&cBitte nutze den Befehl, mit einem weiteren Argument aus der Tabliste!'",
				"'&cPlease use the command, with another argument from the tab list!'");
		add(languageMap, "  UsedChannelForBroadCastDontExist: ", 
				"'&cDen in der config.yml gewählten Channel für einen Broadcast existiert nicht!'",
				"'&cThe channel selected in config.yml for a broadcast does not exist!'");
		//Book
		add(languageMap, "  Book:");
		add(languageMap, "    IsNotABook: ", 
				"'&cDas Item ist kein signiertes Buch!'",
				"'&cThe item is not a signed book!'");
		//Broadcast
		add(languageMap, "  Broadcast:");
		add(languageMap, "    Intro: ", 
				"'&7[&cINFO&7] &r",
				"'&7[&cINFO&7] &r");
		//Channel
		add(languageMap, "  Channel:");
		add(languageMap, "    ChannelDontExist: ", 
				"'&cDer angegeben Channel existiert nicht!'",
				"'&cThe specified channel does not exist!'");
		add(languageMap, "    UsedChannelDontExist: ", 
				"'&cDu kannst den angegeben Channel nicht ändern, da du in diesen garnicht schreiben darfst!'",
				"'&cYou can not change the specified channel, because you are not allowed to write in it!'");
		add(languageMap, "    Active: ", 
				"'&eDu hast den Channel &a%channel% &eangeschaltet!'",
				"'&eYou have switched on the &a%channel% &echannel!'");
		add(languageMap, "    Deactive: ", 
				"'&eDu hast den Channel &c%channel% &eausgeschaltet!'",
				"'&eYou have turned off the &c%channel% &echannel!'");
		
		add(languageMap, "  ChannelGui:");
		add(languageMap, "    InvTitle: ", 
				"'§c%player% §eChannels'",
				"'§c%player% §eChannels'");
		//Ignore
		add(languageMap, "  Ignore:");
		add(languageMap, "    Active: ", 
				"'&eDu ignorierst nun den Spieler &c%player%&e!'",
				"'&eYou now ignore the player &c%player%&e!'");
		add(languageMap, "    Deactive: ", 
				"'&eDu ignorierst nun nicht mehr den Spieler &a%player%&e!'",
				"'&eYou are now no longer ignoring the player &a%player%&e!'");
		add(languageMap, "    NoOne: ", 
				"'&eDu ignorierst keinen Spieler!'",
				"'&eYou dont ignore any player!'");
		add(languageMap, "    Hover: ", 
				"'&eKlick hier um den Spieler nicht mehr zu ignorieren!'",
				"'&eClick here to stop ignoring the player!'");
		add(languageMap, "    Headline: ", 
				"'&e===&bIgnorier Liste von &f%player%&e==='",
				"'&e===&bIgnore list from &f%player%&e==='");
		
		//Mute
		add(languageMap, "  Mute:");
		add(languageMap, "    YouHaveBeenMuted: ", 
				"'&cDu wurdest bis zum &f%time% &cgemutet!'",
				"'&cYou have been muted to &f%time%&c!'");
		add(languageMap, "    YouhaveMuteThePlayer: ", 
				"'&eDu hast &c%player% &ebis zum &f%time% &egemutet!'",
				"'&eYou have muted &c%player% &eto the &f%time%&e!'");
		add(languageMap, "    PlayerMute: ", 
				"'&eDer Spieler &c%target% &ewurden von &f%player% &ebis zum &f%time% &egemutet!'",
				"'&eThe player &c%target% &ehas been muted from &f%player% &eto &f%time%&e!'");
		add(languageMap, "    YouHaveBeenUnmute: ", 
				"'&eDu kannst dich wieder am Chat beteiligen!'",
				"'&eYou can join the chat again!'");
		add(languageMap, "    YouHaveUnmute: ", 
				"'&eDu hast den &f%player% &eunmutet!'",
				"'&eYou have the &f%player% &eunmuted!'");
		add(languageMap, "    PlayerUnmute: ", 
				"'&eDer Spieler &f%player% &ekann wieder reden.'",
				"'&eThe &f%player%&ecan talk again.'");
		add(languageMap, "  Performance:");
		add(languageMap, "    Headline: ", 
				"'&e=====&7[&cScc MySQLPerformance&7]&e====='",
				"'&e=====&7[&cScc MysqlPerformance&7]&e====='");
		add(languageMap, "    Subline: ", 
				"'&eZeitraum von &f%begin% &ebis &f%end%'",
				"'&eTime &f%begin% &etoo &f%end%'");
		add(languageMap, "    Text: ", 
				"'&4%server% &e>> &6Inserts:&f%insert% &bUpdates:&f%update% &cDeletes:&f%delete% &eReads:&f%read%'",
				"'&4%server% &e>> &6Inserts:&f%insert% &bUpdates:&f%update% &cDeletes:&f%delete% &eReads:&f%read%'");
		
		//Option
		add(languageMap, "  Option:");
		add(languageMap, "    Channel:");
		add(languageMap, "      Active: ", 
				"'&eDu siehst nun alle Aktiven Channels beim Login.'",
				"'&eYou will now see all active channels when you log in.'");
		add(languageMap, "      Deactive: ", 
				"'&eDu siehst nun nicht mehr alle Aktiven Channels beim Login.'",
				"'&eYou no longer see all active channels when you log in.'");
		add(languageMap, "    Join:");
		add(languageMap, "      Active: ", 
				"'&eDu siehst nun die Nachricht, wenn Spieler den Server verlassen oder joinen.'",
				"'&eYou will now see the message when players leave or join the server.'");
		add(languageMap, "      Deactive: ", 
				"'&eDu siehst nun die Nachricht nicht mehr, wenn Spieler den Server verlassen oder joinen.'",
				"'&eYou no longer see the message when players leave or join the server.'");
		add(languageMap, "    Spy:");
		add(languageMap, "      Active: ", 
				"'&eDu siehst nun alle Chatnachrichten, die dir sonst für dich verborgen wären.'",
				"'&eYou will now see all chat messages that would otherwise be hidden to you.'");
		add(languageMap, "      Deactive: ", 
				"'&eDu siehst nun nur noch die Chatnachrichten, wozu du auch berechtig bist.'",
				"'&eYou will now only see the chat messages that you are authorized to see.'");
		//ItemReplacer
		add(languageMap, "  Item:");
		add(languageMap, "    InvTitle: ", 
				"'§c%player% §eReplacer §6Items'",
				"'§c%player% §eReplacer §6Items'");
		add(languageMap, "    YouCannotSaveItems: ", 
				"'&cDu kannst keine Items vorspeicher!'",
				"'&cYou can't pre-store items!'");
		add(languageMap, "    Rename:");
		add(languageMap, "      NotDefault: ", 
				"'&cDer alte oder neue Name darf nicht &fdefault &cheißen!'",
				"'&cThe old or new name must not be &fdefault&c!'");
		add(languageMap, "      NameAlreadyExist: ", 
				"'&cDer Name ist schon vergeben!'",
				"'&cThe name is already taken!'");
		add(languageMap, "      Renamed: ", 
				"'&eDas Item mit dem Name &f%oldname% &ewurde in &f%newname% &eumbenannt!'",
				"'&eThe item with the name &f%oldname% &has been renamed to &f%newname%!'");
		add(languageMap, "    Replacers:");
		add(languageMap, "      ListEmpty: ", 
				"'&cDu hast keine ItemReplacer!'",
				"'&cYou have no ItemReplacer!'");
		add(languageMap, "      Headline: ", 
				"'&e=====&7[&bItemReplacer&7]&e====='",
				"'&e=====&7[&bItemReplacer&7]&e====='");
		
		//INFO:PermanentChannel
		add(languageMap, "  PermanentChannel:");
		add(languageMap, "    YouAreNotInAChannel: ", 
				"'&cDu bist in keinem permanenten Channel!'",
				"'&cYou are not in a permanent channel!'");
		add(languageMap, "    YouAreNotTheOwner: ", 
				"'&cDu bist nicht der Ersteller in diesem permanenten Channel!'",
				"'&cYou are not the creator in this permanent channel!'");
		add(languageMap, "    YouAreNotTheOwnerOrVice: ", 
				"'&cDu bist weder der Ersteller noch ein Stellvertreter in diesem permanenten Channel!'",
				"'&cYou are neither the creator nor an vice in this permanent channel!'");
		add(languageMap, "    NotAChannelMember: ", 
				"'&cDer angegebene Spieler ist nicht Mitglied im permanenten Channel!'",
				"'&cThe specified player is not a member of the permanent channel!'");
		//Ban
		add(languageMap, "    Ban:");
		add(languageMap, "      ViceCannotBanCreator: ", 
				"'&cDu kannst als Stellvertreter den Ersteller nicht bannen!'",
				"'&cYou cant ban the creator as a vice!'");
		add(languageMap, "      OwnerCantSelfBan: ", 
				"'&cDer Ersteller kann nicht gebannt werden!'",
				"'&cThe creator can not be banned!'");
		add(languageMap, "      AlreadyBanned: ", 
				"'&cDer Spieler ist schon auf der gebannt!'",
				"'&cThe player is already on the banned!'");
		add(languageMap, "      YouHasBanned: ", 
				"'&eDu hast den Spieler &f%player% &eaus dem &5Perma&fnenten &eChannel verbannt.'",
				"'&eYou have banned the &f%player% &efrom the &5perma&fnent &eChannel.'");
		add(languageMap, "      YourWereBanned: ", 
				"'&cDu wurdest vom Permanenten Channel &r%channel% &cverbannt!'",
				"'&cYou were banned from the Permanent Channel &r%channel%&c!'");
		add(languageMap, "      PlayerWasBanned: ", 
				"'&eDer Spieler &f%player% &ewurde aus dem &5Perma&fnenten &eChannel verbannt.'",
				"'&eThe player &f%player% &ehas been banned from the &5perma&fnent &eChannel.'");
		add(languageMap, "    Unban:");
		add(languageMap, "      PlayerNotBanned: ", 
				"'&cDer Spieler ist nicht gebannt!'",
				"'&cThe player is not banned!'");
		add(languageMap, "      YouUnbanPlayer: ", 
				"'&eDu hast &f%player% &efür den &5Perma&fnenten &eChannel entbannt!'",
				"'&eYou have unbanned &f%player% &efor the &5perma&fnent &eChannel!'");
		add(languageMap, "      PlayerWasUnbanned: ", 
				"'&eDer Spieler &f%player% &ewurde für den &5Perma&fnenten &eChannel &r%channel% &r&eentbannt.'",
				"'&eThe player &f%player% &ehas been banned for the &5perma&fnent &eChannel &r%channel%&e.'");
		//ChangePassword
		add(languageMap, "    ChangePassword:");
		add(languageMap, "      Success: ", 
				"'&eDer Spieler &f%player% &ewurde für den &5Perma&fnenten &eChannel &r%channel% &r&eentbannt.'",
				"'&eThe player &f%player% &ehas been banned for the &5perma&fnent &eChannel &r%channel%&e.'");
		//Channels
		add(languageMap, "    Channels:");
		add(languageMap, "      Headline: ", 
				"'&e=====&5[&5Perma&fnente &fChannels&5]&e====='",
				"'&e=====&5[&5Perma&fnente &fChannels&5]&e====='");
		//ChatColor
		add(languageMap, "    ChatColor:");
		add(languageMap, "      NewColor: ", 
				"'&eDie Farben vom Chat des Channels &r%channel% &ewurden in &f%color%Beispielnachricht &r&egeändert.'",
				"'&eThe colors of the channel chat &r%channel% &ehave been changed to &r%color%example message &r&e.'");
		//Create
		add(languageMap, "    Create:");
		add(languageMap, "      ChannelNameAlreadyExist: ", 
				"'&cDieser Name existiert bereits für einen Permanente Channel!'",
				"'&cThis name already exists for a permanent channel!'");
		add(languageMap, "      MaximumAmount: ", 
				"'&cDu hast schon die maximale Anzahl von Permanenten Channels erstellt. Lösche vorher einen von deinen Permanenten Channels, um einen neuen zu erstellen!'",
				"'&cYou have already created the maximum number of permanent channels. Delete one of your permanent channels before to create a new one!'");
		add(languageMap, "      ChannelCreateWithoutPassword: ", 
				"'&eDu hast den &5Perma&fnenten &eChannel &r%channel%&r &eerstellt! Zum Schreiben am Anfang &f%symbol% &enutzen.'",
				"'&eYou have created the &5perma&fnent &eChannel &r%channel%&r! To write at the beginning &f%symbol% &use.'");
		add(languageMap, "      ChannelCreateWithPassword: ", 
				"'&eDu hast den &5Perma&fnenten &eChannel &r%channel%&r &emit dem Passwort &f%password% &eerstellt! Zum Schreiben am Anfang &f%symbol% &enutzen.'",
				"'&eYou have &created the &5perma&fnent &eChannel &r%channel%&r &with the password &f%password%! To write at the beginning &f%symbol% &use.'");
		//Delete
		add(languageMap, "    Delete:");
		add(languageMap, "      .Confirm: ", 
				"'&cBist du sicher, dass du den Permanenten Channel &r%channel% &r&clöschen willst? Wenn ja, klicke auf diese Nachricht.'",
				"'&cAre you sure you want to delete the Permanent Channel &r%channel%&c? If yes, click on this message.'");
		add(languageMap, "      .Deleted: ", 
				"'&cDer Permanente Channel &r%channel% &r&cwurde von %player% gelöscht. Alle Mitglieder verlassen somit diesen Channel.'",
				"'&cThe permanent channel &r%channel% &r&chas been deleted by %player%. All members leave this channel.'");
		//Info
		add(languageMap, "    Info:");
		add(languageMap, "      Headline: ", 
				"'&e=====&5[&5Perma&fnenter &fChannel &r%channel%&r&5]&e====='",
				"'&e=====&5[&5Perma&fnent &fChannel &r%channel%&r&5]&e====='");
		add(languageMap, "      ID: ", 
				"'&eChannel ID: &f%id%'",
				"'&eChannel ID: &f%id%'");
		add(languageMap, "      Creator: ", 
				"'&eChannel Ersteller: &f%creator%'",
				"'&eChannel creator: &f%creator%'");
		add(languageMap, "      Vice: ", 
				"'&eChannel Stellvertreter: &f%vice%'",
				"'&eChannel vice: &f%vice%'");
		add(languageMap, "      Members: ", 
				"'&eChannel Mitglieder: &f%members%'",
				"'&eChannel members: &f%members%'");
		add(languageMap, "      Password: ", 
				"'&eChannel Passwort: &f%password%'",
				"'&eChannel password: &f%password%'");
		add(languageMap, "      Symbol: ", 
				"'&eChannel Symbol: &f%symbol%'",
				"'&eChannel symbol: &f%symbol%'");
		add(languageMap, "      ChatColor: ", 
				"'&eChannel Chat Farben: &f%color% Beispielnachricht'",
				"'&eChannel chat colors: &f%color% example message'");
		add(languageMap, "      NameColor: ", 
				"'&eChannel Farben: &f%color%'",
				"'&eChannel colors: &f%color%'");
		add(languageMap, "      Banned: ", 
				"'&eChannel Gebannte Spieler: &f%banned%'",
				"'&eChannel banned players: &f%banned%'");
		//Inherit
		add(languageMap, "    Inherit:");
		add(languageMap, "      NewCreator: ", 
				"'&eIm &5Perma&fnenten &eChannel &r%channel% &r&ebeerbt der Spieler &a%creator% &eden Spieler &c%oldcreator% &eals neuer Ersteller des Channels.'",
				"'&eIn the &5perma&fnent &eChannel &r%channel% &r&einherits the player &a%creator% &the player &c%oldcreator% &eas the new creator of the channel.'");
		//Invite
		add(languageMap, "    Invite:");
		add(languageMap, "      Cooldown: ", 
				"'&cDu hast schon in der letzten Zeit jemanden eingeladen! Bitte warte bis %time%, um die nächsten Einladung zu verschicken!'",
				"'&cYou have already invited someone in the last time! Please wait until %time% to send the next invitation!'");
		add(languageMap, "      SendInvite: ", 
				"'&eDu hast den Spieler &6%target% &ein den &5Perma&fnenten &eChannel &r%channel% &r&aeingeladen.'",
				"'&eYou have invited the player &6%target% &einto the &5perma&fnent &eChannel &r%channel%&r&e.'");
		add(languageMap, "      Invitation: ", 
				"'&eDu wurdest vom Spieler &6%player% &ein den &5Perma&fnenten &eChannel &r%channel% &r&aeingeladen. Klicke auf die Nachricht zum Betreten des Channels.'",
				"'&eYou have been invited by the &6%player% &into the &5perma&fnent &eChannel &r%channel%&r&e. Click on the message to enter the channel.'");
		//Join
		add(languageMap, "    Join:");
		add(languageMap, "      UnknownChannel: ", 
				"'&cEs gibt keinen Permanenten Channel mit dem Namen &f%name%&c!'",
				"'&cThere is no permanent channel with the name &f%name%&c!'");
		add(languageMap, "      Banned: ", 
				"'&cDu bist in diesem Permanenten Channel gebannt und darfst nicht beitreten!'",
				"'&cYou are banned in this permanent channel and are not allowed to join!'");
		add(languageMap, "      AlreadyInTheChannel: ", 
				"'&cDu bist schon diesem Permanenten Channel beigetreten!'",
				"'&cYou have already joined this permanent channel!'");
		add(languageMap, "      ChannelHasPassword: ", 
				"'&cDer Permanente Channel hat ein Passwort, bitte gib dieses beim Beitreten an!'",
				"'&cThe Permanent Channel has a password, please enter it when joining!'");
		add(languageMap, "      PasswordIncorrect: ", 
				"'&cDas angegebene Passwort ist nicht korrekt!'",
				"'&cThe specified password is not correct!'");
		add(languageMap, "      ChannelJoined: ", 
				"'&eDu bist dem &5Perma&fnenten &eChannel &r%channel%&r &abeigetreten&e! ChannelSymbol: &r%symbol%'",
				"'&eYou have joined the &5Perma&fnent &eChannel &r%channel%&r&e! ChannelSymbol: &r%symbol%'");
		add(languageMap, "      PlayerIsJoined: ", 
				"'&eSpieler &f%player% &eist dem &5Perma&fnenten &eChannel &r%channel% &r&ebeigetreten!'",
				"'&ePlayer &f%player% &ehas joined the &5Perma&fnent &eChannel &r%channel%&r&e!'");
		//Kick
		add(languageMap, "    Kick:");
		add(languageMap, "      ViceCannotKickCreator: ", 
				"'&cDu kannst als Stellvertreter den Ersteller nicht kicken!'",
				"'&cYou can't kick the creator as a vice!'");
		add(languageMap, "      CannotSelfKick: ", 
				"'&cDu kannst dich nicht kicken!'",
				"'&cYou can't kick yourself!'");
		add(languageMap, "      YouWereKicked: ", 
				"'&cDu wurdest aus dem Permanenten &eChannel &r%channel%&r &cgekickt!'",
				"'&cYou have been kicked out of the Permanent &eChannel &r%channel%&r&c!'");
		add(languageMap, "      YouKicked: ", 
				"'&eDu hast &f%player% &eaus dem &5Perma&fnenten &eChannel &r%channel%&r &egekickt!'",
				"'&eYou have &f%player% &e kicked out of the &5perma&fnent &eChannel &r%channel%&r &egekickt!'");
		add(languageMap, "      KickedSomeone: ", 
				"'&eDer Spieler &f%player% &ewurde aus dem &5Perma&fnenten &eChannel &r%channel%&r &egekickt!'",
				"'&The player &f%player% &has been kicked out of the &5perma&fnent &eChannel &r%channel%&r &egekickt!'");
		//Leave
		add(languageMap, "    Leave:");
		add(languageMap, "      Confirm: ", 
				"'&c&lAchtung! &r&cBist du sicher, dass du den Channel verlassen willst? Wenn der Ersteller den Permanenten Channel verlässt, wird dieser gelöscht! Bitte bestätigen mit dem Klick auf diese Nachricht.'",
				"'&c&lAttention! &r&cAre you sure you want to leave the channel? If the creator leaves the permanent channel, it will be deleted! Please confirm and just click on this message.'");
		add(languageMap, "      CreatorLeft: ", 
				"'&cDer Ersteller hat den Channel verlassen und ihn somit aufgelöst. Alle Mitglieder haben somit den Permanenten Channel &r%channel%&r &cverlassen!'",
				"'&cThe creator has left the channel and thus dissolved it. All members have left the permanent channel &r%channel%&r &c!'");
		add(languageMap, "      YouLeft: ", 
				"'&eDu hast den &5Perma&fnenten &eChannel &r%channel%&r &everlassen!'",
				"'&eYou have left the &5perma&fnent &eChannel &r%channel%&r &ever!'");
		add(languageMap, "      PlayerLeft: ", 
				"'&cDer Spieler &f%player% &chat den permanenten Channel &r%channel%&r &cverlassen!'",
				"'&cThe player &f%player% &chas left the permanent channel &r%channel%&r &c!'");
		//NameColor
		add(languageMap, "    NameColor:");
		add(languageMap, "      NewColor: ", 
				"'&eDie Farben vom Namen des &5Perma&fnenten Channels &r%channel%&r &ewurden in &f%color%Beispielnachricht &r&egeändert.'",
				"'&The colors from the name of the &5perma&fnent channel &r%channel%&r &have been changed to &f%color%example message &r&e.'");
		//Player
		add(languageMap, "    Player:");
		add(languageMap, "      Headline: ", 
				"'&e=====&5[&5Perma&fnente &fChannels von &6%player%&5]&e====='",
				"'&e=====&5[&5Perma&fnent &fchannels from &6%player%&5]&e====='");
		add(languageMap, "      Creator: ", 
				"'&eIst Ersteller von: &r%creator%'",
				"'&eIs creator from: &r%creator%'");
		add(languageMap, "      Vice: ", 
				"'&eIst Vertreter bei: &r%vice%'",
				"'&eIs vice in: &r%vice%'");
		add(languageMap, "      Member: ", 
				"'&eIst Mitglied bei: &r%member%'",
				"'&eIs member in: &r%member%'");
		add(languageMap, "      Banned: ", 
				"'&eIst Gebannt bei: &r%banned%'",
				"'&eIs banned in: &r%banned%'");
		//Rename
		add(languageMap, "    Rename:");
		add(languageMap, "      NameAlreadyExist: ", 
				"'&cEs gibt schon einen Permanenten Channel &r%channel%&r&c!'",
				"'&cThere is already a Permanent Channel &r%channel%&r&c!'");
		add(languageMap, "      Renaming: ", 
				"'&eDer &5Perma&fnente &eChannel &r%oldchannel% &r&ewurde in &r%channel% &r&eumbenannt.'",
				"'&eThe &5Perma&fnente &eChannel &r%oldchannel% &r&has been renamed &r%channel&r&e.'");
		//Symbol
		add(languageMap, "    Symbol:");
		add(languageMap, "      SymbolAlreadyExist: ", 
				"'&cDas Symbol &f%symbol% &cwird schon von dem Permanenten Channel &r%channel%&r &cbenutzt!'",
				"'&cThe symbol &f%symbol% &cis already used by the Permanent Channel &r%channel%&r&c!'");
		add(languageMap, "      NewSymbol: ", 
				"'&eFür den &5Perma&fnenten &eChannel &r%channel%&r &egibt es ein neues Symbol: &f%symbol%'",
				"'&eFor the &5perma&fnent &eChannel &r%channel%&r &there is a new symbol: &f%symbol%'");
		//Vice
		add(languageMap, "    Vice:");
		add(languageMap, "      Degraded: ", 
				"'&eDer Spieler &f%player% &ewurde zum Mitglied im &5Perma&fnenten &eChannel &r%channel%&r &cdegradiert&e!'",
				"'&The player &f%player% &has been &degraded&e to a member of the &5perma&fnent &eChannel &r%channel%&r&c!'");
		add(languageMap, "      Promoted: ", 
				"'&eDer Spieler &f%player% &ewurde zum Stellvertreter im &5Perma&fnenten &eChannel &r%channel%&r &abefördert&e!'",
				"'&Tehe player &f%player% &has been &promoted&e to vice in the &5perma&fnent &eChannel &r%channel%&r!'");
		
		//INFO:TemproraryChannel
		add(languageMap, "  TemporaryChannel:");
		add(languageMap, "    YouAreNotInAChannel: ", 
				"'&cDu bist in keinem temporären Channel!'",
				"'&cYou are not in a temporary channel!'");
		add(languageMap, "    YouAreNotTheOwner: ", 
				"'&cDu bist nicht der Ersteller in diesem temporären Channel!'",
				"'&cYou are not the creator in this temporary channel!'");
		add(languageMap, "    NotAChannelMember: ", 
				"'&cDer angegebene Spieler ist nicht Mitglied im temporären Channel!'",
				"'&cThe specified player is not a member of the temporary Channel!'");
		//Ban
		add(languageMap, "    Ban:");
		add(languageMap, "      CreatorCannotSelfBan: ", 
				"'&cDu als Ersteller kannst dich nicht selber bannen!'",
				"'&cYou as the creator can not ban yourself!'");
		add(languageMap, "      AlreadyBanned: ", 
				"'&cDer Spieler ist schon gebannt!'",
				"'&cThe player is already banned!'");
		add(languageMap, "      YouHasBanned: ", 
				"'&eDu hast den Spieler &f%player% &eaus dem &5Temp&forären &eChannel verbannt.'",
				"'&eYou have banned the &f%player% &efrom the &5temp&forary &eChannel.'");
		add(languageMap, "      YourWereBanned: ", 
				"'&cDu wurdest vom Temporären Channel &f%channel% &cverbannt!'",
				"'&cYou have been &f%channel% &cbanned from the Temporary Channel!'");
		add(languageMap, "      CreatorHasBanned: ", 
				"'&eDer Spieler &f%player% &ewurde aus dem &5temp&forären &eChannel verbannt.'",
				"'&eThe player &f%player% &has been banned from the &5temp&forary &eChannel.'");
		add(languageMap, "      PlayerNotBanned: ", 
				"'&cDer Spieler ist nicht gebannt!'",
				"'&cThe player is not banned!'");
		add(languageMap, "      YouUnbanPlayer: ", 
				"'&eDu hast &f%player% &efür den &5temp&forären &eChannel entbannt!'",
				"'&eYou have unbanned &f%player% &for the &5temp&forary &eChannel!'");
		add(languageMap, "      CreatorUnbanPlayer: ", 
				"'&eDer Spieler &f%player% &ewurde für den &5Temp&forären &eChannel entbannt.'",
				"'&The player &f%player% &ehas been unbanned for the &5temp&forary &eChannel.'");
		//ChangePassword
		add(languageMap, "    ChangePassword:");
		add(languageMap, "      PasswordChange: ", 
				"'&eDu hast das Passwort zu &f%password% &egeändert!'",
				"'&eYou have changed the password to &f%password%!'");
		//Create
		add(languageMap, "    Create:");
		add(languageMap, "      AlreadyInAChannel: ", 
				"'&cDu bist schon in dem Temporären Channel &f%channel%&c! Um einen neuen Temporären Channel zu eröffnen, müsst du den vorherigen erst schließen.'",
				"'&cYou are already in the temporary channel &f%channel%&c! To open a new temporary channel, you must first close the previous one.'");
		add(languageMap, "      ChannelCreateWithoutPassword: ", 
				"'&eDu hast den &5temp&forären &eChannel &f%channel% &eerstellt!'",
				"'&eYou have set the &5temp&for &eChannel &f%channel%&e!'");
		add(languageMap, "      ChannelCreateWithPassword: ", 
				"'&eDu hast den &5Temp&forären &eChannel &f%channel% &emit dem Passwort &f%password% &eerstellt!'",
				"'&eYou have created the &5Temp&for &eChannel &f%channel% &with the password &f%password%!'");
		//Info
		add(languageMap, "    Info:");
		add(languageMap, "      Headline: ", 
				"'&e=====&5[&5Temp&forären &fChannel &6%channel%&5]&e====='",
				"'&e=====&5[&5Temp&forary &fChannel &6%channel%&5]&e====='");
		add(languageMap, "      Creator: ", 
				"'&eChannel Ersteller: &f%creator%'",
				"'&eChannel creator: &f%creator%'");
		add(languageMap, "      Members: ", 
				"'&eChannel Mitglieder: &f%members%'",
				"'&eChannel members: &f%members%'");
		add(languageMap, "      Password: ", 
				"'&eChannel Passwort: &f%password%'",
				"'&eChannel password: &f%password%'");
		add(languageMap, "      Banned: ", 
				"'&eChannel Gebannte Spieler: &f%banned%'",
				"'&eChannel banned players: &f%banned%'");
		//Invite
		add(languageMap, "    Invite:");
		add(languageMap, "      Cooldown: ", 
				"'&cDu hast schon in der letzten Zeit jemanden eingeladen! Bitte warte etwas bis zur nächsten Einladung!'",
				"'&cYou have already invited someone in the last time! Please wait a little until the next invitation!'");
		add(languageMap, "      SendInvite: ", 
				"'&eDu hast den Spieler &6%target% &ein den &5temp&forären &eChannel &6%channel% &aeingeladen.'",
				"'&eYou have invited the player &6%target% &into the &5temp&forary &eChannel &6%channel%&e.'");
		add(languageMap, "      Invitation: ", 
				"'&eDu wurdest vom Spieler &6%player% &ein den &5temp&forären &eChannel &6%channel% &aeingeladen. Klicke auf die Nachricht zum Betreten des Channels.'",
				"'&eYou have been invited by the player &6%player% &into the &5temp&forary &eChannel &6%channel%&e. Click on the message to enter the channel.'");
		//Join
		add(languageMap, "    Join:");
		add(languageMap, "      AlreadyInAChannel: ", 
				"'&cDu bist schon in einem anderen Temporären Channel beigetreten, verlasse erst diesen!'",
				"'&cYou have already joined another temporary channel, leave this one first!'");
		add(languageMap, "      UnknownChannel: ", 
				"'&cEs gibt keinen Temporären Channel mit dem Namen &f%name%&c!'",
				"'&cThere is no temporary channel with the name &f%name%&c!'");
		add(languageMap, "      Banned: ", 
				"'&cDu bist in diesem Temporären Channel gebannt und darfst nicht beitreten!'",
				"'&cYou are banned in this temporary channel and are not allowed to join!'");
		add(languageMap, "      ChannelHasPassword: ", 
				"'&cDer Temporäre Channel hat ein Passwort, bitte gib dieses beim Beitreten an!'",
				"'&cThe Temporary Channel has a password, please enter it when joining!'");
		add(languageMap, "      PasswordIncorrect: ", 
				"'&cDas angegebene Passwort ist nicht korrekt!'",
				"'&cThe specified password is not correct!'");
		add(languageMap, "      ChannelJoined: ", 
				"'&eDu bist dem &5temp&forären &eChannel &f%channel% &abeigetreten!'",
				"'&eYou have joined the &5temp&forary &eChannel &f%channel%!'");
		add(languageMap, "      PlayerIsJoined: ", 
				"'&eSpieler &f%player% &eist dem &5temp&forären &eChannel beigetreten!'",
				"'&ePlayer &f%player% &has joined the &5temp&forary &eChannel!'");
		//Kick
		add(languageMap, "    Kick:");
		add(languageMap, "      CreatorCannotSelfKick: ", 
				"'&cDu als Ersteller kannst dich nicht kicken!'",
				"'&cYou as the creator can not kick you!'");
		add(languageMap, "      YouKicked: ", 
				"'&eDu hast &f%player% &eaus dem &5temp&forären &eChannel %channel% gekickt!'",
				"'&eYou have kicked &f%player% &eout of the &5temp&forary &eChannel %channel%!'");
		add(languageMap, "      YouWereKicked: ", 
				"'&cDu wurdest aus dem temporären Channel &f%channel% &cgekickt!'",
				"'&cYou have been kicked out of the Temporary Channel &f%channel%&c!'");
		add(languageMap, "      CreatorKickedSomeone: ", 
				"'&eDer Spieler &f%player% &ewurde aus dem &5temp&forären &eChannel %channel% gekickt!'",
				"'&The player &f%player% &ehas been kicked out of the &5temp&forär &eChannel %channel%!'");
		//Leave
		add(languageMap, "    Leave:");
		add(languageMap, "      NewCreator: ", 
				"'&eDu wurdest der neue Ersteller des &5temp&forären &eChannels &f%channel'%",
				"'&eYou became the new creator of the &5temp&forary &eChannel &f%channel%'");
		add(languageMap, "      YouLeft: ", 
				"'&eDu hast den &5temp&forären &eChannel &f%channel% &everlassen!'",
				"'&eYou have left the &5temp&forary &eChannel &f%channel% &eever!'");
		
		//UpdatePlayer
		add(languageMap, "  UpdatePlayer:");
		add(languageMap, "    IsUpdated: ", 
				"'&eDu hast den Spieler &f%player% &eneu bewerten lassen! Seine aktiven Channels sind nach seinen Permission neu eingestellt worden.'",
				"'&eYou have had the player &f%player% &revaluated! His active channels have been reset after his permission.'");
		add(languageMap, "    YouWasUpdated: ", 
				"'&eDer Spieler &f%player% &ehat deine aktiven Channels nach deinen Permission neu einstellen lassen.'",
				"'&eThe player &f%player% &ehas your active channels reset according to your permission.'");
	}
	
	//INFO:ChatTitle
	public void initChatTitle() 
	{
		add(chatTitleMap, "admin:");
		add(chatTitleMap, "  UniqueIdentifierName: ",
				"'Admin'",
				"'Admin'");
		add(chatTitleMap, "  IsPrefix: ", ISO639_2B.GER,
				true);
		add(chatTitleMap, "  InChatName: ",
				"'&7[&4Admin&7]'",
				"'&7[&4Admin&7]'");
		add(chatTitleMap, "  InChatColorCode: ",
				"'&4'",
				"'&4'");
		add(chatTitleMap, "  SuggestCommand: ", ISO639_2B.GER,
				"'/rules'");
		add(chatTitleMap, "  Hover: ", 
				"'&eDie Admins sind für die administrative Arbeit auf dem Server zuständig.~!~Für Hilfe im Spielbetrieb sind sie aber die letzte Instanz.'",
				"'&eThe admins are responsible for the administrative work on the server.~!~But for help in the game operation they are the last instance.'");
		add(chatTitleMap, "  Permission: ",
				"'scc.title.admin'",
				"'scc.title.admin'");
		add(chatTitleMap, "  Weight: ",
				1000,
				1000);
	}
	
	//INFO:Channels
	public void initChannels() 
	{
		add(channelsMap, "private:");
		add(channelsMap, "  UniqueIdentifierName: "
				, ISO639_2B.GER,
				"'Private'");
		add(channelsMap, "  Symbol: "
				, ISO639_2B.GER,
				"'/msg'");
		add(channelsMap, "  InChatName: "
				, ISO639_2B.GER,
				"'&e[Private]'");
		add(channelsMap, "  InChatColorMessage: "
				, ISO639_2B.GER,
				"'&d'");
		add(channelsMap, "  Permission: "
				, ISO639_2B.GER,
				"scc.channel.private'");
		add(channelsMap, "  JoinPart: "
				, ISO639_2B.GER,
				"'&ePrivate &7= /msg'");
		add(channelsMap, "  ChatFormat: "
				, ISO639_2B.GER,
				"'&7[%time%&7] %playername_with_prefixhighcolorcode% &e>> %other_playername_with_prefixhighcolorcode% &7: %message%'");
		add(channelsMap, "  MinimumTimeBetweenMessages: "
				, ISO639_2B.GER,
				500);
		add(channelsMap, "  MinimumTimeBetweenSameMessages: "
				, ISO639_2B.GER,
				1000);
		add(channelsMap, "  PercentOfSimiliarityOrLess: "
				, ISO639_2B.GER,
				75.0);
		add(channelsMap, "  TimeColor: "
				, ISO639_2B.GER,
				"'&7'");
		add(channelsMap, "  PlayernameCustomColor: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  OtherPlayernameCustomColor: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  SeperatorBetweenPrefix: "
				, ISO639_2B.GER,
				"' '");
		add(channelsMap, "  SeperatorBetweenSuffix: "
				, ISO639_2B.GER,
				"' '");
		add(channelsMap, "  MentionSound: "
				, ISO639_2B.GER,
				"'ENTITY_WANDERING_TRADER_REAPPEARED'");
		add(channelsMap, "  ServerConverter: ");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'proxy;&2BungeeCord;/warp spawn;&eDer Proxy ist der Verwalter aller Spigotserver.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'hub;&aHub;/warp hub;&eVom Hub kommst du zu alle~!~&eandere Server.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'nether;&cNether;/warp nether;&cDie Hölle'");				
		add(channelsMap, "  WorldConverter: ");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'spawn;&aSpawn;/warp spawn;&eVom Spawn kommst du zu alle~!~&eandere Server.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'nether;&cNether;/warp nether;&cDie Hölle'");
		add(channelsMap, "  UseColor: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseItemReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseBookReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseRunCommandReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseSuggestCommandReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseWebsiteReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseEmojiReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UsePositionReplacer: "
				, ISO639_2B.GER,
				false);
		
		add(channelsMap, "permanent:");
		add(channelsMap, "  UniqueIdentifierName: "
				, ISO639_2B.GER,
				"'Permanent'");
		add(channelsMap, "  Symbol: "
				, ISO639_2B.GER,
				"'.'");
		add(channelsMap, "  InChatName: "
				, ISO639_2B.GER,
				"'&d[%channel%&d]'");
		add(channelsMap, "  InChatColorMessage: "
				, ISO639_2B.GER,
				"'&f'");
		add(channelsMap, "  Permission: "
				, ISO639_2B.GER,
				"scc.channel.permanent'");
		add(channelsMap, "  JoinPart: "
				, ISO639_2B.GER,
				"'&dPerma&7nent &7= .'");
		add(channelsMap, "  ChatFormat: "
				, ISO639_2B.GER,
				"'&7[%time%&7] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%&7: %message%'");
		add(channelsMap, "  MinimumTimeBetweenMessages: "
				, ISO639_2B.GER,
				500);
		add(channelsMap, "  MinimumTimeBetweenSameMessages: "
				, ISO639_2B.GER,
				1000);
		add(channelsMap, "  PercentOfSimiliarityOrLess: "
				, ISO639_2B.GER,
				75.0);
		add(channelsMap, "  TimeColor: "
				, ISO639_2B.GER,
				"'&7'");
		add(channelsMap, "  PlayernameCustomColor: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  OtherPlayernameCustomColor: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  SeperatorBetweenPrefix: "
				, ISO639_2B.GER,
				"' '");
		add(channelsMap, "  SeperatorBetweenSuffix: "
				, ISO639_2B.GER,
				"' '");
		add(channelsMap, "  MentionSound: "
				, ISO639_2B.GER,
				"'ENTITY_WANDERING_TRADER_REAPPEARED'");
		add(channelsMap, "  ServerConverter: ");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'proxy;&2BungeeCord;/warp spawn;&eDer Proxy ist der Verwalter aller Spigotserver.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'hub;&aHub;/warp hub;&eVom Hub kommst du zu alle~!~&eandere Server.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'nether;&cNether;/warp nether;&cDie Hölle'");				
		add(channelsMap, "  WorldConverter: ");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'spawn;&aSpawn;/warp spawn;&eVom Spawn kommst du zu alle~!~&eandere Server.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'nether;&cNether;/warp nether;&cDie Hölle'");
		add(channelsMap, "  UseColor: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseColor: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseItemReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseBookReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseRunCommandReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseSuggestCommandReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseWebsiteReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseEmojiReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseMentionReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UsePositionReplacer: "
				, ISO639_2B.GER,
				false);
		
		add(channelsMap, "temporary:");
		add(channelsMap, "  UniqueIdentifierName: "
				, ISO639_2B.GER,
				"'Temporary'");
		add(channelsMap, "  Symbol: "
				, ISO639_2B.GER,
				"';'");
		add(channelsMap, "  InChatName: "
				, ISO639_2B.GER,
				"'&5[%channel%]'");
		add(channelsMap, "  InChatColorMessage: "
				, ISO639_2B.GER,
				"'&5'");
		add(channelsMap, "  Permission: "
				, ISO639_2B.GER,
				"scc.channel.temporary");
		add(channelsMap, "  JoinPart: "
				, ISO639_2B.GER,
				"'&5Temporary &7= ;'");
		add(channelsMap, "  ChatFormat: "
				, ISO639_2B.GER,
				"'&7[%time%&7] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%&7: %message%'");
		add(channelsMap, "  MinimumTimeBetweenMessages: "
				, ISO639_2B.GER,
				500);
		add(channelsMap, "  MinimumTimeBetweenSameMessages: "
				, ISO639_2B.GER,
				1000);
		add(channelsMap, "  PercentOfSimiliarityOrLess: "
				, ISO639_2B.GER,
				75.0);
		add(channelsMap, "  TimeColor: "
				, ISO639_2B.GER,
				"'&7'");
		add(channelsMap, "  PlayernameCustomColor: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  OtherPlayernameCustomColor: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  SeperatorBetweenPrefix: "
				, ISO639_2B.GER,
				"' '");
		add(channelsMap, "  SeperatorBetweenSuffix: "
				, ISO639_2B.GER,
				"' '");
		add(channelsMap, "  MentionSound: "
				, ISO639_2B.GER,
				"'ENTITY_WANDERING_TRADER_REAPPEARED'");
		add(channelsMap, "  ServerConverter: ");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'proxy;&2BungeeCord;/warp spawn;&eDer Proxy ist der Verwalter aller Spigotserver.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'hub;&aHub;/warp hub;&eVom Hub kommst du zu alle~!~&eandere Server.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'nether;&cNether;/warp nether;&cDie Hölle'");				
		add(channelsMap, "  WorldConverter: ");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'spawn;&aSpawn;/warp spawn;&eVom Spawn kommst du zu alle~!~&eandere Server.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'nether;&cNether;/warp nether;&cDie Hölle'");
		add(channelsMap, "  UseColor: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseItemReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseBookReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseRunCommandReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseSuggestCommandReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseWebsiteReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseEmojiReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseMentionReplacer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UsePositionReplacer: "
				, ISO639_2B.GER,
				false);
		
		add(channelsMap, "global:");
		add(channelsMap, "  UniqueIdentifierName: "
				, ISO639_2B.GER,
				"'Global'");
		add(channelsMap, "  Symbol: "
				, ISO639_2B.GER,
				"'NULL'");
		add(channelsMap, "  InChatName: "
				, ISO639_2B.GER,
				"'&e[G]'");
		add(channelsMap, "  InChatColorMessage: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  Permission: "
				, ISO639_2B.GER,
				"scc.channel.global'");
		add(channelsMap, "  JoinPart: "
				, ISO639_2B.GER,
				"'&eGlobal &7= Without Symbol'");
		add(channelsMap, "  ChatFormat: "
				, ISO639_2B.GER,
				"'&7[%time%&7] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%&7: %message%'");
		add(channelsMap, "  UseSpecificServer: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseSpecificsWorld: "
				, ISO639_2B.GER,
				false);
		add(channelsMap, "  UseBlockRadius: "
				, ISO639_2B.GER,
				0);
		add(channelsMap, "  MinimumTimeBetweenMessages: "
				, ISO639_2B.GER,
				500);
		add(channelsMap, "  MinimumTimeBetweenSameMessages: "
				, ISO639_2B.GER,
				1000);
		add(channelsMap, "  PercentOfSimiliarityOrLess: "
				, ISO639_2B.GER,
				75.0);
		add(channelsMap, "  TimeColor: "
				, ISO639_2B.GER,
				"'&7'");
		add(channelsMap, "  PlayernameCustomColor: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  OtherPlayernameCustomColor: "
				, ISO639_2B.GER,
				"'&e'");
		add(channelsMap, "  SeperatorBetweenPrefix: "
				, ISO639_2B.GER,
				"' '");
		add(channelsMap, "  SeperatorBetweenSuffix: "
				, ISO639_2B.GER,
				"' '");
		add(channelsMap, "  MentionSound: "
				, ISO639_2B.GER,
				"'ENTITY_WANDERING_TRADER_REAPPEARED'");
		add(channelsMap, "  ServerConverter: ");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'proxy;&2BungeeCord;/warp spawn;&eDer Proxy ist der Verwalter aller Spigotserver.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'hub;&aHub;/warp hub;&eVom Hub kommst du zu alle~!~&eandere Server.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'nether;&cNether;/warp nether;&cDie Hölle'");				
		add(channelsMap, "  WorldConverter: ");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'spawn;&aSpawn;/warp spawn;&eVom Spawn kommst du zu alle~!~&eandere Server.'");
		add(channelsMap, "  "
				, ISO639_2B.GER,
				"- 'nether;&cNether;/warp nether;&cDie Hölle'");
		add(channelsMap, "  UseColor: "
				, ISO639_2B.GER,
				true);
		add(channelsMap, "  UseItemReplacer: "
				, ISO639_2B.GER,
				true);
		add(channelsMap, "  UseBookReplacer: "
				, ISO639_2B.GER,
				true);
		add(channelsMap, "  UseRunCommandReplacer: "
				, ISO639_2B.GER,
				true);
		add(channelsMap, "  UseSuggestCommandReplacer: "
				, ISO639_2B.GER,
				true);
		add(channelsMap, "  UseWebsiteReplacer: "
				, ISO639_2B.GER,
				true);
		add(channelsMap, "  UseEmojiReplacer: "
				, ISO639_2B.GER,
				true);
		add(channelsMap, "  UseMentionReplacer: "
				, ISO639_2B.GER,
				true);
		add(channelsMap, "  UsePositionReplacer: "
				, ISO639_2B.GER,
				true);
	}
	
	 //INFO:Emojis
	public void initEmojis()
	{
		/* ADDME
		 * https://de.wikipedia.org/wiki/Unicodeblock_Mathematische_Operatoren
		 * https://de.wikipedia.org/wiki/Unicodeblock_Verschiedene_technische_Zeichen
		 * https://de.wikipedia.org/wiki/Unicodeblock_Symbole_für_Steuerzeichen Nur das letzte
		 * https://de.wikipedia.org/wiki/Unicodeblock_Umschlossene_alphanumerische_Zeichen
		 * https://de.wikipedia.org/wiki/Unicodeblock_Rahmenzeichnung
		 * https://de.wikipedia.org/wiki/Unicodeblock_Blockelemente
		 * https://de.wikipedia.org/wiki/Unicodeblock_Geometrische_Formen
		 * https://de.wikipedia.org/wiki/Unicodeblock_Verschiedene_Symbole
		 * https://de.wikipedia.org/wiki/Unicodeblock_Pfeile
		 * https://de.wikipedia.org/wiki/Unicodeblock_Zus%C3%A4tzliche_Pfeile-A
		 * https://de.wikipedia.org/wiki/Unicodeblock_Zus%C3%A4tzliche_Pfeile-B
		 * https://de.wikipedia.org/wiki/Unicodeblock_Zus%C3%A4tzliche_Pfeile-C
		 * https://de.wikipedia.org/wiki/Unicodeblock_CJK-Symbole_und_-Interpunktion
		 * https://de.wikipedia.org/wiki/Unicodeblock_CJK-Kompatibilit%C3%A4tsformen
		 * https://de.wikipedia.org/wiki/Unicodeblock_W%C3%A4hrungszeichen
		 * https://de.wikipedia.org/wiki/Unicodeblock_I-Ging-Hexagramme
		 * https://de.wikipedia.org/wiki/Unicodeblock_Zahlzeichen
		 * https://de.wikipedia.org/wiki/Unicodeblock_Dingbats
		 * https://www.weblog-deluxe.de/sonderzeichen-und-symbole-ascii-art-fur-second-life/
		 * https://de.wikipedia.org/wiki/Unicodeblock_Umschlossene_alphanumerische_Zeichen
		 * https://de.wikipedia.org/wiki/Unicodeblock_Zus%C3%A4tzliche_umschlossene_alphanumerische_Zeichen
		 */
		
		add(emojisMap, "umbrella: ", ISO639_2B.GER, "'☂'");
	}
	
	public void initWordFilter() //INFO:Wordfilter
	{
		add(wordFilterMap, "WordFilter: ");
		add(wordFilterMap, "", ISO639_2B.GER, "- 'Arschloch'");
		add(wordFilterMap, "", ISO639_2B.GER, "- 'Asshole'");
	}
	
	public enum GuiType
	{
		CHANNELS
	}
	
	private void setSlot(GuiType type, int slot, String function,
			SettingsLevel settingLevel, org.bukkit.Material material, int amount, String urlTexture,
			String displaynameGER, String displaynameENG,
			String[] itemflag, String[] enchantments, String[] lore)
	{
		LinkedHashMap<ISO639_2B, ArrayList<String>> gui = new LinkedHashMap<>();
		boolean exist = false;
		if(guiMap.containsKey(type.toString()))
		{
			gui = guiMap.get(type.toString());
			exist = true;
		}
		add(gui, function+":");
		add(gui, "  "+settingLevel.getName()+":");
		add(gui, "    Name:", 
				"'"+displaynameGER+"'",
				"'"+displaynameENG+"'");
		add(gui, "    Slot:", ISO639_2B.GER, slot);
		add(gui, "    Material:", ISO639_2B.GER, "'"+material.toString()+"'");
		add(gui, "    Amount: ", ISO639_2B.GER, amount);
		if(urlTexture != null)
		{
			add(gui, "    PlayerHeadTexture:", ISO639_2B.GER, "'"+urlTexture+"'");
		}
		if(itemflag != null)
		{
			add(gui, "    Itemflag:");
			for(String s : itemflag)
			{
				add(gui, "    ", ISO639_2B.GER,
						"- '"+s+"'");
			}
		}
		if(enchantments != null)
		{
			add(gui, "    Enchantments:");
			for(String s : enchantments)
			{
				add(gui, "    ", ISO639_2B.GER,
						"- '"+s+"'");
			}
		}
		if(lore != null)
		{
			add(gui, "    Lore:");
			for(String s : lore)
			{
				add(gui, "    ", ISO639_2B.GER,
						"- '"+s+"'");
			}
		}
		if(exist)
		{
			guiMap.replace(type.toString(), gui);
		} else
		{
			guiMap.put(type.toString(), gui);
		}
	}
	
	 //INFO:Guis
	public void initGuis()
	{
		setSlot(GuiType.CHANNELS, 22, GuiValues.CHANNELGUI_FUNCTION+"_Private",
				SettingsLevel.NOLEVEL, org.bukkit.Material.PAPER, 1,
				null,
				"'&ePrivatChat: %boolean%'",
				"'&ePrivateChat: %boolean%'",
				null,//Itemflag
				null,//Ench
				null);
	}
}
