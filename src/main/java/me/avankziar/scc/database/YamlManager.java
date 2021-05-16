package main.java.me.avankziar.scc.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.scc.database.Language.ISO639_2B;
import main.java.me.avankziar.scc.spigot.guihandling.GUIApi.SettingsLevel;
import main.java.me.avankziar.scc.spigot.guihandling.GuiValues;

public class YamlManager
{
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> chatTitleKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> channelsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> emojisKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> wordFilterKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> guiKeys = new LinkedHashMap<>();
	
	public YamlManager(boolean spigot)
	{
		initConfig();
		initCommands();
		initLanguage();
		initChatTitle();
		initChannels();
		initEmojis();
		if(spigot)
		{
			initGuis();
		}
		initWordFilter();
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
	
	public LinkedHashMap<String, Language> getConfigKey()
	{
		return configKeys;
	}
	
	public LinkedHashMap<String, Language> getCommandsKey()
	{
		return commandsKeys;
	}
	
	public LinkedHashMap<String, Language> getLanguageKey()
	{
		return languageKeys;
	}
	
	public LinkedHashMap<String, Language> getChatTitleKey()
	{
		return chatTitleKeys;
	}
	
	public LinkedHashMap<String, Language> getChannelsKey()
	{
		return channelsKeys;
	}
	
	public LinkedHashMap<String, Language> getEmojiKey()
	{
		return emojisKeys;
	}
	
	public LinkedHashMap<String, Language> getWordFilterKey()
	{
		return wordFilterKeys;
	}
	
	public LinkedHashMap<String, Language> getGuiKeys(String key)
	{
		return guiKeys.get(key);
	}
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInputBungee(net.md_5.bungee.config.Configuration yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	public void setFileInputBukkit(org.bukkit.configuration.file.YamlConfiguration yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", ""));
			} else
			{
				yml.set(key, keyMap.get(key).languageValues.get(languageType)[0]);
			}
		} else
		{
			List<Object> list = Arrays.asList(keyMap.get(key).languageValues.get(languageType));
			ArrayList<String> stringList = new ArrayList<>();
			if(list instanceof List<?>)
			{
				for(Object o : list)
				{
					if(o instanceof String)
					{
						stringList.add(((String) o).replace("\r\n", ""));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	public void initConfig() //INFO:Config
	{
		configKeys.put("Language"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENG"}));
		configKeys.put("Server"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"hub"}));
		configKeys.put("IsBungeeActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Mysql.Status"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.Host"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"127.0.0.1"}));
		configKeys.put("Mysql.Port"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				3306}));
		configKeys.put("Mysql.DatabaseName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"mydatabase"}));
		configKeys.put("Mysql.SSLEnabled"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.AutoReconnect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("Mysql.VerifyServerCertificate"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		configKeys.put("Mysql.User"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"admin"}));
		configKeys.put("Mysql.Password"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"not_0123456789"}));
		configKeys.put("Mysql.TableNameI"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"simplechatchannelsPlayerData"}));
		configKeys.put("Mysql.TableNameII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"simplechatchannelsIgnorelist"}));
		configKeys.put("Mysql.TableNameIII"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"simplechatchannelsPermanentChannels"}));
		configKeys.put("Mysql.TableNameIV"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"simplechatchannelsItemJson"}));
		configKeys.put("Mysql.TableNameV"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"simplechatchannelsPlayerUsedChannels"}));
		configKeys.put("Mysql.TableNameVI"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"simplechatchannelsMails"}));
		configKeys.put("Use.Mail"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("PrivateChannel.UseDynamicColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("PrivateChannel.DynamicColorPerPlayerChat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&#F5A9F2",
				"&#F7819F",
				"&#FA58F4",
				"&#FE2E64",
				"&#FF00FF",
				"&#DF013A",
				"&#B404AE",
				"&#8A0829",
				"&#D0A9F5",
				"&#D0A9F5",
				"&#9A2EFE"}));
		configKeys.put("PermanentChannel.AmountPerPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1}));
		configKeys.put("PermanentChannel.InviteCooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				60}));
		configKeys.put("TemporaryChannel.InviteCooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				60}));
		configKeys.put("BroadCast.UsingChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Global"}));
		configKeys.put("Mute.SendGlobal"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("CleanUp.RunAutomaticByRestart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		configKeys.put("CleanUp.DeletePlayerWhichJoinIsOlderThanDays"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				120}));
		configKeys.put("CleanUp.DeleteReadedMailWhichIsOlderThanDays"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				120}));
		configKeys.put("Mail.UseChannelForMessageParser"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Global"}));
		configKeys.put("Mail.ConsoleReplacerInSendedMails"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Console"}));
		configKeys.put("Mail.CCSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"@"}));
		configKeys.put("Mail.SubjectMessageSeperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<|>"}));
		configKeys.put("ChatReplacer.Command.RunCommandStart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"cmd|/"}));
		configKeys.put("ChatReplacer.Command.SuggestCommandStart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"cmd/"}));
		configKeys.put("ChatReplacer.Command.CommandStartReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[&fClickCmd: "}));
		configKeys.put("ChatReplacer.Command.CommandEndReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7]"}));
		configKeys.put("ChatReplacer.Command.SpaceReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"+"}));
		configKeys.put("ChatReplacer.Item.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<item"}));
		configKeys.put("ChatReplacer.Item.Seperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				":"}));
		configKeys.put("ChatReplacer.Item.End"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				">"}));
		configKeys.put("ChatReplacer.Book.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<book"}));
		configKeys.put("ChatReplacer.Book.Seperator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				":"}));
		configKeys.put("ChatReplacer.Book.End"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				">"}));
		configKeys.put("ChatReplacer.Emoji.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				":|"}));
		configKeys.put("ChatReplacer.Emoji.End"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"|:"}));
		configKeys.put("ChatReplacer.Mention.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"@@"}));
		configKeys.put("ChatReplacer.Mention.Color"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&4"}));
		configKeys.put("ChatReplacer.Mention.SoundEnum"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENTITY_WANDERING_TRADER_REAPPEARED"}));
		configKeys.put("ChatReplacer.Position.Replacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<pos>"}));
		configKeys.put("ChatReplacer.Position.Replace"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[&9%server% &d%world% &e%x% %y% %z%&7]"}));
		configKeys.put("ChatReplacer.NewLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"~!~"}));
		configKeys.put("Gui.ActiveTerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&a✔"}));
		configKeys.put("Gui.DeactiveTerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&c✖"}));
		configKeys.put("Gui.Channels.RowAmount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				6}));
		configKeys.put("GuiList"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"CHANNELS", "DUMMY"}));
		
	}
	
	//INFO:Commands
	public void initCommands()
	{
		comBypass();
		commandsInput("scc", "scc", "scc.cmd.scc.scc", 
				"/scc [pagenumber]", "/scc ",
				"&c/scc [Seitenzahl] &f| Infoseite für alle Befehle.",
				"&c/scc [pagenumber] &f| Info page for all commands.");
		commandsInput("clch", "clch", "scc.cmd.clch", 
				"/clch [pagenumber]", "/clch ",
				"&c/clch <Spielername> <Zahl> <Nachricht...> &f| Sendet einen Klickbaren Chat für den Spieler. Geeignet für Citizen / Denizen plugin.",
				"&c/clch <player name> <number> <message...> &f| Sends a clickable chat for the player. Suitable for Citizen / Denizen plugin.");
		commandsInput("msg", "msg", "scc.cmd.msg", 
				"/msg <player> <message...>", "/msg ",
				"&c/msg <Spielernamen> <Nachricht> &f| Schreibt dem Spieler privat. Alle Spieler, welche online sind, werden als Vorschlag angezeigt.",
				"&c/msg <player name> <message> &f| Write to the player privately. All players who are online will be displayed as a suggestion.");
		commandsInput("re", "re", "scc.cmd.re", 
				"/re <player> <message...>", "/re ",
				"&c/re <Spielernamen> <Nachricht> &f| Schreibt dem Spieler privat. Alle Spieler mit denen man schon geschrieben hat, werden als Vorschlag angezeigt.",
				"&c/re <player name> <message> &f| Write to the player privately. All players with whom you have already written are displayed as suggestions.");
		commandsInput("r", "r", "scc.cmd.r", 
				"/r <message...>", "/r ",
				"&c/r <message...> &f| Schreibt dem Spieler, welcher einem selbst zuletzt privat geschrieben hat.",
				"&c/r <message...> &f| Write to the player who last wrote to you privately.");
		commandsInput("w", "w", "scc.cmd.w", 
				"/w <player>", "/w ",
				"&c/w [Spielername] &f| Consolenbefehl für Privatnachrichten an Spieler.",
				"&c/w [playername] &f| Consolecommand for private message to player.");
		String path = "scc_";
		String basePermission = "scc.cmd.scc";
		//INFO:Argument Start
		argumentInput(path+"book", "book", basePermission,
				"/scc book <Itemname>", "/scc book ",
				"&c/scc book <Itemname> &f| Öffnet das Buch vom ItemReplacer.",
				"&c/scc book <Itemname> &f| Open the book from ItemReplacer.");
		argumentInput(path+"broadcast", "broadcast", basePermission,
				"/scc broadcast <message...>", "/scc broadcast ",
				"&c/scc broadcast <Nachricht> &f| Zum Senden einer Broadcast Nachricht. Falls Bungeecord aktiviert ist, kann man auch von Spigot als Console, bungeecordübergreifend dies an alle Spieler senden.",
				"&c/scc broadcast <message> &f| To send a broadcast message. If bungeecord is enabled, you can also send this to all players from Spigot as a console, across bungeecords.");
		argumentInput(path+"channel", "channel", basePermission,
				"/scc channel <channel>", "/scc channel ",
				"&c/scc channel &f| Zum An- & Ausstellen des angegebenen Channels.",
				"&c/scc channel &f| To turn the specified channel on & off.");
		argumentInput(path+"channelgui", "channelgui", basePermission,
				"/scc channelgui ", "/scc channelgui ",
				"&c/scc channelgui &f| Öffnet ein Menü, wo die Channels aus und eingestellt werden können.",
				"&c/scc channelgui &f| Opens a menu where the channels can be selected and set.");
		argumentInput(path+"ignore", "ignore", basePermission,
				"/scc ignore <player>", "/scc ignore ",
				"&c/scc ignore <Spielername> &f| Zum Einsetzten oder Aufheben des Ignores für den Spieler.",
				"&c/scc ignore <playername> &f| To set or remove the ignore for the player.");
		argumentInput(path+"ignorelist", "ignorelist", basePermission,
				"/scc ignorelist [playername]", "/scc ignorelist ",
				"&c/scc ignorelist [Spielername] &f| Zum Anzeigen aller Spieler auf der Ignoreliste.",
				"&c/scc ignorelist [playername] &f| To show all players on the ignore list.");
		argumentInput(path+"mute", "mute", basePermission,
				"/scc mute <playername> [values...]", "/scc mute ",
				"&c/scc mute <Spielername> [Werte...] &f| Stellt den Spieler für die angegebene Zeit stumm. Bei keinem Wert ist es permanent. Möglich addidative Werte sind: (Format xxx:<Zahl>) y=Jahre, M=Monate, d=Tage, H=Stunden, m=Minuten, s=Sekunden.",
				"&c/scc mute <playername> [values...] &f| Mutes the player for the specified time. With no value, it is permanent. Possible addidative values are: (format xxx:<number>) y=years, M=months, d=days, H=hours, m=minutes, s=seconds.");
		argumentInput(path+"performance", "performance", basePermission,
				"/scc performance ", "/scc performance ",
				"&c/scc performance &f| Zeigt die MysqlPerformances des Plugins an.",
				"&c/scc performance &f| Displays the MysqlPerformances of the plugin.");
		argumentInput(path+"unmute", "unmute", basePermission,
				"/scc unmute <playername", "/scc unmute ",
				"&c/scc unmute <Spielername> &f| Zum Wiedererlangen der Schreib-Rechte.",
				"&c/scc unmute <playername> &f| To regain write permissions.");
		argumentInput(path+"updateplayer", "updateplayer", basePermission,
				"/scc updateplayer <playername>", "/scc updateplayer ",
				"&c/scc updateplayer <Spielername> &f| Updatet die Zugangsrechte des Spielers für alle Channels.",
				"&c/scc updateplayer <playername> &f| Updates the player's access rights for all channels.");
		
		argumentInput(path+"option", "option", basePermission,
				"/scc option ", "/scc option ",
				"&c/scc option &f| Zwischenbefehl",
				"&c/scc option &f| Intermediate command");
		basePermission = "scc.cmd.scc.option";
		argumentInput(path+"option_channel", "channel", basePermission,
				"/scc option channel ", "/scc option channel ",
				"&c/scc option channel &f| Aktiviert oder deaktiviert ob man beim Joinen seine aktiven Channels sieht.",
				"&c/scc option channel &f| Enables or disables whether you can see your active channels when joining.");
		argumentInput(path+"option_join", "join", basePermission,
				"/scc option join", "/scc option join",
				"&c/scc option join &f| Aktiviert oder deaktiviert ob man die Joinnachricht anderer Spieler sieht.",
				"&c/scc option join &f| Enables or disables whether you can see the join message of other players.");
		argumentInput(path+"option_spy", "spy", basePermission,
				"/scc option spy ", "/scc option spy ",
				"&c/scc option spy &f| Aktiviert oder deaktiviert ob man Nachrichten sehen kann, welche einem sonst vorborgen wären.",
				"&c/scc option spy &f| Enables or disables whether you can see messages that would otherwise be hidden from you.");
		basePermission = "scc.cmd.scc.item.";
		argumentInput(path+"item", "item", basePermission,
				"/scc item ", "/scc item ",
				"&c/scc item &f| Öffnet das Menü, wo man die Items für den Replacer einstellen kann.",
				"&c/scc item &f| Opens the menu where you can set the items for the replacer.");
		argumentInput(path+"item_rename", "rename", basePermission,
				"/scc item rename <oldname> <newname> ", "/scc item rename ",
				"&c/scc item rename <oldname> <newname> &f| Benennt das Item, welches auf den alten Namen hört, um.",
				"&c/scc item rename <oldname> <newname> &f| Renames the item that goes by the old name.");
		argumentInput(path+"item_replacers", "replacers", basePermission,
				"/scc item replacers ", "/scc item replacers ",
				"&c/scc item replacers &f| Zeigt alle Möglichen Replacer im Chat an, sowie dessen Item als Hover.",
				"&c/scc item replacers &f| Displays all possible replacers in the chat, as well as their item as a hover.");
		//INFO:PermanentChannel
		basePermission = "scc.cmd.scc";
		argumentInput(path+"pc", "permanentchannel", basePermission,
				"/scc permanentchannel ", "/scc permanentchannel ",
				"&c/scc permanentchannel &f| Zwischenbefehl",
				"&c/scc permanentchannel &f| Intermediate command");
		basePermission = "scc.cmd.scc.pc";
		argumentInput(path+"pc_ban", "ban", basePermission,
				"/scc permanentchannel ban <channelname> <playername> ", "/scc permanentchannel ban",
				"&c/scc permanentchannel ban <Channelname> <Spielername> &f| Bannt einen Spieler von einem permanenten Channel.",
				"&c/scc permanentchannel ban <channelname> <playername> &f| Bans a player from a permanent channel.");
		argumentInput(path+"pc_unban", "unban", basePermission,
				"/scc permanentchannel unban <channelname> <playername>", "/scc permanentchannel unban ",
				"&c/scc permanentchannel <Channelname> <Spielername> &f| Unbannt einen Spieler von einem permanenten Channel.",
				"&c/scc permanentchannel <channelname> <playername> &f| Unbans a player from a permanent channel.");
		argumentInput(path+"pc_changepassword", "changepassword", basePermission,
				"/scc permanentchannel changepassword <channelname> <password>", "/scc permanentchannel changepassword ",
				"&c/scc permanentchannel changepassword <Channelname> <Passwort> &f| Ändert das Passwort von einem permanenten Channel.",
				"&c/scc permanentchannel changepassword <channelname> <password> &f| Changes the password of a permanent channel.");
		argumentInput(path+"pc_channels", "channels", basePermission,
				"/scc permanentchannel channels <channel> ", "/scc permanentchannel channels ",
				"&c/scc permanentchannel channels <Channel> &f| Aktiviert oder deaktiviert, für sich selbst, den angegeben Channel.",
				"&c/scc permanentchannel channels <channel> &f| Enables or disables, for itself, the specified channel.");
		argumentInput(path+"pc_chatcolor", "chatcolor", basePermission,
				"/scc permanentchannel chatcolor <channelname> <color> ", "/scc permanentchannel chatcolor ",
				"&c/scc permanentchannel chatcolor <Channelname> <Farbe> &f| Ändert die Farbe des permanenten Channel für den Chat.",
				"&c/scc permanentchannel chatcolor <channelname> <color> &f| Changes the color of the permanent channel for the chat.");
		argumentInput(path+"pc_create", "create", basePermission,
				"/scc permanentchannel create <channelname> [password] ", "/scc permanentchannel create ",
				"&c/scc permanentchannel create <Channelname> [Passwort] &f| Erstellt einen permanenten Channel. Optional mit Passwort.",
				"&c/scc permanentchannel create <channelname> [password] &f| Creates a permanent channel. Optionally with password.");
		argumentInput(path+"pc_delete", "delete", basePermission,
				"/scc permanentchannel delete <channelname> ", "/scc permanentchannel delete ",
				"&c/scc permanentchannel delete <Channelname> &f| Löscht den Channel.",
				"&c/scc permanentchannel delete <channelname> &f| Delete the channel.");
		argumentInput(path+"pc_info", "info", basePermission,
				"/scc permanentchannel info [channelname] ", "/scc permanentchannel info ",
				"&c/scc permanentchannel info [Channelname] &f| Zeigt alle Infos zum permanenten Channel an.",
				"&c/scc permanentchannel info [channelname] &f| Displays all info about the permanent channel.");
		argumentInput(path+"pc_inherit", "inherit", basePermission,
				"/scc permanentchannel inherit <channelname> <playername> ", "/scc permanentchannel inherit ",
				"&c/scc permanentchannel inherit <Channelname> <Spielername> &f| Lässt den Spieler den Channel als Ersteller beerben.",
				"&c/scc permanentchannel inherit <channelname> <playername> &f| Lets the player inherit the channel as creator.");
		argumentInput(path+"pc_invite", "invite", basePermission,
				"/scc permanentchannel invite <channelname> <playername>", "/scc permanentchannel invite ",
				"&c/scc permanentchannel invite <Channelname> <Spielername> &f| Lädt einen Spieler in den permanenten Channel ein.",
				"&c/scc permanentchannel invite <channelname> <playername> &f| Invites a player to the permanent Channel.");
		argumentInput(path+"pc_join", "join", basePermission,
				"/scc permanentchannel join <channelname> [password] ", "/scc permanentchannel join ",
				"&c/scc permanentchannel join <Channelname> [Passwort] &f| Betritt einen permanenten Channel.",
				"&c/scc permanentchannel join <channelname> [password] &f| Enter a permanent channel.");
		argumentInput(path+"pc_kick", "kick", basePermission,
				"/scc permanentchannel kick <channelname> <playername> ", "/scc permanentchannel kick ",
				"&c/scc permanentchannel kick <Channelname> <Spielername> &f| Kickt einen Spieler von einem permanenten Channel.",
				"&c/scc permanentchannel kick <channelname> <playername> &f| Kicks a player from a permanent channel.");
		argumentInput(path+"pc_leave", "leave", basePermission,
				"/scc permanentchannel leave <channelname> ", "/scc permanentchannel leave ",
				"&c/scc permanentchannel leave <Channelname> &f| Verlässt einen permanenten Channel.",
				"&c/scc permanentchannel leave <channelname> &f| Leaves a permanent channel.");
		argumentInput(path+"pc_namecolor", "namecolor", basePermission,
				"/scc permanentchannel namecolor <channelname> <color> ", "/scc permanentchannel namecolor ",
				"&c/scc permanentchannel namecolor <Channelname> <Farbe> &f| Ändert die Farbe des permanenten Channelpräfix.",
				"&c/scc permanentchannel namecolor <channelname> <color> &f| Changes the color of the permanent Channelprefix.");
		argumentInput(path+"pc_player", "player", basePermission,
				"/scc permanentchannel player [playername] ", "/scc permanentchannel player ",
				"&c/scc permanentchannel player [Spielername] &f| Zeigt alle permanenten Channels, wo der Spieler beigetreten ist an.",
				"&c/scc permanentchannel player [playername] &f| Displays all permanent channels where the player has joined.");
		argumentInput(path+"pc_rename", "rename", basePermission,
				"/scc permanentchannel rename <channelname> <newname>", "/scc permanentchannel rename ",
				"&c/scc permanentchannel rename <Channelname> <Neuer Name> &f| Ändert den Namen des permanenten Channel.",
				"&c/scc permanentchannel rename <channelname> <newname> &f| Changes the name of the permanent Channel.");
		argumentInput(path+"pc_symbol", "symbol", basePermission,
				"/scc permanentchannel symbol <channelname> <symbols>", "/scc permanentchannel symbol ",
				"&c/scc permanentchannel symbol <Channelname> <Symbole> &f| Ändert das Zugangssymbol des Channels.",
				"&c/scc permanentchannel symbol <channelname> <symbols> &f| Changes the access icon of the channel.");
		argumentInput(path+"pc_vice", "vice", basePermission,
				"/scc permanentchannel vice <channelname> <playername> ", "/scc permanentchannel vice ",
				"&c/scc permanentchannel vice <Channelname> <Spielername> &f| Befördert oder degradiert einen Spieler innerhalb des permanenten Channels.",
				"&c/scc permanentchannel vice <channelname> <playername> &f| Promotes or demotes a player within the permanent Channel.");
		//INFO:TemporaryChannel
		basePermission = "scc.cmd.scc";
		argumentInput(path+"tc", "temporarychannel", basePermission,
				"/scc temporarychannel ", "/scc temporarychannel ",
				"&c/scc temporarychannel &f| Zwischenbefehl",
				"&c/scc temporarychannel &f| Intermediate command");
		basePermission = "scc.cmd.scc.temporarychannel.";
		argumentInput(path+"tc_ban", "ban", basePermission,
				"/scc temporarychannel ban <playername> ", "/scc temporarychannel ban ",
				"&c/scc temporarychannel ban <Spielername> &f| Bannt einen Spieler von einem temporären Channel.",
				"&c/scc temporarychannel ban <playername> &f| Bans a player from a temporary channel.");
		argumentInput(path+"tc_unban", "unban", basePermission,
				"/scc temporarychannel unban <playername> ", "/scc temporarychannel unban ",
				"&c/scc temporarychannel unban <Spielername> &f| Entbannt einen Spieler von einem temporären Channel.",
				"&c/scc temporarychannel unban <playername> &f| Unbans a player from a temporary channel.");
		argumentInput(path+"tc_changepassword", "changepassword", basePermission,
				"/scc temporarychannel changepassword <password> ", "/scc temporarychannel changepassword ",
				"&c/scc temporarychannel changepassword <Passwort> &f| Ändert das Passwort von einem temporären Channel.",
				"&c/scc temporarychannel changepassword <password> &f| Changes the password of a temporary channel.");
		argumentInput(path+"tc_create", "create", basePermission,
				"/scc temporarychannel create <channelname> [password] ", "/scc temporarychannel create ",
				"&c/scc temporarychannel create <Channelname> [Passwort] &f| Erstellt einen temporären Channel. Optional mit Passwort.",
				"&c/scc temporarychannel create <channelname> [password] &f| Creates a temporary channel. Optionally with password.");
		argumentInput(path+"tc_info", "info", basePermission,
				"/scc temporarychannel info ", "/scc temporarychannel info ",
				"&c/scc temporarychannel info &f| Zeigt alle Informationen bezüglich des temporären Channels an.",
				"&c/scc temporarychannel info &f| Displays all information related to the temporary channel.");
		argumentInput(path+"tc_invite", "invite", basePermission,
				"/scc temporarychannel invite <playername> ", "/scc temporarychannel invite ",
				"&c/scc temporarychannel invite <Spielername> &f| Lädt einen Spieler in den eigenen temporären Channel ein.",
				"&c/scc temporarychannel invite <playername> &f| Invites a player to the own temporary channel.");
		argumentInput(path+"tc_join", "join", basePermission,
				"/scc temporarychannel join <channelname> [password] ", "/scc temporarychannel join ",
				"&c/scc temporarychannel join <Channelname> [Passwort] &f| Betritt einem temporären Channel.",
				"&c/scc temporarychannel join <channelname> [password] &f| Enter a temporary channel.");
		argumentInput(path+"tc_kick", "kick", basePermission,
				"/scc temporarychannel kick <playername> ", "/scc temporarychannel kick ",
				"&c/scc temporarychannel kick <Spielername> &f| Kickt einen Spieler von einem temporären Channel.",
				"&c/scc temporarychannel kick <playername> &f| Kicks a player from a temporary channel.");
		argumentInput(path+"tc_leave", "leave", basePermission,
				"/scc temporarychannel leave ", "/scc temporarychannel leave ",
				"&c/scc temporarychannel leave &f| Verlässt einen temporären Channel.",
				"&c/scc temporarychannel leave &f| Leaves a temporary channel.");
		commandsInput("mail", "mail", "scc.cmd.mail.mail", 
				"/mail [page]", "/mail ",
				"&c/mail [Seitenzahl] &f| Zeigt alle ungelesene Mails mit Klick- und HoverEvents.",
				"&c/mail [pagen] &f| Shows all unread mails with click and hover events.");
		path = "mail_";
		basePermission = "scc.cmd.mail";
		argumentInput(path+"lastmails", "lastmails", basePermission,
				"/mail lastmails [page] [playername] ", "/mail read ",
				"&c/mail read [Seitenzahl] [Spielername] &f| Liest die Mail.",
				"&c/mail read [page] [playername] &f| Read the mail.");
		argumentInput(path+"read", "read", basePermission,
				"/mail read <id> ", "/mail read ",
				"&c/mail read <id> &f| Liest die Mail.",
				"&c/mail read <id> &f| Read the mail.");
		argumentInput(path+"send", "send", basePermission,
				"/mail send <reciver, multiple seperate with @> <subject...> <seperator> <message...> ", "/mail read ",
				"&c/mail send <Empfänger, mehrere getrennt mit @> <Betreff...> <Trennwert> <Nachricht...> &f| Schreibt eine Mail.",
				"&c/mail send <reciver, multiple seperate with @> <subject...> <seperator> <message...> &f| Write a mail.");
		/*argumentInput(path+"", "", basePermission,
				"/scc ", "/scc ",
				"&c/scc &f| ",
				"&c/scc &f| ");*/
	}
	
	private void comBypass() //INFO:ComBypass
	{
		String path = "Bypass.";
		String normal = "scc.channel.";
		String by = "scc.bypass.";
		String cus = "scc.custom.";
		commandsKeys.put(path+"Color.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"color"}));
		commandsKeys.put(path+"Color.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"color"}));
		commandsKeys.put(path+"Item.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"item"}));
		commandsKeys.put(path+"Item.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"item"}));
		commandsKeys.put(path+"Book.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"book"}));
		commandsKeys.put(path+"Book.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"book"}));
		commandsKeys.put(path+"RunCommand.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"runcommand"}));
		commandsKeys.put(path+"RunCommand.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"runcommand"}));
		commandsKeys.put(path+"SuggestCommand.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"suggestcommand"}));
		commandsKeys.put(path+"SuggestCommand.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"suggestcommand"}));
		commandsKeys.put(path+"Website.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"website"}));
		commandsKeys.put(path+"Website.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"website"}));
		commandsKeys.put(path+"Emoji.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"emoji"}));
		commandsKeys.put(path+"Emoji.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"emoji"}));
		commandsKeys.put(path+"Mention.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"mention"}));
		commandsKeys.put(path+"Mention.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"mention"}));
		commandsKeys.put(path+"Position.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"position"}));
		commandsKeys.put(path+"Position.Bypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"position"}));
		commandsKeys.put(path+"Sound.Channel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				normal+"sound"}));
		commandsKeys.put(path+"Ignore"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"ignore"}));
		commandsKeys.put(path+"OfflineChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"offlinechannel"}));
		commandsKeys.put(path+"PermanentChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"permanentchannel"}));
		commandsKeys.put(path+"BookOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"bookother"}));
		commandsKeys.put(path+"Mail.ReadOther"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"mail.readother"}));
		path = "Custom.";
		commandsKeys.put(path+"ItemReplacerStorage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				cus+"itemreplacerstorage."}));
	}
	
	private void commandsInput(String path, String name, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		commandsKeys.put(path+".Name"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				name}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
	}
	
	private void argumentInput(String path, String argument, String basePermission, 
			String suggestion, String commandString,
			String helpInfoGerman, String helpInfoEnglish)
	{
		commandsKeys.put(path+".Argument"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				argument}));
		commandsKeys.put(path+".Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				basePermission+"."+argument}));
		commandsKeys.put(path+".Suggestion"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				suggestion}));
		commandsKeys.put(path+".CommandString"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				commandString}));
		commandsKeys.put(path+".HelpInfo"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				helpInfoGerman,
				helpInfoEnglish}));
	}
	
	public void initLanguage() //INFO:Languages
	{
		languageKeys.put("GeneralError",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cGenereller Fehler!",
						"&cGeneral Error!"}));
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"&cYour input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast dafür keine Rechte!",
						"&cYou have no rights for this!"}));
		languageKeys.put("PlayerNotExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler existiert nicht!",
						"&cThe player does not exist!"}));
		languageKeys.put("PlayerNotOnline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler ist nicht online!",
						"&cThe player is not online!"}));
		languageKeys.put("NotNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEiner oder einige der Argumente muss eine Zahl sein!",
						"&cOne or some of the arguments must be a number!"}));
		
		languageKeys.put("BaseInfo.Headline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&cSimpleChatChannels&7]&e=====",
						"&e=====&7[&cSimpleChatChannels&7]&e====="}));
		languageKeys.put("BaseInfo.Next",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e&nnächste Seite &e==>",
						"&e&nnext page &e==>"}));
		languageKeys.put("BaseInfo.Past",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e<== &nvorherige Seite",
						"&e<== &npast page"}));
		
		languageKeys.put("JoinListener.Comma",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&b, ",
						"&b, "}));
		languageKeys.put("JoinListener.YouMuted",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist zurzeit gemutet!",
						""}));
		languageKeys.put("JoinListener.Pretext",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&bAktive Channels: ",
						""}));
		languageKeys.put("JoinListener.Spy",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&4Spy",
						"&4Spy"}));
		languageKeys.put("JoinListener.Join",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&a+&7] &e%player%",
						"&7[&a+&7] &e%player%"}));
		languageKeys.put("JoinListener.HasNewMail",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%count% &eneue Mails!",
						"&eYou have &f%count% &enew mails!"}));
		languageKeys.put("JoinListener.Welcome",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&9.:|°*`&bWillkommen %player%&9´*°|:.",
						"&9.:|°*`&bWelcome %player%&9´*°|:."}));
		languageKeys.put("LeaveListener.Leave",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&c-&7] &e%player%",
						"&7[&c-&7] &e%player%"}));
		
		//INFO:ChatListener
		languageKeys.put("ChatListener.NoChannelIsNullChannel",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Chateingabe kann in kein Channel gepostet werden, da kein Channel passt und auch kein Channel ohne Eingangssymbol existiert!",
						"&cYour chat entry canot be posted in any channel, because no channel fits and also no channel exists without an entry symbol!"}));
		languageKeys.put("ChatListener.NotATemporaryChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist in keinem Temporären Channel!",
						"&cYou are not in a temporary channel!"}));
		languageKeys.put("ChatListener.NotAPermanentChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist in keinem Permanenten Channel!",
						"&cYou are not in a permanent channel!"}));
		languageKeys.put("ChatListener.SymbolNotKnow"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Permanente Channel &c%symbol% &cexistiert nicht.",
						"&cThe &f%symbol% &cpermanent channel does not exist."}));
		languageKeys.put("ChatListener.ChannelIsOff"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7Du hast diesen Channel ausgeschaltet. Bitte schalte ihn zum Benutzen wieder an.",
						"&7You have turned off this channel. Please turn it on again to use it."}));
		languageKeys.put("ChatListener.ContainsBadWords"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEiner deiner geschriebenen Wörter ist im Wortfilter enthalten, bitte unterlasse solche Ausdrücke!",
						"&cOne of your written words is included in the word filter, please refrain from such expressions!"}));
		languageKeys.put("ChatListener.YouAreMuted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist für &f%time% &cgemutet!",
						"&cYou are muted for &f%time%&c!"}));
		languageKeys.put("ChatListener.PleaseWaitALittle"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte warte noch bis &f%time%&c, bis du in im Channel &r%channel% &cwieder etwas schreibst.",
						"&cPlease wait until &f%time%&c to write something again in the channel &r%channel%&c."}));
		languageKeys.put("ChatListener.PleaseWaitALittleWithSameMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte warte noch bis &f%time%&c, bis du in im Channel &r%channel% &cwieder die selbe Nachricht schreibst.",
						"&cPlease wait until &f%time% &cto write again the same message in the channel &r%channel%&c."}));
		languageKeys.put("ChatListener.PlayerIgnoreYou"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler &f%player% &cignoriert dich!",
						"&cThe player &f%player% &cignores you!"}));
		languageKeys.put("ChatListener.PlayerIgnoreYouButYouBypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler &f%player% &cignoriert dich, jedoch konntest du das umgehen!",
						"&cThe &f%player% &cignores you, however you were able to bypass that!"}));
		languageKeys.put("ChatListener.PlayerHasPrivateChannelOff"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler &f%player% &chat privat Nachrichten deaktiviert!",
						"&cThe player &f%player% &chas private messaging disabled!"}));
		languageKeys.put("ChatListener.StringTrim"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte schreibe Nachrichten mit Inhalt.",
						"&cPlease write messages with content."}));
		languageKeys.put("ChatListener.ItemIsMissing"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&fNicht gefunden&7]",
						"&7[&fNot found&7]"}));
		languageKeys.put("ChatListener.PrivateChatHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&dKlick hier um im Privaten mit &f%player% &dzu schreiben.",
						"&dClick here to write in private with &f%player% &d."}));
		languageKeys.put("ChatListener.ChannelHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"%channelcolor%Klick hier um im %channel% Channel zu schreiben.",
						"%channelcolor%Click here to write in the %channel% channel."}));
		languageKeys.put("ChatListener.CommandRunHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&4Klick hier um den Befehl auszuführen.",
						"&4Click here to execute the command."}));
		languageKeys.put("ChatListener.CommandSuggestHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick hier um den Befehl in der Chatzeile zu erhalten.",
						"&eClick here to get the command in the chat line."}));
		languageKeys.put("ChatListener.Website.Replacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&fWeb&7seite",
						"&fweb&7site"}));
		languageKeys.put("ChatListener.Website.NotAllowReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&f[&7Zensiert&f]",
						"&f[&7Censord&f]"}));
		languageKeys.put("ChatListener.Website.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlicke hier um diese Webseite zu öffnen.~!~&b",
						"&eClick here to open this web page.~!~&b"}));
		languageKeys.put("ChatListener.Website.NotAllowHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cIn diesem Channel ist das Posten von Webseiten nicht erlaubt.",
						"&cPosting web pages is not allowed in this channel."}));
		languageKeys.put("ChatListener.Mention.YouAreMentionHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest von &f%player% &eerwähnt!",
						"&eYou have been &f%player% &mentioned!"}));
		languageKeys.put("ChatListener.Emoji.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDieses Emoji wurde mit &f%emoji% &egeneriert!",
						"&eThis emoji was generated with &f%emoji%&e!"}));
		
		languageKeys.put("CmdMsg.PrivateChannelsNotActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer PrivateChannel ist global deaktiviert!",
						"&cThe PrivateChannel is globally disabled!"}));
		languageKeys.put("CmdMsg.PleaseEnterAMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte schreibe eine Nachricht mit Inhalt!",
						"&cPlease write a message with content!"}));
		languageKeys.put("CmdMsg.YouHaveNoPrivateMessagePartner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast mit keinem Spieler geschrieben!",
						"&cYou have not written with any player!"}));
		
		/*
		 * INFO:Mail
		 */
		languageKeys.put("CmdMail.Base.NoUnreadMail", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keine ungelesenen Mails!",
						"&cYou have no unread mails!"}));
		languageKeys.put("CmdMail.Base.Read.Click", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&bRead&7]",
						"&7[&bRead&7]"}));
		languageKeys.put("CmdMail.Base.Read.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick hier um die Mail zu lesen.",
						"&eClick here to read the mail."}));
		languageKeys.put("CmdMail.Base.SendPlus.Click", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSend&a+CC&7]",
						"&7[&cSend&a+CC&7]"}));
		languageKeys.put("CmdMail.Base.SendPlus.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick hier um eine Antwort an alle, Verfasser sowie CC, zu schreiben.",
						"&eClick here to write a reply to all, authors as well as CC."}));
		languageKeys.put("CmdMail.Base.SendMinus.Click", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cSend&4-CC&7]",
						"&7[&cSend&4-CC&7]"}));
		languageKeys.put("CmdMail.Base.SendMinus.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick hier um eine Antwort nur an Verfasser zu schreiben.",
						"&eClick here to write a reply to author only."}));
		languageKeys.put("CmdMail.Base.Subject.Text", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						" &6{&f%sender%&6} &d>> &r%subject%",
						" &6{&f%sender%&6} &d>> &r%subject%"}));
		languageKeys.put("CmdMail.Base.Subject.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eGesendet am &f%sendeddate%~!~&cCC: &f%cc%",
						"&eSended on the &f%sendeddate%~!~&cCC: &f%cc%"}));
		languageKeys.put("CmdMail.Base.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e===== &b%mailscount% &fUngelesene Nachrichten&e=====",
						"&e===== &b%mailscount% &fUnreaded messages&e====="}));
		//LastMails
		languageKeys.put("CmdMail.LastMails.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&cSeite %page% &fder letzten Mails von &b%player%&e=====",
						"&e=====&cSeite %page% &fthe last mails von &b%player%&e====="}));
		//Read
		languageKeys.put("CmdMail.Read.MailNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDiese Mail existiert nicht!",
						"&cThis mail does not exist!"}));
		languageKeys.put("CmdMail.Read.CannotReadOthersMails", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu darfst diese Mail nicht lesen, das sie nicht für dich adressiert ist!",
						"&cYou must not read this mail, it is not addressed to you!"}));
		languageKeys.put("CmdMail.Read.NoChannelIsNullChannel",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDeine Mail kann nicht verarbeitet werden, da der Channel, welche für das Verarbeiten der Mailnachricht nicht existiert!",
						"&cYour mail cannot be processed, because the channel which is used for processing the mail message does not exist!"}));
		languageKeys.put("CmdMail.Read.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e==========&7[&cMail &f%id%&7]&e==========",
						"&e==========&7[&cMail &f%id%&7]&e=========="}));
		languageKeys.put("CmdMail.Read.Sender", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cVon: &f%sender%",
						"&cFrom: &f%sender%"}));
		languageKeys.put("CmdMail.Read.CC", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cCC: &7[&f%cc%&7]",
						"&cCC: &7[&f%cc%&7]"}));
		languageKeys.put("CmdMail.Read.Subject", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBetreff: &r%subject%",
						"&cSubject: &r%subject%"}));
		languageKeys.put("CmdMail.Read.Bottomline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e==========&7[&cMail Ende&7]&e==========",
						"&e==========&7[&cMail End&7]&e=========="}));
		//Send
		languageKeys.put("CmdMail.Send.PlayerNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEiner der angegebenen Empfänger existiert nicht!",
						"&cOne of the specified recipients does not exist!"}));
		languageKeys.put("CmdMail.Send.OneWordMinimum", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte gebe mindestens 1 Wort als Nachricht an!",
						"&cPlease enter at least 1 word as message!"}));
		languageKeys.put("CmdMail.Send.Sended", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast eine Mail geschrieben.!",
						"&eYou have written an mail!"}));
		languageKeys.put("CmdMail.Send.SendedHover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eBetreff: &r%subject%~!~&cCC: &r%cc%",
						"&eSubject: &r%subject%~!~&cCC: &r%cc%"}));
		languageKeys.put("CmdMail.Send.HasNewMail", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&bMail&7] &eDu hast eine neue Mail!",
						"&7[&bMail&7] &eYou have a new mail!"}));
		languageKeys.put("CmdMail.Send.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick auf die Nachricht um all deine neuen Mails zu sehen!",
						"&eClick on the message to see all your new mails!"}));
		
		/*
		 * INFO:Scc
		 */
		languageKeys.put("CmdScc.OtherCmd", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBitte nutze den Befehl, mit einem weiteren Argument aus der Tabliste!",
						"&cPlease use the command, with another argument from the tab list!"}));
		languageKeys.put("CmdScc.UsedChannelForBroadCastDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDen in der config.yml gewählten Channel für einen Broadcast existiert nicht!",
						"&cThe channel selected in config.yml for a broadcast does not exist!"}));
		//Book
		languageKeys.put("CmdScc.Book.IsNotABook"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Item ist kein signiertes Buch!",
						"&cThe item is not a signed book!"}));
		//Broadcast
		languageKeys.put("CmdScc.Broadcast.Intro"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&7[&cINFO&7] &r",
						"&7[&cINFO&7] &r"}));
		//Channel
		languageKeys.put("CmdScc.Channel.ChannelDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer angegeben Channel existiert nicht!",
						"&cThe specified channel does not exist!"}));
		languageKeys.put("CmdScc.Channel.UsedChannelDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst den angegeben Channel nicht ändern, da du in diesen garnicht schreiben darfst!",
						"&cYou can not change the specified channel, because you are not allowed to write in it!"}));
		languageKeys.put("CmdScc.Channel.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Channel &a%channel% &eangeschaltet!",
						"&eYou have switched on the &a%channel% &echannel!"}));
		languageKeys.put("CmdScc.Channel.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Channel &c%channel% &eausgeschaltet!",
						"&eYou have turned off the &c%channel% &echannel!"}));
		
		languageKeys.put("CmdScc.ChannelGui.InvTitle"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"§c%player% §eChannels",
						"§c%player% §eChannels"}));
		//Ignore
		languageKeys.put("CmdScc.Ignore.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu ignorierst nun den Spieler &c%player%&e!",
						"&eYou now ignore the player &c%player%&e!"}));
		languageKeys.put("CmdScc.Ignore.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu ignorierst nun nicht mehr den Spieler &a%player%&e!",
						"&eYou are now no longer ignoring the player &a%player%&e!"}));
		languageKeys.put("CmdScc.Ignore.NoOne"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu ignorierst keinen Spieler!",
						"&eYou dont ignore any player!"}));
		languageKeys.put("CmdScc.Ignore.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eKlick hier um den Spieler nicht mehr zu ignorieren!",
						"&eClick here to stop ignoring the player!"}));
		languageKeys.put("CmdScc.Ignore.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e===&bIgnorier Liste von &f%player%&e===",
						"&e===&bIgnore list from &f%player%&e==="}));
		
		//Mute
		languageKeys.put("CmdScc.Mute.YouHaveBeenMuted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu wurdest bis zum &f%time% &cgemutet!",
						"&cYou have been muted to &f%time%&c!"}));
		languageKeys.put("CmdScc.Mute.YouhaveMuteThePlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &c%player% &ebis zum &f%time% &egemutet!",
						"&eYou have muted &c%player% &eto the &f%time%&e!"}));
		languageKeys.put("CmdScc.Mute.PlayerMute"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &c%target% &ewurden von &f%player% &ebis zum &f%time% &egemutet!",
						"&eThe player &c%target% &ehas been muted from &f%player% &eto &f%time%&e!"}));
		languageKeys.put("CmdScc.Mute.YouHaveBeenUnmute"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu kannst dich wieder am Chat beteiligen!",
						"&eYou can join the chat again!"}));
		languageKeys.put("CmdScc.Mute.YouHaveUnmute"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den &f%player% &eunmutet!",
						"&eYou have the &f%player% &eunmuted!"}));
		languageKeys.put("CmdScc.Mute.PlayerUnmute"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ekann wieder reden.",
						"&eThe &f%player%&ecan talk again."}));
		languageKeys.put("CmdScc.Performance.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&cScc MysqlPerformance &f<%time%>&7]&e=====",
						"&e=====&7[&cScc MysqlPerformance &f<%time%>&7]&e====="}));
		languageKeys.put("CmdScc.Performance.Text"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&4%server% &e>> &6Inserts:&f%insert% &bUpdates:&f%update% &cDeletes:&f%delete% &eReads:&f%read%",
						"&4%server% &e>> &6Inserts:&f%insert% &bUpdates:&f%update% &cDeletes:&f%delete% &eReads:&f%read%"}));
		
		//Option
		languageKeys.put("CmdScc.Option.Channel.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu siehst nun alle Aktiven Channels beim Login.",
						"&eYou will now see all active channels when you log in."}));
		languageKeys.put("CmdScc.Option.Channel.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu siehst nun nicht mehr alle Aktiven Channels beim Login.",
						"&eYou no longer see all active channels when you log in."}));
		languageKeys.put("CmdScc.Option.Join.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu siehst nun die Nachricht, wenn Spieler den Server verlassen oder joinen.",
						"&eYou will now see the message when players leave or join the server."}));
		languageKeys.put("CmdScc.Option.Join.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu siehst nun die Nachricht nicht mehr, wenn Spieler den Server verlassen oder joinen.",
						"&eYou no longer see the message when players leave or join the server."}));
		languageKeys.put("CmdScc.Option.Spy.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu siehst nun alle Chatnachrichten, die dir sonst für dich verborgen wären.",
						"&eYou will now see all chat messages that would otherwise be hidden to you."}));
		languageKeys.put("CmdScc.Option.Spy.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu siehst nun nur noch die Chatnachrichten, wozu du auch berechtig bist.",
						"&eYou will now only see the chat messages that you are authorized to see."}));
		//ItemReplacer
		languageKeys.put("CmdScc.Item.InvTitle"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"§c%player% §eReplacer §6Items",
						"§c%player% §eReplacer §6Items"}));
		languageKeys.put("CmdScc.Item.YouCannotSaveItems"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst keine Items vorspeicher!",
						"&cYou can't pre-store items!"}));
		languageKeys.put("CmdScc.Item.Rename.NotDefault"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer alte oder neue Name darf nicht &fdefault &cheißen!",
						"&cThe old or new name must not be &fdefault&c!"}));
		languageKeys.put("CmdScc.Item.Rename.NameAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Name ist schon vergeben!",
						"&cThe name is already taken!"}));
		languageKeys.put("CmdScc.Item.Rename.Renamed"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDas Item mit dem Name &f%oldname% &ewurde in &f%newname% &eumbenannt!",
						"&eThe item with the name &f%oldname% &has been renamed to &f%newname%!"}));
		languageKeys.put("CmdScc.Item.Replacers.ListEmpty"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast keine ItemReplacer!",
						"&cYou have no ItemReplacer!"}));
		languageKeys.put("CmdScc.Item.Replacers.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&7[&bItemReplacer&7]&e=====",
						"&e=====&7[&bItemReplacer&7]&e====="}));
		
		//INFO:PermanentChannel
		languageKeys.put("CmdScc.PermanentChannel.YouAreNotInAChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist in keinem permanenten Channel!",
						"&cYou are not in a permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.YouAreNotTheOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist nicht der Ersteller in diesem permanenten Channel!",
						"&cYou are not the creator in this permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist weder der Ersteller noch ein Stellvertreter in diesem permanenten Channel!",
						"&cYou are neither the creator nor an vice in this permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.NotAChannelMember"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer angegebene Spieler ist nicht Mitglied im permanenten Channel!",
						"&cThe specified player is not a member of the permanent channel!"}));
		//Ban
		languageKeys.put("CmdScc.PermanentChannel.Ban.ViceCannotBanCreator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst als Stellvertreter den Ersteller nicht bannen!",
						"&cYou cant ban the creator as a vice!"}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.OwnerCantSelfBan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Ersteller kann nicht gebannt werden!",
						"&cThe creator can not be banned!"}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.AlreadyBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler ist schon auf der gebannt!",
						"&cThe player is already on the banned!"}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.YouHasBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Spieler &f%player% &eaus dem &5Perma&fnenten &eChannel verbannt.",
						"&eYou have banned the &f%player% &efrom the &5perma&fnent &eChannel."}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.YourWereBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu wurdest vom Permanenten Channel &r%channel% &cverbannt!",
						"&cYou were banned from the Permanent Channel &r%channel%&c!"}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.PlayerWasBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde aus dem &5Perma&fnenten &eChannel verbannt.",
						"&eThe player &f%player% &ehas been banned from the &5perma&fnent &eChannel."}));
		languageKeys.put("CmdScc.PermanentChannel.Unban.PlayerNotBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler ist nicht gebannt!",
						"&cThe player is not banned!"}));
		languageKeys.put("CmdScc.PermanentChannel.Unban.YouUnbanPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%player% &efür den &5Perma&fnenten &eChannel entbannt!",
						"&eYou have unbanned &f%player% &efor the &5perma&fnent &eChannel!"}));
		languageKeys.put("CmdScc.PermanentChannel.Unban.PlayerWasUnbanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde für den &5Perma&fnenten &eChannel &r%channel% &r&eentbannt.",
						"&eThe player &f%player% &ehas been banned for the &5perma&fnent &eChannel &r%channel%&e."}));
		//ChangePassword
		languageKeys.put("CmdScc.PermanentChannel.ChangePassword.Success"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde für den &5Perma&fnenten &eChannel &r%channel% &r&eentbannt.",
						"&eThe player &f%player% &ehas been banned for the &5perma&fnent &eChannel &r%channel%&e."}));
		//Channels
		languageKeys.put("CmdScc.PermanentChannel.Channels.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&5[&5Perma&fnente &fChannels&5]&e=====",
						"&e=====&5[&5Perma&fnente &fChannels&5]&e====="}));
		//ChatColor
		languageKeys.put("CmdScc.PermanentChannel.ChatColor.NewColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Farben vom Chat des Channels &r%channel% &ewurden in &f%color%Beispielnachricht &r&egeändert.",
						"&eThe colors of the channel chat &r%channel% &ehave been changed to &r%color%example message &r&e."}));
		//Create
		languageKeys.put("CmdScc.PermanentChannel.Create.ChannelNameAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDieser Name existiert bereits für einen Permanente Channel!",
						"&cThis name already exists for a permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.Create.MaximumAmount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon die maximale Anzahl von Permanenten Channels erstellt. Lösche vorher einen von deinen Permanenten Channels, um einen neuen zu erstellen!",
						"&cYou have already created the maximum number of permanent channels. Delete one of your permanent channels before to create a new one!"}));
		languageKeys.put("CmdScc.PermanentChannel.Create.ChannelCreateWithoutPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den &5Perma&fnenten &eChannel &r%channel%&r &eerstellt! Zum Schreiben am Anfang &f%symbol% &enutzen.",
						"&eYou have created the &5perma&fnent &eChannel &r%channel%&r! To write at the beginning &f%symbol% &use."}));
		languageKeys.put("CmdScc.PermanentChannel.Create.ChannelCreateWithPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den &5Perma&fnenten &eChannel &r%channel%&r &emit dem Passwort &f%password% &eerstellt! Zum Schreiben am Anfang &f%symbol% &enutzen.",
						"&eYou have &created the &5perma&fnent &eChannel &r%channel%&r &with the password &f%password%! To write at the beginning &f%symbol% &use."}));
		//Delete
		languageKeys.put("CmdScc.PermanentChannel.Delete.Confirm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cBist du sicher, dass du den Permanenten Channel &r%channel% &r&clöschen willst? Wenn ja, klicke auf diese Nachricht.",
						"&cAre you sure you want to delete the Permanent Channel &r%channel%&c? If yes, click on this message."}));
		languageKeys.put("CmdScc.PermanentChannel.Delete.Deleted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Permanente Channel &r%channel% &r&cwurde von %player% gelöscht. Alle Mitglieder verlassen somit diesen Channel.",
						"&cThe permanent channel &r%channel% &r&chas been deleted by %player%. All members leave this channel."}));
		//Info
		languageKeys.put("CmdScc.PermanentChannel.Info.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&5[&5Perma&fnenter &fChannel &r%channel%&r&5]&e=====",
						"&e=====&5[&5Perma&fnent &fChannel &r%channel%&r&5]&e====="}));
		languageKeys.put("CmdScc.PermanentChannel.Info.ID"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel ID: &f%id%",
						"&eChannel ID: &f%id%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Creator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Ersteller: &f%creator%",
						"&eChannel creator: &f%creator%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Vice"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Stellvertreter: &f%vice%",
						"&eChannel vice: &f%vice%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Members"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Mitglieder: &f%members%",
						"&eChannel members: &f%members%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Password"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Passwort: &f%password%",
						"&eChannel password: &f%password%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Symbol: &f%symbol%",
						"&eChannel symbol: &f%symbol%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.ChatColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Chat Farben: &f%color% Beispielnachricht",
						"&eChannel chat colors: &f%color% example message"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.NameColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Farben: &f%color%",
						"&eChannel colors: &f%color%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Gebannte Spieler: &f%banned%",
						"&eChannel banned players: &f%banned%"}));
		//Inherit
		languageKeys.put("CmdScc.PermanentChannel.Inherit.NewCreator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eIm &5Perma&fnenten &eChannel &r%channel% &r&ebeerbt der Spieler &a%creator% &eden Spieler &c%oldcreator% &eals neuer Ersteller des Channels.",
						"&eIn the &5perma&fnent &eChannel &r%channel% &r&einherits the player &a%creator% &the player &c%oldcreator% &eas the new creator of the channel."}));
		//Invite
		languageKeys.put("CmdScc.PermanentChannel.Invite.Cooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon in der letzten Zeit jemanden eingeladen! Bitte warte bis %time%, um die nächsten Einladung zu verschicken!",
						"&cYou have already invited someone in the last time! Please wait until %time% to send the next invitation!"}));
		languageKeys.put("CmdScc.PermanentChannel.Invite.SendInvite"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Spieler &6%target% &ein den &5Perma&fnenten &eChannel &r%channel% &r&aeingeladen.",
						"&eYou have invited the player &6%target% &einto the &5perma&fnent &eChannel &r%channel%&r&e."}));
		languageKeys.put("CmdScc.PermanentChannel.Invite.Invitation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest vom Spieler &6%player% &ein den &5Perma&fnenten &eChannel &r%channel% &r&aeingeladen. Klicke auf die Nachricht zum Betreten des Channels.",
						"&eYou have been invited by the &6%player% &into the &5perma&fnent &eChannel &r%channel%&r&e. Click on the message to enter the channel."}));
		//Join
		languageKeys.put("CmdScc.PermanentChannel.Join.UnknownChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs gibt keinen Permanenten Channel mit dem Namen &f%name%&c!",
						"&cThere is no permanent channel with the name &f%name%&c!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist in diesem Permanenten Channel gebannt und darfst nicht beitreten!",
						"&cYou are banned in this permanent channel and are not allowed to join!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.AlreadyInTheChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist schon diesem Permanenten Channel beigetreten!",
						"&cYou have already joined this permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.ChannelHasPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Permanente Channel hat ein Passwort, bitte gib dieses beim Beitreten an!",
						"&cThe Permanent Channel has a password, please enter it when joining!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.PasswordIncorrect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas angegebene Passwort ist nicht korrekt!",
						"&cThe specified password is not correct!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.ChannelJoined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bist dem &5Perma&fnenten &eChannel &r%channel%&r &abeigetreten&e! ChannelSymbol: &r%symbol%",
						"&eYou have joined the &5Perma&fnent &eChannel &r%channel%&r&e! ChannelSymbol: &r%symbol%"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.PlayerIsJoined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eSpieler &f%player% &eist dem &5Perma&fnenten &eChannel &r%channel% &r&ebeigetreten!",
						"&ePlayer &f%player% &ehas joined the &5Perma&fnent &eChannel &r%channel%&r&e!"}));
		//Kick
		languageKeys.put("CmdScc.PermanentChannel.Kick.ViceCannotKickCreator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst als Stellvertreter den Ersteller nicht kicken!",
						"&cYou can't kick the creator as a vice!"}));
		languageKeys.put("CmdScc.PermanentChannel.Kick.CannotSelfKick"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu kannst dich nicht kicken!",
						"&cYou can't kick yourself!"}));
		languageKeys.put("CmdScc.PermanentChannel.Kick.YouWereKicked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu wurdest aus dem Permanenten &eChannel &r%channel%&r &cgekickt!",
						"&cYou have been kicked out of the Permanent &eChannel &r%channel%&r&c!"}));
		languageKeys.put("CmdScc.PermanentChannel.Kick.YouKicked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%player% &eaus dem &5Perma&fnenten &eChannel &r%channel%&r &egekickt!",
						"&eYou have &f%player% &e kicked out of the &5perma&fnent &eChannel &r%channel%&r &egekickt!"}));
		languageKeys.put("CmdScc.PermanentChannel.Kick.KickedSomeone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde aus dem &5Perma&fnenten &eChannel &r%channel%&r &egekickt!",
						"&The player &f%player% &has been kicked out of the &5perma&fnent &eChannel &r%channel%&r &egekickt!"}));
		//Leave
		languageKeys.put("CmdScc.PermanentChannel.Leave.Confirm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&c&lAchtung! &r&cBist du sicher, dass du den Channel verlassen willst? Wenn der Ersteller den Permanenten Channel verlässt, wird dieser gelöscht! Bitte bestätigen mit dem Klick auf diese Nachricht.",
						"&c&lAttention! &r&cAre you sure you want to leave the channel? If the creator leaves the permanent channel, it will be deleted! Please confirm and just click on this message."}));
		languageKeys.put("CmdScc.PermanentChannel.Leave.CreatorLeft"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Ersteller hat den Channel verlassen und ihn somit aufgelöst. Alle Mitglieder haben somit den Permanenten Channel &r%channel%&r &cverlassen!",
						"&cThe creator has left the channel and thus dissolved it. All members have left the permanent channel &r%channel%&r &c!"}));
		languageKeys.put("CmdScc.PermanentChannel.Leave.YouLeft"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den &5Perma&fnenten &eChannel &r%channel%&r &everlassen!",
						"&eYou have left the &5perma&fnent &eChannel &r%channel%&r &ever!"}));
		languageKeys.put("CmdScc.PermanentChannel.Leave.PlayerLeft"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler &f%player% &chat den permanenten Channel &r%channel%&r &cverlassen!",
						"&cThe player &f%player% &chas left the permanent channel &r%channel%&r &c!"}));
		//NameColor
		languageKeys.put("CmdScc.PermanentChannel.NameColor.NewColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDie Farben vom Namen des &5Perma&fnenten Channels &r%channel%&r &ewurden in &f%color%Beispielnachricht &r&egeändert.",
						"&The colors from the name of the &5perma&fnent channel &r%channel%&r &have been changed to &f%color%example message &r&e."}));
		//Player
		languageKeys.put("CmdScc.PermanentChannel.Player.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&5[&5Perma&fnente &fChannels von &6%player%&5]&e=====",
						"&e=====&5[&5Perma&fnent &fchannels from &6%player%&5]&e====="}));
		languageKeys.put("CmdScc.PermanentChannel.Player.Creator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eIst Ersteller von: &r%creator%",
						"&eIs creator from: &r%creator%"}));
		languageKeys.put("CmdScc.PermanentChannel.Player.Vice"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eIst Vertreter bei: &r%vice%",
						"&eIs vice in: &r%vice%"}));
		languageKeys.put("CmdScc.PermanentChannel.Player.Member"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eIst Mitglied bei: &r%member%",
						"&eIs member in: &r%member%"}));
		languageKeys.put("CmdScc.PermanentChannel.Player.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eIst Gebannt bei: &r%banned%",
						"&eIs banned in: &r%banned%"}));
		//Rename
		languageKeys.put("CmdScc.PermanentChannel.Rename.NameAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs gibt schon einen Permanenten Channel &r%channel%&r&c!",
						"&cThere is already a Permanent Channel &r%channel%&r&c!"}));
		languageKeys.put("CmdScc.PermanentChannel.Rename.Renaming"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer &5Perma&fnente &eChannel &r%oldchannel% &r&ewurde in &r%channel% &r&eumbenannt.",
						"&eThe &5Perma&fnente &eChannel &r%oldchannel% &r&has been renamed &r%channel&r&e."}));
		//Symbol
		languageKeys.put("CmdScc.PermanentChannel.Symbol.SymbolAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas Symbol &f%symbol% &cwird schon von dem Permanenten Channel &r%channel%&r &cbenutzt!",
						"&cThe symbol &f%symbol% &cis already used by the Permanent Channel &r%channel%&r&c!"}));
		languageKeys.put("CmdScc.PermanentChannel.Symbol.NewSymbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eFür den &5Perma&fnenten &eChannel &r%channel%&r &egibt es ein neues Symbol: &f%symbol%",
						"&eFor the &5perma&fnent &eChannel &r%channel%&r &there is a new symbol: &f%symbol%"}));
		//Vice
		languageKeys.put("CmdScc.PermanentChannel.Vice.Degraded"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde zum Mitglied im &5Perma&fnenten &eChannel &r%channel%&r &cdegradiert&e!",
						"&The player &f%player% &has been &degraded&e to a member of the &5perma&fnent &eChannel &r%channel%&r&c!"}));
		languageKeys.put("CmdScc.PermanentChannel.Vice.Promoted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde zum Stellvertreter im &5Perma&fnenten &eChannel &r%channel%&r &abefördert&e!",
						"&Tehe player &f%player% &has been &promoted&e to vice in the &5perma&fnent &eChannel &r%channel%&r!"}));
		
		//INFO:TemproraryChannel
		languageKeys.put("CmdScc.TemporaryChannel.YouAreNotInAChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist in keinem temporären Channel!",
						"&cYou are not in a temporary channel!"}));
		languageKeys.put("CmdScc.TemporaryChannel.YouAreNotTheOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist nicht der Ersteller in diesem temporären Channel!",
						"&cYou are not the creator in this temporary channel!"}));
		languageKeys.put("CmdScc.TemporaryChannel.NotAChannelMember"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer angegebene Spieler ist nicht Mitglied im temporären Channel!",
						"&cThe specified player is not a member of the temporary Channel!"}));
		//Ban
		languageKeys.put("CmdScc.TemporaryChannel.Ban.CreatorCannotSelfBan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu als Ersteller kannst dich nicht selber bannen!",
						"&cYou as the creator can not ban yourself!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.AlreadyBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler ist schon gebannt!",
						"&cThe player is already banned!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.YouHasBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Spieler &f%player% &eaus dem &5Temp&forären &eChannel verbannt.",
						"&eYou have banned the &f%player% &efrom the &5temp&forary &eChannel."}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.YourWereBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu wurdest vom Temporären Channel &f%channel% &cverbannt!",
						"&cYou have been &f%channel% &cbanned from the Temporary Channel!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.CreatorHasBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde aus dem &5temp&forären &eChannel verbannt.",
						"&eThe player &f%player% &has been banned from the &5temp&forary &eChannel."}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.PlayerNotBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Spieler ist nicht gebannt!",
						"&cThe player is not banned!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.YouUnbanPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%player% &efür den &5temp&forären &eChannel entbannt!",
						"&eYou have unbanned &f%player% &for the &5temp&forary &eChannel!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.CreatorUnbanPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde für den &5Temp&forären &eChannel entbannt.",
						"&The player &f%player% &ehas been unbanned for the &5temp&forary &eChannel."}));
		//ChangePassword
		languageKeys.put("CmdScc.TemporaryChannel.ChangePassword.PasswordChange"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast das Passwort zu &f%password% &egeändert!",
						"&eYou have changed the password to &f%password%!"}));
		//Create
		languageKeys.put("CmdScc.TemporaryChannel.Create.AlreadyInAChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist schon in dem Temporären Channel &f%channel%&c! Um einen neuen Temporären Channel zu eröffnen, müsst du den vorherigen erst schließen.",
						"&cYou are already in the temporary channel &f%channel%&c! To open a new temporary channel, you must first close the previous one."}));
		languageKeys.put("CmdScc.TemporaryChannel.Create.ChannelCreateWithoutPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den &5temp&forären &eChannel &f%channel% &eerstellt!",
						"&eYou have set the &5temp&for &eChannel &f%channel%&e!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Create.ChannelCreateWithPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den &5Temp&forären &eChannel &f%channel% &emit dem Passwort &f%password% &eerstellt!",
						"&eYou have created the &5Temp&for &eChannel &f%channel% &with the password &f%password%!"}));
		//Info
		languageKeys.put("CmdScc.TemporaryChannel.Info.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&e=====&5[&5Temp&forären &fChannel &6%channel%&5]&e=====",
						"&e=====&5[&5Temp&forary &fChannel &6%channel%&5]&e====="}));
		languageKeys.put("CmdScc.TemporaryChannel.Info.Creator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Ersteller: &f%creator%",
						"&eChannel creator: &f%creator%"}));
		languageKeys.put("CmdScc.TemporaryChannel.Info.Members"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Mitglieder: &f%members%",
						"&eChannel members: &f%members%"}));
		languageKeys.put("CmdScc.TemporaryChannel.Info.Password"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Passwort: &f%password%",
						"&eChannel password: &f%password%"}));
		languageKeys.put("CmdScc.TemporaryChannel.Info.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eChannel Gebannte Spieler: &f%banned%",
						"&eChannel banned players: &f%banned%"}));
		//Invite
		languageKeys.put("CmdScc.TemporaryChannel.Invite.Cooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu hast schon in der letzten Zeit jemanden eingeladen! Bitte warte etwas bis zur nächsten Einladung!",
						"&cYou have already invited someone in the last time! Please wait a little until the next invitation!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Invite.SendInvite"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Spieler &6%target% &ein den &5temp&forären &eChannel &6%channel% &aeingeladen.",
						"&eYou have invited the player &6%target% &into the &5temp&forary &eChannel &6%channel%&e."}));
		languageKeys.put("CmdScc.TemporaryChannel.Invite.Invitation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest vom Spieler &6%player% &ein den &5temp&forären &eChannel &6%channel% &aeingeladen. Klicke auf die Nachricht zum Betreten des Channels.",
						"&eYou have been invited by the player &6%player% &into the &5temp&forary &eChannel &6%channel%&e. Click on the message to enter the channel."}));
		//Join
		languageKeys.put("CmdScc.TemporaryChannel.Join.AlreadyInAChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist schon in einem anderen Temporären Channel beigetreten, verlasse erst diesen!",
						"&cYou have already joined another temporary channel, leave this one first!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.UnknownChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cEs gibt keinen Temporären Channel mit dem Namen &f%name%&c!",
						"&cThere is no temporary channel with the name &f%name%&c!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu bist in diesem Temporären Channel gebannt und darfst nicht beitreten!",
						"&cYou are banned in this temporary channel and are not allowed to join!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.ChannelHasPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDer Temporäre Channel hat ein Passwort, bitte gib dieses beim Beitreten an!",
						"&cThe Temporary Channel has a password, please enter it when joining!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.PasswordIncorrect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDas angegebene Passwort ist nicht korrekt!",
						"&cThe specified password is not correct!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.ChannelJoined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu bist dem &5temp&forären &eChannel &f%channel% &abeigetreten!",
						"&eYou have joined the &5temp&forary &eChannel &f%channel%!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.PlayerIsJoined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eSpieler &f%player% &eist dem &5temp&forären &eChannel beigetreten!",
						"&ePlayer &f%player% &has joined the &5temp&forary &eChannel!"}));
		//Kick
		languageKeys.put("CmdScc.TemporaryChannel.Kick.CreatorCannotSelfKick"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu als Ersteller kannst dich nicht kicken!",
						"&cYou as the creator can not kick you!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Kick.YouKicked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast &f%player% &eaus dem &5temp&forären &eChannel %channel% gekickt!",
						"&eYou have kicked &f%player% &eout of the &5temp&forary &eChannel %channel%!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Kick.YouWereKicked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&cDu wurdest aus dem temporären Channel &f%channel% &cgekickt!",
						"&cYou have been kicked out of the Temporary Channel &f%channel%&c!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Kick.CreatorKickedSomeone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ewurde aus dem &5temp&forären &eChannel %channel% gekickt!",
						"&The player &f%player% &ehas been kicked out of the &5temp&forär &eChannel %channel%!"}));
		//Leave
		languageKeys.put("CmdScc.TemporaryChannel.Leave.NewCreator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu wurdest der neue Ersteller des &5temp&forären &eChannels &f%channel%",
						"&eYou became the new creator of the &5temp&forary &eChannel &f%channel%"}));
		languageKeys.put("CmdScc.TemporaryChannel.Leave.YouLeft"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den &5temp&forären &eChannel &f%channel% &everlassen!",
						"&eYou have left the &5temp&forary &eChannel &f%channel% &eever!"}));
		
		//UpdatePlayer
		languageKeys.put("CmdScc.UpdatePlayer.IsUpdated"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDu hast den Spieler &f%player% &eneu bewerten lassen! Seine aktiven Channels sind nach seinen Permission neu eingestellt worden.",
						"&eYou have had the player &f%player% &revaluated! His active channels have been reset after his permission."}));
		languageKeys.put("CmdScc.UpdatePlayer.YouWasUpdated"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"&eDer Spieler &f%player% &ehat deine aktiven Channels nach deinen Permission neu einstellen lassen.",
						"&eThe player &f%player% &ehas your active channels reset according to your permission."}));
		/*languageKeys.put(""
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"",
				""}));*/
		languageKeys.put("CmdScc.TemporaryChannel.Leave."
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						""}));
	}
	
	public void initChatTitle() //INFO:ChatTitle
	{
		chatTitleKeys.put("admin.UniqueIdentifierName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Admin"}));
		chatTitleKeys.put("admin.IsPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		chatTitleKeys.put("admin.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[&4Admin&7]"}));
		chatTitleKeys.put("admin.InChatColorCode"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&4"}));
		chatTitleKeys.put("admin.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"&eDie Admins sind für die administrative Arbeit auf dem Server zuständig.~!~Für Hilfe im Spielbetrieb sind sie aber die letzte Instanz.",
				"&eThe admins are responsible for the administrative work on the server.~!~But for help in the game operation they are the last instance."}));
		chatTitleKeys.put("admin.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.title.admin"}));
		chatTitleKeys.put("admin.Weight"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000}));
	}
	
	public void initChannels() //INFO:Channels
	{
		channelsKeys.put("private.UniqueIdentifierName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Private"}));
		channelsKeys.put("private.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"/msg"}));
		channelsKeys.put("private.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e[Private]"}));
		channelsKeys.put("private.InChatColorMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&d"}));
		channelsKeys.put("private.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.channel.private"}));
		channelsKeys.put("private.JoinPart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&ePrivate &7= /msg"}));
		channelsKeys.put("private.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[%time%&7] %playername_with_prefixhighcolorcode% &e>> %other_playername_with_prefixhighcolorcode% &7: %message%"}));
		channelsKeys.put("private.MinimumTimeBetweenMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				500}));
		channelsKeys.put("private.MinimumTimeBetweenSameMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000}));
		channelsKeys.put("private.PercentOfSimiliarityOrLess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				75.0}));
		channelsKeys.put("private.TimeColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7"}));
		channelsKeys.put("private.PlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("private.OtherPlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("private.SeperatorBetweenPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				" "}));
		channelsKeys.put("private.SeperatorBetweenSuffix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				" "}));
		channelsKeys.put("private.MentionSound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENTITY_WANDERING_TRADER_REAPPEARED"}));
		channelsKeys.put("private.ServerConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"proxy;&2BungeeCord;/warp spawn;&eDer Proxy ist der Verwalter aller Spigotserver.",
				"hub;&aHub;/warp hub;&eVom Hub kommst du zu alle~!~&eandere Server.",
				"nether;&cNether;/warp nether;&cDie Hölle"}));
		channelsKeys.put("private.WorldConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"spawn;&aSpawn;/warp spawn;&eVom Spawn kommst du zu alle~!~&eandere Server.",
				"nether;&cNether;/warp nether;&cDie Hölle"}));
		channelsKeys.put("private.UseColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("private.UseItemReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("private.UseBookReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("private.UseRunCommandReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("private.UseSuggestCommandReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("private.UseWebsiteReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("private.UseEmojiReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("private.UsePositionReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		
		channelsKeys.put("permanent.UniqueIdentifierName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Permanent"}));
		channelsKeys.put("permanent.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"."}));
		channelsKeys.put("permanent.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&d[%channel%&d]"}));
		channelsKeys.put("permanent.InChatColorMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&f"}));
		channelsKeys.put("permanent.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.channel.permanent"}));
		channelsKeys.put("permanent.JoinPart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&dPerma&7nent &7= ."}));
		channelsKeys.put("permanent.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[%time%&7] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%&7: %message%"}));
		channelsKeys.put("permanent.MinimumTimeBetweenMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				500}));
		channelsKeys.put("permanent.MinimumTimeBetweenSameMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000}));
		channelsKeys.put("permanent.PercentOfSimiliarityOrLess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				75.0}));
		channelsKeys.put("permanent.TimeColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7"}));
		channelsKeys.put("permanent.PlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("permanent.OtherPlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("permanent.SeperatorBetweenPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				" "}));
		channelsKeys.put("permanent.SeperatorBetweenSuffix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				" "}));
		channelsKeys.put("permanent.MentionSound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENTITY_WANDERING_TRADER_REAPPEARED"}));
		channelsKeys.put("permanent.ServerConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"proxy;&2BungeeCord;/warp spawn;&eDer Proxy ist der Verwalter aller Spigotserver.",
				"hub;&aHub;/warp hub;&eVom Hub kommst du zu alle~!~&eandere Server.",
				"nether;&cNether;/warp nether;&cDie Hölle"}));
		channelsKeys.put("permanent.WorldConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"spawn;&aSpawn;/warp spawn;&eVom Spawn kommst du zu alle~!~&eandere Server.",
				"nether;&cNether;/warp nether;&cDie Hölle"}));
		channelsKeys.put("permanent.UseColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UseColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UseItemReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UseBookReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UseRunCommandReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UseSuggestCommandReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UseWebsiteReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UseEmojiReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UseMentionReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.UsePositionReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		
		channelsKeys.put("temporary.UniqueIdentifierName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Temporary"}));
		channelsKeys.put("temporary.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				";"}));
		channelsKeys.put("temporary.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&5[%channel%]"}));
		channelsKeys.put("temporary.InChatColorMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&5"}));
		channelsKeys.put("temporary.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.channel.temporary"}));
		channelsKeys.put("temporary.JoinPart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&5Temporary &7= ;"}));
		channelsKeys.put("temporary.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[%time%&7] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%&7: %message%"}));
		channelsKeys.put("temporary.MinimumTimeBetweenMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				500}));
		channelsKeys.put("temporary.MinimumTimeBetweenSameMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000}));
		channelsKeys.put("temporary.PercentOfSimiliarityOrLess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				75.0}));
		channelsKeys.put("temporary.TimeColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7"}));
		channelsKeys.put("temporary.PlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("temporary.OtherPlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("temporary.SeperatorBetweenPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				" "}));
		channelsKeys.put("temporary.SeperatorBetweenSuffix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				" "}));
		channelsKeys.put("temporary.MentionSound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENTITY_WANDERING_TRADER_REAPPEARED"}));
		channelsKeys.put("temporary.ServerConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"proxy;&2BungeeCord;/warp spawn;&eDer Proxy ist der Verwalter aller Spigotserver.",
				"hub;&aHub;/warp hub;&eVom Hub kommst du zu alle~!~&eandere Server.",
				"nether;&cNether;/warp nether;&cDie Hölle"}));
		channelsKeys.put("temporary.WorldConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"spawn;&aSpawn;/warp spawn;&eVom Spawn kommst du zu alle~!~&eandere Server.",
				"nether;&cNether;/warp nether;&cDie Hölle"}));
		channelsKeys.put("temporary.UseColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.UseItemReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.UseBookReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.UseRunCommandReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.UseSuggestCommandReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.UseWebsiteReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.UseEmojiReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.UseMentionReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.UsePositionReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		
		channelsKeys.put("global.UniqueIdentifierName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Global"}));
		channelsKeys.put("global.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"NULL"}));
		channelsKeys.put("global.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e[G]"}));
		channelsKeys.put("global.InChatColorMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("global.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.channel.global"}));
		channelsKeys.put("global.JoinPart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&eGlobal &7= Without Symbol"}));
		channelsKeys.put("global.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[%time%&7] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%&7: %message%"}));
		channelsKeys.put("global.UseSpecificServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("global.UseSpecificsWorld"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("global.UseBlockRadius"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				0}));
		channelsKeys.put("global.MinimumTimeBetweenMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				500}));
		channelsKeys.put("global.MinimumTimeBetweenSameMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000}));
		channelsKeys.put("global.PercentOfSimiliarityOrLess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				75.0}));
		channelsKeys.put("global.TimeColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7"}));
		channelsKeys.put("global.PlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("global.OtherPlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&e"}));
		channelsKeys.put("global.SeperatorBetweenPrefix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				" "}));
		channelsKeys.put("global.SeperatorBetweenSuffix"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				" "}));
		channelsKeys.put("global.MentionSound"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENTITY_WANDERING_TRADER_REAPPEARED"}));
		channelsKeys.put("global.ServerConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"proxy;&2BungeeCord;/warp spawn;&eDer Proxy ist der Verwalter aller Spigotserver.",
				"hub;&aHub;/warp hub;&eVom Hub kommst du zu alle~!~&eandere Server.",
				"nether;&cNether;/warp nether;&cDie Hölle"}));
		channelsKeys.put("global.WorldConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"spawn;&aSpawn;/warp spawn;&eVom Spawn kommst du zu alle~!~&eandere Server.",
				"nether;&cNether;/warp nether;&cDie Hölle"}));
		channelsKeys.put("global.UseColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		channelsKeys.put("global.UseItemReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		channelsKeys.put("global.UseBookReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		channelsKeys.put("global.UseRunCommandReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		channelsKeys.put("global.UseSuggestCommandReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		channelsKeys.put("global.UseWebsiteReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		channelsKeys.put("global.UseEmojiReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		channelsKeys.put("global.UseMentionReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		channelsKeys.put("global.UsePositionReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
	}
	
	public void initEmojis() //INFO:Emojis
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
		
		emojisKeys.put("umbrella"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"☂"}));
	}
	
	public void initWordFilter() //INFO:Wordfilter
	{
		wordFilterKeys.put("WordFilter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Arschloch",
				"Asshole"}));
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
		if(guiKeys.containsKey(type.toString()))
		{
			LinkedHashMap<String, Language> gui = guiKeys.get(type.toString());
			gui.put(slot+"."+settingLevel.getName()+".Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					displaynameGER,
					displaynameENG}));
			gui.put(slot+"."+settingLevel.getName()+".Function"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					function}));
			gui.put(slot+"."+settingLevel.getName()+".Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					material.toString()}));
			gui.put(slot+"."+settingLevel.getName()+".Amount"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					amount}));
			if(urlTexture != null)
			{
				gui.put(slot+"."+settingLevel.getName()+".PlayerHeadTexture"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					urlTexture}));
			}
			if(itemflag != null)
			{
				gui.put(slot+"."+settingLevel.getName()+".Itemflag"
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						itemflag}));
			}
			if(enchantments != null)
			{
				gui.put(slot+"."+settingLevel.getName()+".Enchantments"
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						enchantments}));
			}
			if(lore != null)
			{
				gui.put(slot+"."+settingLevel.getName()+".Lore"
						, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, lore));
			}
			guiKeys.replace(type.toString(), gui);
		} else
		{
			LinkedHashMap<String, Language> gui = new LinkedHashMap<>();
			gui.put(slot+"."+settingLevel.getName()+".Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					displaynameGER,
					displaynameENG}));
			gui.put(slot+"."+settingLevel.getName()+".Function"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					function}));
			gui.put(slot+"."+settingLevel.getName()+".Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					material.toString()}));
			gui.put(slot+"."+settingLevel.getName()+".Amount"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					amount}));
			if(urlTexture != null)
			{
				gui.put(slot+"."+settingLevel.getName()+".PlayerHeadTexture"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					urlTexture}));
			}
			if(itemflag != null)
			{
				gui.put(slot+"."+settingLevel.getName()+".Itemflag"
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						itemflag}));
			}
			if(enchantments != null)
			{
				gui.put(slot+"."+settingLevel.getName()+".Enchantments"
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						enchantments}));
			}
			if(lore != null)
			{
				gui.put(slot+"."+settingLevel.getName()+".Lore"
						, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, lore));
			}
			guiKeys.put(type.toString(), gui);
		}
	}
	
	/*private void setSlot(GuiType type, String identifier, int slot, String function,
			Material material, int amount, String urlTexture,
			String displaynameGER, String displaynameENG,
			String[] itemflag, String[] enchantments, String[] lore)
	{
		List<SettingsLevel> list =
                new ArrayList<SettingsLevel>(EnumSet.allOf(SettingsLevel.class));
		for(SettingsLevel sl : list)
		{
			setSlot(type, identifier, slot, function, sl, material, amount, urlTexture, displaynameGER, displaynameENG, itemflag, enchantments, lore);
		}
	}*/
	
	public void initGuis() //INFO:Guis
	{
		setSlot(GuiType.CHANNELS, 22, GuiValues.CHANNELGUI_FUNCTION+":Private",
				SettingsLevel.NOLEVEL, org.bukkit.Material.PAPER, 1,
				null,
				"&ePrivatChat: %boolean%",
				"&ePrivateChat: %boolean%",
				null,//Itemflag
				null,//Ench
				null);
	}
}
