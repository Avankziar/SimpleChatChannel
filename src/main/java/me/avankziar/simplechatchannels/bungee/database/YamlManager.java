package main.java.me.avankziar.simplechatchannels.bungee.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.database.Language.ISO639_2B;
import net.md_5.bungee.config.Configuration;

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
	
	public YamlManager()
	{
		initConfig();
		initCommands();
		initLanguage();
		initChatTitle();
		initChannels();
		initEmojis();
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
	
	/*
	 * The main methode to set all paths in the yamls.
	 */
	public void setFileInput(Configuration yml, LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType)
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
				""})); //ADDME
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
		configKeys.put("ChatReplacer.Command.RunCommandStart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"cmd|/"}));
		configKeys.put("ChatReplacer.Command.SuggestCommandStart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"cmd/"}));
		configKeys.put("ChatReplacer.Command.CommandStartReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[&f"}));
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
		configKeys.put("ChatReplacer.Position.Replacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<pos>"}));
		configKeys.put("ChatReplacer.Position.Replace"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[&9%server% &d%world% &e%x% %y% %z%&7]"}));
		
	}
	
	@SuppressWarnings("unused") //INFO:Commands
	public void initCommands()
	{
		comBypass();
		String path = "";
		commandsInput("path", "base", "perm.command.perm", 
				"/base [pagenumber]", "/base ",
				"&c/base &f| Infoseite für alle Befehle.",
				"&c/base &f| Info page for all commands.");
		String basePermission = "perm.base.";
		argumentInput("base_argument", "argument", basePermission,
				"/base argument <id>", "/econ deletelog ",
				"&c/base argument &f| Ein Subbefehl",
				"&c/base argument &f| A Subcommand.");
	}
	
	private void comBypass() //INFO:ComBypass
	{
		String path = "Bypass.";
		commandsKeys.put(path+"Perm1.Perm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"perm.bypass.perm"}));
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
		
		/*
		 * INFO:MSG Command
		 */
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
						"&cDu hast schon in der letzten Zeit jemanden eingeladen! Bitte warte etwas bis zur nächsten Einladung!",
						"&cYou have already invited someone in the last time! Please wait a little until the next invitation!"}));
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
				"&4@5;&c"}));
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
				"&ePrivate &7= "}));
		//FIXME chatformt hat nicht den anderen Spieler
		channelsKeys.put("private.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[%time%] %playername_with_prefixhighcolorcode% %playername_with_prefixhighcolorcode% &r&7: %message%"}));
		channelsKeys.put("private.MinimumTimeBetweenMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				500}));
		channelsKeys.put("private.MinimumTimeBetweenSameMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000}));
		channelsKeys.put("private.PercentOfSimiliarityOrLess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				75.0}));
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
				"&dPerma&7nent &7= "}));
		channelsKeys.put("permanent.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[%time%] &r%channel% &r%prefixall% %playername_with_prefixhighcolorcode% &r%suffixall%&r&7: %message%"}));
		channelsKeys.put("permanent.MinimumTimeBetweenMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				500}));
		channelsKeys.put("permanent.MinimumTimeBetweenSameMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000}));
		channelsKeys.put("permanent.PercentOfSimiliarityOrLess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				75.0}));
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
				"&5Temporary &7= "}));
		channelsKeys.put("temporary.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[%time%] &r%channel% &r%prefixall% %playername_with_prefixhighcolorcode% &r%suffixall%&r&7: %message%"}));
		channelsKeys.put("temporary.MinimumTimeBetweenMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				500}));
		channelsKeys.put("temporary.MinimumTimeBetweenSameMessages"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				1000}));
		channelsKeys.put("temporary.PercentOfSimiliarityOrLess"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				75.0}));
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
				"&eGlobal &7= "}));
		channelsKeys.put("global.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"&7[%time%] &r%channel% &r%prefixall% %playername_with_prefixhighcolorcode% &r%suffixall%&r&7: %message%"}));
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
		/*
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
		
		emojisKeys.put(""
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				""}));
	}
	
	public void initWordFilter() //INFO:Wordfilter
	{
		wordFilterKeys.put("WordFilter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"Arschloch",
				"Asshole"}));
	}
}
