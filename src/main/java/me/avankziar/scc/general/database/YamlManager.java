package main.java.me.avankziar.scc.general.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import main.java.me.avankziar.scc.general.database.Language.ISO639_2B;
import main.java.me.avankziar.scc.spigot.guihandling.GUIApi.SettingsLevel;
import main.java.me.avankziar.scc.spigot.guihandling.GuiValues;

public class YamlManager
{
	public enum Type
	{
		BUNGEE, SPIGOT, VELO;
	}
	
	private ISO639_2B languageType = ISO639_2B.GER;
	//The default language of your plugin. Mine is german.
	private ISO639_2B defaultLanguageType = ISO639_2B.GER;
	private Type type;
	
	//Per Flatfile a linkedhashmap.
	private static LinkedHashMap<String, Language> configKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> commandsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> languageKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> chatTitleKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> channelsKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> emojisKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, Language> wordFilterKeys = new LinkedHashMap<>();
	private static LinkedHashMap<String, LinkedHashMap<String, Language>> guiKeys = new LinkedHashMap<>();
	
	/**
	 * The parameter <b>Type</b> declares on which server is this class called.<br>
	 * This ensures to first, create only needed files and if a chatconversion to the old format is needed.
	 * @param type
	 */
	public YamlManager(Type type)
	{
		this.type = type;
		initConfig(type);
		initCommands();
		initLanguage();
		initChatTitle();
		initChannels();
		initEmojis();
		if(type == Type.SPIGOT)
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
	public void setFileInput(dev.dejvokep.boostedyaml.YamlDocument yml,
			LinkedHashMap<String, Language> keyMap, String key, ISO639_2B languageType) throws org.spongepowered.configurate.serialize.SerializationException
	{
		if(!keyMap.containsKey(key))
		{
			return;
		}
		if(yml.get(key) != null)
		{
			return;
		}
		if(key.startsWith("#"))
		{
			if(type == Type.BUNGEE)
			{
				//On Bungee dont work comments
				return;
			}
			//Comments
			String k = key.replace("#", "");
			if(yml.get(k) == null)
			{
				//return because no actual key are present
				return;
			}
			if(yml.getBlock(k) == null)
			{
				return;
			}
			if(yml.getBlock(k).getComments() != null && !yml.getBlock(k).getComments().isEmpty())
			{
				//Return, because the comments are already present, and there could be modified. F.e. could be comments from a admin.
				return;
			}
			if(keyMap.get(key).languageValues.get(languageType).length == 1)
			{
				if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
				{
					String s = ((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "");
					yml.getBlock(k).setComments(Arrays.asList(s));
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
						}
					}
				}
				yml.getBlock(k).setComments((List<String>) stringList);
			}
			return;
		}
		if(keyMap.get(key).languageValues.get(languageType).length == 1)
		{
			if(keyMap.get(key).languageValues.get(languageType)[0] instanceof String)
			{
				yml.set(key, convertMiniMessageToBungee(((String) keyMap.get(key).languageValues.get(languageType)[0]).replace("\r\n", "")));
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
						stringList.add(convertMiniMessageToBungee(((String) o).replace("\r\n", "")));
					} else
					{
						stringList.add(o.toString().replace("\r\n", ""));
					}
				}
			}
			yml.set(key, (List<String>) stringList);
		}
	}
	
	private String convertMiniMessageToBungee(String s)
	{
		if(type != Type.BUNGEE)
		{
			//If Server is not Bungee, there is no need to convert.
			return s;
		}
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if(c == '<' && i+1 < s.length())
			{
				char cc = s.charAt(i+1);
				if(cc == '#' && i+8 < s.length())
				{
					//Hexcolors
					//     i12345678
					//f.e. <#00FF00>
					String rc = s.substring(i, i+8);
					b.append(rc.replace("<#", "&#").replace(">", ""));
					i += 8;
				} else
				{
					//Normal Colors
					String r = null;
					StringBuilder sub = new StringBuilder();
					sub.append(c).append(cc);
					i++;
					for(int j = i+1; j < s.length(); j++)
					{
						i++;
						char jc = s.charAt(j);
						if(jc == '>')
						{
							sub.append(jc);
							switch(sub.toString())
							{
							case "</color>":
							case "</black>":
							case "</dark_blue>":
							case "</dark_green>":
							case "</dark_aqua>":
							case "</dark_red>":
							case "</dark_purple>":
							case "</gold>":
							case "</gray>":
							case "</dark_gray>":
							case "</blue>":
							case "</green>":
							case "</aqua>":
							case "</red>":
							case "</light_purple>":
							case "</yellow>":
							case "</white>":
							case "</obf>":
							case "</obfuscated>":
							case "</b>":
							case "</bold>":
							case "</st>":
							case "</strikethrough>":
							case "</u>":
							case "</underlined>":
							case "</i>":
							case "</em>":
							case "</italic>":
								r = "";
								break;
							case "<black>":
								r = "&0";
								break;
							case "<dark_blue>":
								r = "&1";
								break;
							case "<dark_green>":
								r = "&2";
								break;
							case "<dark_aqua>":
								r = "&3";
								break;
							case "<dark_red>":
								r = "&4";
								break;
							case "<dark_purple>":
								r = "&5";
								break;
							case "<gold>":
								r = "&6";
								break;
							case "<gray>":
								r = "&7";
								break;
							case "<dark_gray>":
								r = "&8";
								break;
							case "<blue>":
								r = "&9";
								break;
							case "<green>":
								r = "&a";
								break;
							case "<aqua>":
								r = "&b";
								break;
							case "<red>":
								r = "&c";
								break;
							case "<light_purple>":
								r = "&d";
								break;
							case "<yellow>":
								r = "&e";
								break;
							case "<white>":
								r = "&f";
								break;
							case "<obf>":
							case "<obfuscated>":
								r = "&k";
								break;
							case "<b>":
							case "<bold>":
								r = "&l";
								break;
							case "<st>":
							case "<strikethrough>":
								r = "&m";
								break;
							case "<u>":
							case "<underlined>":
								r = "&n";
								break;
							case "<i>":
							case "<em>":
							case "<italic>":
								r = "&o";
								break;
							case "<reset>":
								r = "&r";
								break;
							case "<newline>":
								r = "~!~";
								break;
							}
							b.append(r);
							break;
						} else
						{
							//Search for the color.
							sub.append(jc);
						}
					}
				}
			} else
			{
				b.append(c);
			}
		}
		return b.toString();
	}
	
	private void addComments(LinkedHashMap<String, Language> mapKeys, String path, Object[] o)
	{
		mapKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, o));
	}
	
	private void addConfig(String path, Object[] c, Object[] o)
	{
		configKeys.put(path, new Language(new ISO639_2B[] {ISO639_2B.GER}, c));
		addComments(configKeys, "#"+path, o);
	}
	
	public void initConfig(Type type) //INFO:Config
	{
		addConfig("useIFHAdministration",
				new Object[] {
				true},
				new Object[] {
				"Boolean um auf das IFH Interface Administration zugreifen soll.",
				"Wenn 'true' eingegeben ist, aber IFH Administration ist nicht vorhanden, so werden automatisch die eigenen Configwerte genommen.",
				"Boolean to access the IFH Interface Administration.",
				"If 'true' is entered, but IFH Administration is not available, the own config values are automatically used."});
		addConfig("IFHAdministrationPath", 
				new Object[] {
				"scc"},
				new Object[] {
				"",
				"Diese Funktion sorgt dafür, dass das Plugin auf das IFH Interface Administration zugreifen kann.",
				"Das IFH Interface Administration ist eine Zentrale für die Daten von Sprache, Servername und Mysqldaten.",
				"Diese Zentralisierung erlaubt für einfache Änderung/Anpassungen genau dieser Daten.",
				"Sollte das Plugin darauf zugreifen, werden die Werte in der eigenen Config dafür ignoriert.",
				"",
				"This function ensures that the plugin can access the IFH Interface Administration.",
				"The IFH Interface Administration is a central point for the language, server name and mysql data.",
				"This centralization allows for simple changes/adjustments to precisely this data.",
				"If the plugin accesses it, the values in its own config are ignored."});
		addConfig("Language",
				new Object[] {
				"ENG"},
				new Object[] {
				"",
				"Die eingestellte Sprache. Von Haus aus sind 'ENG=Englisch' und 'GER=Deutsch' mit dabei.",
				"Falls andere Sprachen gewünsch sind, kann man unter den folgenden Links nachschauen, welchs Kürzel für welche Sprache gedacht ist.",
				"Siehe hier nach, sowie den Link, welche dort auch für Wikipedia steht.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java",
				"",
				"The set language. By default, ENG=English and GER=German are included.",
				"If other languages are required, you can check the following links to see which abbreviation is intended for which language.",
				"See here, as well as the link, which is also there for Wikipedia.",
				"https://github.com/Avankziar/RootAdministration/blob/main/src/main/java/me/avankziar/roota/general/Language.java"});
		if(type == Type.SPIGOT)
		{
			addConfig("Server",
					new Object[] {
					"hub"},
					new Object[] {
					"",
					"Der Server steht für den Namen des Spigotservers, wie er in BungeeCord/Waterfall config.yml unter dem Pfad 'servers' angegeben ist.",
					"Sollte kein BungeeCord/Waterfall oder andere Proxys vorhanden sein oder du nutzt IFH Administration, so kannst du diesen Bereich ignorieren.",
					"",
					"The server stands for the name of the spigot server as specified in BungeeCord/Waterfall config.yml under the path 'servers'.",
					"If no BungeeCord/Waterfall or other proxies are available or you are using IFH Administration, you can ignore this area."});
			configKeys.put("IsBungeeActive"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
		}
		addConfig("Mysql.Status",
				new Object[] {
				false},
				new Object[] {
				"",
				"'Status' ist ein simple Sicherheitsfunktion, damit nicht unnötige Fehler in der Konsole geworfen werden.",
				"Stelle diesen Wert auf 'true', wenn alle Daten korrekt eingetragen wurden.",
				"",
				"'Status' is a simple security function so that unnecessary errors are not thrown in the console.",
				"Set this value to 'true' if all data has been entered correctly."});
		addComments(configKeys, "#Mysql", 
				new Object[] {
				"",
				"Mysql ist ein relationales Open-Source-SQL-Databaseverwaltungssystem, das von Oracle entwickelt und unterstützt wird.",
				"'My' ist ein Namenkürzel und 'SQL' steht für Structured Query Language. Eine Programmsprache mit der man Daten auf einer relationalen Datenbank zugreifen und diese verwalten kann.",
				"Link https://www.mysql.com/de/",
				"Wenn du IFH Administration nutzt, kann du diesen Bereich ignorieren.",
				"",
				"Mysql is an open source relational SQL database management system developed and supported by Oracle.",
				"'My' is a name abbreviation and 'SQL' stands for Structured Query Language. A program language that can be used to access and manage data in a relational database.",
				"Link https://www.mysql.com",
				"If you use IFH Administration, you can ignore this section."});
		addConfig("Mysql.Host",
				new Object[] {
				"127.0.0.1"},
				new Object[] {
				"",
				"Der Host, oder auch die IP. Sie kann aus einer Zahlenkombination oder aus einer Adresse bestehen.",
				"Für den Lokalhost, ist es möglich entweder 127.0.0.1 oder 'localhost' einzugeben. Bedenke, manchmal kann es vorkommen,",
				"das bei gehosteten Server die ServerIp oder Lokalhost möglich ist.",
				"",
				"The host, or IP. It can consist of a number combination or an address.",
				"For the local host, it is possible to enter either 127.0.0.1 or >localhost<.",
				"Please note that sometimes the serverIp or localhost is possible for hosted servers."});
		addConfig("Mysql.Port",
				new Object[] {
				3306},
				new Object[] {
				"",
				"Ein Port oder eine Portnummer ist in Rechnernetzen eine Netzwerkadresse,",
				"mit der das Betriebssystem die Datenpakete eines Transportprotokolls zu einem Prozess zuordnet.",
				"Ein Port für Mysql ist standart gemäß 3306.",
				"",
				"In computer networks, a port or port number ",
				"is a network address with which the operating system assigns the data packets of a transport protocol to a process.",
				"A port for Mysql is standard according to 3306."});
		addConfig("Mysql.DatabaseName",
				new Object[] {
				"mydatabase"},
				new Object[] {
				"",
				"Name der Datenbank in Mysql.",
				"",
				"Name of the database in Mysql."});
		addConfig("Mysql.SSLEnabled",
				new Object[] {
				false},
				new Object[] {
				"",
				"SSL ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"SSL is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.AutoReconnect",
				new Object[] {
				true},
				new Object[] {
				"",
				"AutoReconnect ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"AutoReconnect is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.VerifyServerCertificate",
				new Object[] {
				false},
				new Object[] {
				"",
				"VerifyServerCertificate ist einer der drei Möglichkeiten, welcher, solang man nicht weiß, was es ist, es so lassen sollte wie es ist.",
				"",
				"VerifyServerCertificate is one of the three options which, as long as you don't know what it is, you should leave it as it is."});
		addConfig("Mysql.User",
				new Object[] {
				"admin"},
				new Object[] {
				"",
				"Der User, welcher auf die Mysql zugreifen soll.",
				"",
				"The user who should access the Mysql."});
		addConfig("Mysql.Password",
				new Object[] {
				"not_0123456789"},
				new Object[] {
				"",
				"Das Passwort des Users, womit er Zugang zu Mysql bekommt.",
				"",
				"The user's password, with which he gets access to Mysql."});
		configKeys.put("Enable.InterfaceHub.Providing"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				true}));
		addConfig("Logging",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, dann wird in der Console alle Nachrichte angezeigt, welche im Chat gesendet werden.",
				"",
				"If so, all messages sent in the chat are displayed in the console."});
		if(type == Type.SPIGOT || type == Type.BUNGEE)
		{
			configKeys.put("Use.Mail"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					true}));
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
					"<>"}));
		}
		addConfig("PrivateChannel.UseDynamicColor",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, dann wird wechseln die Farben des Privatchats sich ab. Die neue Farbe bleibt mit dem jeweiligen Spieler während der Session erhalten.",
				"",
				"If true, the colors of the private chat will alternate. The new color remains with the respective player during the session."});
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
		addConfig("PermanentChannel.AmountPerPlayer",
				new Object[] {
				1},
				new Object[] {
				"",
				"Anzahl an PermanentChannel der ein Spieler haben darf. Permission müssen trotzdem vorhanden sein.",
				"",
				"Number of permanent channels a player may have. Permission must still be available."});
		addConfig("PermanentChannel.InviteCooldown",
				new Object[] {
				60},
				new Object[] {
				"",
				"Anzahl an Sekunden, welche man warten muss, bevor man eine weitere Person oder die gleiche mehrmals in den permanenten Channel einladen kann.",
				"",
				"Number of seconds you have to wait before you can invite another person or the same person several times to the permanent channel."});
		addConfig("TemporaryChannel.InviteCooldown",
				new Object[] {
				60},
				new Object[] {
				"",
				"Anzahl an Sekunden, welche man warten muss, bevor man eine weitere Person oder die gleiche mehrmals in den temporären Channel einladen kann.",
				"",
				"Number of seconds you have to wait before you can invite another person or the same person several times to the temporary channel."});
		addConfig("BroadCast.UsingChannel",
				new Object[] {
				"Global"},
				new Object[] {
				"",
				"Name des Channels, welche als Parser für den Broadcast genutzt werden soll.",
				"",
				"Name of the channel to be used as a parser for the broadcast."});
		addConfig("Mute.SendGlobal",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, wird die Nachricht, dass ein Spieler gemutet wurde, an alle anwesenden Spieler gesendet.",
				"",
				"If true, the message that a player has been muted is sent to all players present."});
		addConfig("MsgSoundUsage",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, wird beim erhalt einer Nachricht ein Sound abgespielt.",
				"",
				"If true, a sound is played when a message is received."});
		addConfig("JoinMessageDefaultValue",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, werden alle neuen Spieler automatisch angezeigt, wenn ein Spieler den Server betritt oder verlässt. Durch den Spieler selbst änderbar.",
				"",
				"If true, all new players are automatically displayed when a player enters or leaves the server. Can be changed by the player himself."});
		addConfig("CleanUp.RunAutomaticByRestart",
				new Object[] {
				true},
				new Object[] {
				"",
				"Wenn true, wird der Aufräumtask bei (Neu)Starten ausgeführt.",
				"",
				"If true, the cleanup task is executed on (re)startup."});
		addConfig("CleanUp.DeletePlayerWhichJoinIsOlderThanDays",
				new Object[] {
				120},
				new Object[] {
				"",
				"Löscht einen Spieler nach x Tagen ohne Aktivität.",
				"",
				"Deletes a player after x days without activity."});
		if(type == Type.SPIGOT || type == Type.BUNGEE)
		{
			configKeys.put("CleanUp.DeleteReadedMailWhichIsOlderThanDays"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					365}));
		}		
		configKeys.put("ChatReplacer.Command.RunCommandStart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"cmd|/"}));
		addComments(configKeys, "ChatReplacer",
				new Object[] {
				"",
				"ChatReplacer sind eine Form von SCC um in den Chat Formate hinzuzufügen, die vorher nicht so gewollt oder möglich waren.",
				"Um ChatReplacer nutzen zu können, muss im Channel dieser auch aktiv sein oder man muss eine BypassPerm besitzten.",
				"Alle ChatReplacer werden durch Start-Symbole angefangen. Manche brauchen auch End-Symbole. Der mittlere Teil ist die benutzerdefinierte Angabe.",
				"Bei korrekter Eingabe, wandelt SCC dies in die entsprechende um.",
				"",
				"ChatReplacers are a form of SCC to add formats to the chat that were not wanted or possible before.",
				"To be able to use ChatReplacer, it must also be active in the channel or you must have a BypassPerm.",
				"All ChatReplacers are started by start symbols. Some also need end symbols. The middle part is the user-defined specification.",
				"If the input is correct, SCC converts this into the corresponding."});
		configKeys.put("ChatReplacer.Command.SuggestCommandStart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"cmd/"}));
		configKeys.put("ChatReplacer.Command.RunCommandStartReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[<yellow>ClickCmd: "}));
		configKeys.put("ChatReplacer.Command.RunCommandEndReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>]"}));
		configKeys.put("ChatReplacer.Command.SuggestCommandStartReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[<white>ClickCmd: "}));
		configKeys.put("ChatReplacer.Command.SuggestCommandEndReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>]"}));
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
				":"}));
		configKeys.put("ChatReplacer.Emoji.End"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				":"}));
		configKeys.put("ChatReplacer.Mention.Start"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"@@"}));
		configKeys.put("ChatReplacer.Mention.Color"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<dark_red>"}));
		configKeys.put("ChatReplacer.Mention.SoundEnum"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"ENTITY_WANDERING_TRADER_REAPPEARED"}));
		configKeys.put("ChatReplacer.Position.Replacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<pos>"}));
		configKeys.put("ChatReplacer.Position.Replace"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[<blue>%server% <light_purple>%world% <yellow>%x% %y% %z%<gray>]"}));
		configKeys.put("ChatReplacer.NewLine"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"~!~"}));
		configKeys.put("Gui.ActiveTerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<green>✔"}));
		configKeys.put("Gui.DeactiveTerm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<red>✖"}));
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
				"<red>/scc [Seitenzahl] <white>| Infoseite für alle Befehle.",
				"<red>/scc [pagenumber] <white>| Info page for all commands.");
		commandsInput("scceditor", "scceditor", "scc.cmd.scceditor", 
				"/scceditor", "/scceditor ",
				"<red>/scceditor [true|false] [Spielername] <white>| ChatEditor Toggle.",
				"<red>/scceditor [true|false] [playername] <white>| ChatEditor toggle.");
		commandsInput("clch", "clch", "scc.cmd.clch", 
				"/clch [pagenumber]", "/clch ",
				"<red>/clch <Spielername> <Zahl> <Nachricht...> <white>| Sendet einen klickbaren Chat für den Spieler. Geeignet für Citizen / Denizen Plugin.",
				"<red>/clch <player name> <number> <message...> <white>| Sends a clickable chat for the player. Suitable for Citizen / Denizen plugin.");
		commandsInput("msg", "msg", "scc.cmd.msg", 
				"/msg <player> <message...>", "/msg ",
				"<red>/msg <Spielername> <Nachricht> <white>| Schreibt dem Spieler privat. Alle Spieler, welche online sind, werden als Vorschlag angezeigt.",
				"<red>/msg <player name> <message> <white>| Write to the player privately. All players who are online will be displayed as a suggestion.");
		commandsInput("re", "re", "scc.cmd.re", 
				"/re <player> <message...>", "/re ",
				"<red>/re <Spielername> <Nachricht> <white>| Schreibt dem Spieler privat. Alle Spieler mit denen man schon geschrieben hat, werden als Vorschlag angezeigt.",
				"<red>/re <player name> <message> <white>| Write to the player privately. All players with whom you have already written are displayed as suggestions.");
		commandsInput("r", "r", "scc.cmd.r", 
				"/r <message...>", "/r ",
				"<red>/r <message...> <white>| Schreibt dem Spieler, welcher einem selbst zuletzt privat geschrieben hat.",
				"<red>/r <message...> <white>| Write to the player who last wrote to you privately.");
		commandsInput("w", "w", "scc.cmd.w", 
				"/w <player>", "/w ",
				"<red>/w [Spielername] <white>| Consolenbefehl für Privatnachrichten an Spieler.",
				"<red>/w [playername] <white>| Consolecommand for private message to player.");
		String path = "scc_";
		String basePermission = "scc.cmd.scc";
		//INFO:Argument Start
		argumentInput(path+"book", "book", basePermission,
				"/scc book <itemname> [playername]", "/scc book ",
				"<red>/scc book <Itemname> [Spielername] <white>| Öffnet das Buch vom ItemReplacer.",
				"<red>/scc book <Itemname> [playername] <white>| Open the book from ItemReplacer.");
		argumentInput(path+"broadcast", "broadcast", basePermission,
				"/scc broadcast <message...>", "/scc broadcast ",
				"<red>/scc broadcast <Nachricht> <white>| Zum Senden einer Broadcast Nachricht. Falls Bungeecord aktiviert ist, kann man auch von Spigot als Console, bungeecordübergreifend dies an alle Spieler senden.",
				"<red>/scc broadcast <message> <white>| To send a broadcast message. If bungeecord is enabled, you can also send this to all players from Spigot as a console, across bungeecords.");
		argumentInput(path+"broadcastserver", "broadcastserver", basePermission,
				"/scc broadcastserver <message...>", "/scc broadcastserver ",
				"<red>/scc broadcastserver <Nachricht> <white>| Zum Senden einer Broadcast Nachricht an alle Spieler, welche auf dem gleichen Server sind.",
				"<red>/scc broadcastserver <message> <white>| To send a broadcast message to all players who are on the same server.");
		argumentInput(path+"broadcastworld", "broadcastworld", basePermission,
				"/scc broadcastworld <message...>", "/scc broadcastserver ",
				"<red>/scc broadcastworld <Nachricht> <white>| Zum Senden einer Broadcast Nachricht an alle Spieler, welche auf der gleichen Welt sind.",
				"<red>/scc broadcastworld <message> <white>| To send a broadcast message to all players who are on the same world.");
		argumentInput(path+"channel", "channel", basePermission,
				"/scc channel <channel>", "/scc channel ",
				"<red>/scc channel <Channelname> <white>| Zum An- & Ausstellen des angegebenen Channels.",
				"<red>/scc channel <channelname> <white>| To turn the specified channel on & off.");
		argumentInput(path+"channelgui", "channelgui", basePermission,
				"/scc channelgui ", "/scc channelgui ",
				"<red>/scc channelgui <white>| Öffnet ein Menü, wo die Channels aus und eingestellt werden können.",
				"<red>/scc channelgui <white>| Opens a menu where the channels can be selected and set.");
		argumentInput(path+"debug", "debug", basePermission,
				"/scc debug ", "/scc debug ",
				"<red>/scc debug <white>| Debugbefehl",
				"<red>/scc debug <white>| Debugcommand");
		argumentInput(path+"ignore", "ignore", basePermission,
				"/scc ignore <playername>", "/scc ignore ",
				"<red>/scc ignore <Spielername> <white>| Zum Einsetzen oder Aufheben des Ignores für den Spieler.",
				"<red>/scc ignore <playername> <white>| To set or remove the ignore for the player.");
		argumentInput(path+"ignorelist", "ignorelist", basePermission,
				"/scc ignorelist [playername]", "/scc ignorelist ",
				"<red>/scc ignorelist [Spielername] <white>| Zum Anzeigen aller Spieler auf der Ignoreliste.",
				"<red>/scc ignorelist [playername] <white>| To show all players on the ignore list.");
		argumentInput(path+"mute", "mute", basePermission,
				"/scc mute <playername> [values...]", "/scc mute ",
				"<red>/scc mute <Spielername> [Werte...] <white>| Stellt den Spieler für die angegebene Zeit stumm. Bei keinem Wert ist es permanent. Mögliche hinzufügbare und kombinierbare Werte sind: (Format x:<Zahl>) y=Jahre, M=Monate, d=Tage, H=Stunden, m=Minuten, s=Sekunden. Z.B. H:2 m:10 bedeutet für 2 Stunden und 10 Minuten gemutet.",
				"<red>/scc mute <playername> [values...] <white>| Mutes the player for the specified time. With no value, it is permanent. Possible addable and combinable values are: (format x:<number>) y=years, M=months, d=days, H=hours, m=minutes, s=seconds. E.g. H:2 m:10 means muted for 2 hours and 10 minutes.");
		argumentInput(path+"performance", "performance", basePermission,
				"/scc performance ", "/scc performance ",
				"<red>/scc performance <white>| Zeigt die MysqlPerformances des Plugins an.",
				"<red>/scc performance <white>| Displays the MysqlPerformances of the plugin.");
		argumentInput(path+"unmute", "unmute", basePermission,
				"/scc unmute <playername> ", "/scc unmute ",
				"<red>/scc unmute <Spielername> <white>| Zum sofortigen entmuten des Spielers.",
				"<red>/scc unmute <playername> <white>| To immediately unmute the player.");
		argumentInput(path+"updateplayer", "updateplayer", basePermission,
				"/scc updateplayer <playername>", "/scc updateplayer ",
				"<red>/scc updateplayer <Spielername> <white>| Updatet die Zugangsrechte des Spielers für alle Channels.",
				"<red>/scc updateplayer <playername> <white>| Updates the player's access rights for all channels.");
		
		argumentInput(path+"option", "option", basePermission,
				"/scc option ", "/scc option ",
				"<red>/scc option <white>| Zwischenbefehl",
				"<red>/scc option <white>| Intermediate command");
		basePermission = "scc.cmd.scc.option";
		argumentInput(path+"option_channel", "channel", basePermission,
				"/scc option channel ", "/scc option channel ",
				"<red>/scc option channel <white>| Aktiviert oder deaktiviert, ob man beim Joinen seine aktiven Channels sieht.",
				"<red>/scc option channel <white>| Enables or disables, whether you can see your active channels when joining.");
		argumentInput(path+"option_join", "join", basePermission,
				"/scc option join", "/scc option join",
				"<red>/scc option join <white>| Aktiviert oder deaktiviert, ob man die Joinnachricht anderer Spieler sieht.",
				"<red>/scc option join <white>| Enables or disables, whether you can see the join message of other players.");
		argumentInput(path+"option_spy", "spy", basePermission,
				"/scc option spy ", "/scc option spy ",
				"<red>/scc option spy <white>| Aktiviert oder deaktiviert, ob man Nachrichten sehen kann, welche einem sonst verborgen wären.",
				"<red>/scc option spy <white>| Enables or disables, whether you can see messages that would otherwise be hidden from you.");
		basePermission = "scc.cmd.scc.item.";
		argumentInput(path+"item", "item", basePermission,
				"/scc item ", "/scc item ",
				"<red>/scc item <white>| Öffnet das Menü, wo man die Items für den Replacer einstellen kann.",
				"<red>/scc item <white>| Opens the menu where you can set the items for the replacer.");
		argumentInput(path+"item_rename", "rename", basePermission,
				"/scc item rename <oldname> <newname> ", "/scc item rename ",
				"<red>/scc item rename <Alter Name> <Neuer Name> <white>| Benennt das Item, welches auf den alten Namen registriert ist, um.",
				"<red>/scc item rename <oldname> <newname> <white>| Renames the item that goes by the old name.");
		argumentInput(path+"item_replacers", "replacers", basePermission,
				"/scc item replacers ", "/scc item replacers ",
				"<red>/scc item replacers <white>| Zeigt alle möglichen Replacer im Chat an, sowie dessen Item als Hovernachricht.",
				"<red>/scc item replacers <white>| Displays all possible replacers in the chat, as well as their item as a hovermessage.");
		//INFO:PermanentChannel
		basePermission = "scc.cmd.scc";
		argumentInput(path+"pc", "pc", basePermission,
				"/scc pc ", "/scc pc ",
				"<red>/scc pc <white>| Zwischenbefehl",
				"<red>/scc pc <white>| Intermediate command");
		basePermission = "scc.cmd.scc.pc";
		argumentInput(path+"pc_ban", "ban", basePermission,
				"/scc pc ban <channelname> <playername> ", "/scc pc ban ",
				"<red>/scc pc ban <Channelname> <Spielername> <white>| Bannt einen Spieler von einem permanenten Channel.",
				"<red>/scc pc ban <channelname> <playername> <white>| Bans a player from a permanent channel.");
		argumentInput(path+"pc_unban", "unban", basePermission,
				"/scc pc unban <channelname> <playername>", "/scc pc unban ",
				"<red>/scc pc <Channelname> <Spielername> <white>| Entbannt einen Spieler von einem permanenten Channel.",
				"<red>/scc pc <channelname> <playername> <white>| Unbans a player from a permanent channel.");
		argumentInput(path+"pc_changepassword", "changepassword", basePermission,
				"/scc pc changepassword <channelname> <password>", "/scc pc changepassword ",
				"<red>/scc pc changepassword <Channelname> <Passwort> <white>| Ändert das Passwort von einem permanenten Channel.",
				"<red>/scc pc changepassword <channelname> <password> <white>| Changes the password of a permanent channel.");
		argumentInput(path+"pc_channels", "channels", basePermission,
				"/scc pc channels <channel> ", "/scc pc channels ",
				"<red>/scc pc channels <Channel> <white>| Zeigt alle Channels an mit Infobefehl.",
				"<red>/scc pc channels <channel> <white>| Shows all channels with info command.");
		argumentInput(path+"pc_chatcolor", "chatcolor", basePermission,
				"/scc pc chatcolor <channelname> <color> ", "/scc pc chatcolor ",
				"<red>/scc pc chatcolor <Channelname> <Farbe> <white>| Ändert die Farbe des permanenten Channel für den Chat.",
				"<red>/scc pc chatcolor <channelname> <color> <white>| Changes the color of the permanent channel for the chat.");
		argumentInput(path+"pc_create", "create", basePermission,
				"/scc pc create <channelname> [password] ", "/scc pc create ",
				"<red>/scc pc create <Channelname> [Passwort] <white>| Erstellt einen permanenten Channel. Optional mit Passwort.",
				"<red>/scc pc create <channelname> [password] <white>| Creates a permanent channel. Optionally with password.");
		argumentInput(path+"pc_delete", "delete", basePermission,
				"/scc pc delete <channelname> ", "/scc pc delete ",
				"<red>/scc pc delete <Channelname> <white>| Löscht den Channel.",
				"<red>/scc pc delete <channelname> <white>| Delete the channel.");
		argumentInput(path+"pc_info", "info", basePermission,
				"/scc pc info [channelname] ", "/scc pc info ",
				"<red>/scc pc info [Channelname] <white>| Zeigt alle Infos zum permanenten Channel an.",
				"<red>/scc pc info [channelname] <white>| Displays all info about the permanent channel.");
		argumentInput(path+"pc_inherit", "inherit", basePermission,
				"/scc pc inherit <channelname> <playername> ", "/scc pc inherit ",
				"<red>/scc pc inherit <Channelname> <Spielername> <white>| Lässt den Spieler den Channel als Ersteller beerben.",
				"<red>/scc pc inherit <channelname> <playername> <white>| Lets the player inherit the channel as creator.");
		argumentInput(path+"pc_invite", "invite", basePermission,
				"/scc pc invite <channelname> <playername>", "/scc pc invite ",
				"<red>/scc pc invite <Channelname> <Spielername> <white>| Lädt einen Spieler in den permanenten Channel ein.",
				"<red>/scc pc invite <channelname> <playername> <white>| Invites a player to the permanent Channel.");
		argumentInput(path+"pc_join", "join", basePermission,
				"/scc pc join <channelname> [password] ", "/scc pc join ",
				"<red>/scc pc join <Channelname> [Passwort] <white>| Betritt einen permanenten Channel.",
				"<red>/scc pc join <channelname> [password] <white>| Enter a permanent channel.");
		argumentInput(path+"pc_kick", "kick", basePermission,
				"/scc pc kick <channelname> <playername> ", "/scc pc kick ",
				"<red>/scc pc kick <Channelname> <Spielername> <white>| Kickt einen Spieler von einem permanenten Channel.",
				"<red>/scc pc kick <channelname> <playername> <white>| Kicks a player from a permanent channel.");
		argumentInput(path+"pc_leave", "leave", basePermission,
				"/scc pc leave <channelname> ", "/scc pc leave ",
				"<red>/scc pc leave <Channelname> <white>| Verlässt einen permanenten Channel.",
				"<red>/scc pc leave <channelname> <white>| Leaves a permanent channel.");
		argumentInput(path+"pc_namecolor", "namecolor", basePermission,
				"/scc pc namecolor <channelname> <color> ", "/scc pc namecolor ",
				"<red>/scc pc namecolor <Channelname> <Farbe> <white>| Ändert die Farbe des permanenten Channelpräfix.",
				"<red>/scc pc namecolor <channelname> <color> <white>| Changes the color of the permanent Channelprefix.");
		argumentInput(path+"pc_player", "player", basePermission,
				"/scc pc player [playername] ", "/scc pc player ",
				"<red>/scc pc player [Spielername] <white>| Zeigt alle permanenten Channels an, wo der Spieler beigetreten ist.",
				"<red>/scc pc player [playername] <white>| Displays all permanent channels where the player has joined.");
		argumentInput(path+"pc_rename", "rename", basePermission,
				"/scc pc rename <channelname> <newname>", "/scc pc rename ",
				"<red>/scc pc rename <Channelname> <Neuer Name> <white>| Ändert den Namen des permanenten Channel.",
				"<red>/scc pc rename <channelname> <newname> <white>| Changes the name of the permanent Channel.");
		argumentInput(path+"pc_symbol", "symbol", basePermission,
				"/scc pc symbol <channelname> <symbols>", "/scc pc symbol ",
				"<red>/scc pc symbol <Channelname> <Symbole> <white>| Ändert das Zugangssymbol des Channels.",
				"<red>/scc pc symbol <channelname> <symbols> <white>| Changes the access icon of the channel.");
		argumentInput(path+"pc_vice", "vice", basePermission,
				"/scc pc vice <channelname> <playername> ", "/scc pc vice ",
				"<red>/scc pc vice <Channelname> <Spielername> <white>| Befördert oder degradiert einen Spieler innerhalb des permanenten Channels.",
				"<red>/scc pc vice <channelname> <playername> <white>| Promotes or demotes a player within the permanent Channel.");
		//INFO:TemporaryChannel
		basePermission = "scc.cmd.scc";
		argumentInput(path+"tc", "temporarychannel", basePermission,
				"/scc tc ", "/scc tc ",
				"<red>/scc tc <white>| Zwischenbefehl",
				"<red>/scc tc <white>| Intermediate command");
		basePermission = "scc.cmd.scc.temporarychannel";
		argumentInput(path+"tc_ban", "ban", basePermission,
				"/scc tc ban <playername> ", "/scc tc ban ",
				"<red>/scc tc ban <Spielername> <white>| Bannt einen Spieler von einem temporären Channel.",
				"<red>/scc tc ban <playername> <white>| Bans a player from a temporary channel.");
		argumentInput(path+"tc_unban", "unban", basePermission,
				"/scc tc unban <playername> ", "/scc tc unban ",
				"<red>/scc tc unban <Spielername> <white>| Entbannt einen Spieler von einem temporären Channel.",
				"<red>/scc tc unban <playername> <white>| Unbans a player from a temporary channel.");
		argumentInput(path+"tc_changepassword", "changepassword", basePermission,
				"/scc tc changepassword <password> ", "/scc tc changepassword ",
				"<red>/scc tc changepassword <Passwort> <white>| Ändert das Passwort von einem temporären Channel.",
				"<red>/scc tc changepassword <password> <white>| Changes the password of a temporary channel.");
		argumentInput(path+"tc_create", "create", basePermission,
				"/scc tc create <channelname> [password] ", "/scc tc create ",
				"<red>/scc tc create <Channelname> [Passwort] <white>| Erstellt einen temporären Channel. Optional mit Passwort.",
				"<red>/scc tc create <channelname> [password] <white>| Creates a temporary channel. Optionally with password.");
		argumentInput(path+"tc_info", "info", basePermission,
				"/scc tc info ", "/scc tc info ",
				"<red>/scc tc info <white>| Zeigt alle Informationen bezüglich des temporären Channels an.",
				"<red>/scc tc info <white>| Displays all information related to the temporary channel.");
		argumentInput(path+"tc_invite", "invite", basePermission,
				"/scc tc invite <playername> ", "/scc tc invite ",
				"<red>/scc tc invite <Spielername> <white>| Lädt einen Spieler in den eigenen temporären Channel ein.",
				"<red>/scc tc invite <playername> <white>| Invites a player to the own temporary channel.");
		argumentInput(path+"tc_join", "join", basePermission,
				"/scc tc join <channelname> [password] ", "/scc tc join ",
				"<red>/scc tc join <Channelname> [Passwort] <white>| Betritt einen temporären Channel.",
				"<red>/scc tc join <channelname> [password] <white>| Enter a temporary channel.");
		argumentInput(path+"tc_kick", "kick", basePermission,
				"/scc tc kick <playername> ", "/scc tc kick ",
				"<red>/scc tc kick <Spielername> <white>| Kickt einen Spieler von einem temporären Channel.",
				"<red>/scc tc kick <playername> <white>| Kicks a player from a temporary channel.");
		argumentInput(path+"tc_leave", "leave", basePermission,
				"/scc tc leave ", "/scc tc leave ",
				"<red>/scc tc leave <white>| Verlässt einen temporären Channel.",
				"<red>/scc tc leave <white>| Leaves a temporary channel.");
		commandsInput("mail", "mail", "scc.cmd.mail.mail", 
				"/mail [page]", "/mail ",
				"<red>/mail [Seitenzahl] <white>| Zeigt alle ungelesenen Mails mit Klick- und Hovernachrichten.",
				"<red>/mail [pagen] <white>| Shows all unread mails with click and hover events.");
		path = "mail_";
		basePermission = "scc.cmd.mail";
		argumentInput(path+"lastreceivedmails", "lastreceivedmails", basePermission,
				"/mail lastreceivedmails [page] [playername] ", "/mail lastreceivedmails",
				"<red>/mail lastreceivedmails [Seitenzahl] [Spielername] <white>| Zeigt die letzten empfangen Mails an.",
				"<red>/mail lastreceivedmails [page] [playername] <white>| Show the last received mails.");
		argumentInput(path+"lastsendedmails", "lastsendedmails", basePermission,
				"/mail lastsendedmails [page] [playername] ", "/mail lastsendedmails",
				"<red>/mail lastsendedmails [Seitenzahl] [Spielername] <white>| Zeigt die letzten gesendeten Mails.",
				"<red>/mail lastsendedmails [page] [playername] <white>| Show the last sended mails.");
		argumentInput(path+"forward", "forward", basePermission,
				"/mail forward <id> ", "/mail forward ",
				"<red>/mail forward <id> <Spielername> <white>| Leitet die Mail an den Spieler weiter.",
				"<red>/mail forward <id> <playername> <white>| Forwards the mail to the player.");
		argumentInput(path+"read", "read", basePermission,
				"/mail read <id> ", "/mail read ",
				"<red>/mail read <id> <white>| Liest die Mail.",
				"<red>/mail read <id> <white>| Read the mail.");
		argumentInput(path+"send", "send", basePermission,
				"/mail send <receiver, multiple seperate with @> <subject...> <seperator> <message...> ", "/mail send ",
				"<red>/mail send <Empfänger, mehrere getrennt mit @> <Betreff...> <Trennzeichen> <Nachricht...> <white>| Schreibt eine Mail.",
				"<red>/mail send <receiver, multiple seperate with @> <subject...> <seperator> <message...> <white>| Write a mail.");
		/*argumentInput(path+"", "", basePermission,
				"/scc ", "/scc ",
				"<red>/scc <white>| ",
				"<red>/scc <white>| ");*/
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
		commandsKeys.put(path+"WordFilter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				by+"wordfilter"}));
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
						"<red>Genereller Fehler!",
						"<red>General Error!"}));
		languageKeys.put("InputIsWrong",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Deine Eingabe ist fehlerhaft! Klicke hier auf den Text, um weitere Infos zu bekommen!",
						"<red>Your input is incorrect! Click here on the text to get more information!"}));
		languageKeys.put("NoPermission",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast dafür keine Rechte!",
						"<red>You have no rights for this!"}));
		languageKeys.put("PlayerNotExist",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler existiert nicht!",
						"<red>The player does not exist!"}));
		languageKeys.put("PlayerNotOnline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler ist nicht online!",
						"<red>The player is not online!"}));
		languageKeys.put("NotNumber",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Einer oder einige der Argumente muss eine Zahl sein!",
						"<red>One or some of the arguments must be a number!"}));
		languageKeys.put("GeneralHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick mich!",
						"<yellow>Click me!"}));
		
		languageKeys.put("BaseInfo.Headline",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<gray>[<red>SimpleChatChannels<gray>]<yellow>=====",
						"<yellow>=====<gray>[<red>SimpleChatChannels<gray>]<yellow>====="}));
		languageKeys.put("BaseInfo.Next",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow><u>nächste Seite <yellow>==>",
						"<yellow><u>next page <yellow>==>"}));
		languageKeys.put("BaseInfo.Past",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow><== <u>vorherige Seite",
						"<yellow><== <u>past page"}));
		
		languageKeys.put("JoinListener.Comma",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<aqua>, ",
						"<aqua>, "}));
		languageKeys.put("JoinListener.YouMuted",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist zurzeit gemutet!",
						""}));
		languageKeys.put("JoinListener.Pretext",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<aqua>Aktive Channels: ",
						""}));
		languageKeys.put("JoinListener.Spy",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<dark_red>Spy",
						"<dark_red>Spy"}));
		languageKeys.put("JoinListener.Join",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<green>+<gray>] <yellow>%player%",
						"<gray>[<green>+<gray>] <yellow>%player%"}));
		languageKeys.put("JoinListener.HasNewMail",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast <white>%count% <yellow>neue Mails!",
						"<yellow>You have <white>%count% <yellow>new mails!"}));
		languageKeys.put("JoinListener.Welcome",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<blue>.:|°*`<aqua>Willkommen %player%<blue>´*°|:.",
						"<blue>.:|°*`<aqua>Welcome %player%<blue>´*°|:."}));
		languageKeys.put("LeaveListener.Leave",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<red>-<gray>] <yellow>%player%",
						"<gray>[<red>-<gray>] <yellow>%player%"}));
		
		//ChatListener
		languageKeys.put("ChatListener.NoChannelIsNullChannel",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Deine Chateingabe kann in keinen Channel gepostet werden, da kein Channel passt und auch kein Channel ohne Eingangssymbol existiert!",
						"<red>Your chat entry canot be posted in any channel, because no channel fits and also no channel exists without an entry symbol!"}));
		languageKeys.put("ChatListener.ToManyReplacer",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Deine Chateingabe kann nicht gepostet werden! Zuviele Replacer sind verbaut, welche das Zeichenlimit überschreiten würde!",
						"<red>Your chat input can not be posted! Too many replacers are installed, which would exceed the character limit!"}));
		languageKeys.put("ChatListener.NotATemporaryChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist in keinem Temporären Channel!",
						"<red>You are not in a temporary channel!"}));
		languageKeys.put("ChatListener.NotAPermanentChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist in keinem Permanenten Channel!",
						"<red>You are not in a permanent channel!"}));
		languageKeys.put("ChatListener.SymbolNotKnow"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Permanente Channel <red>%symbol% <red>existiert nicht.",
						"<red>The <white>%symbol% <red>permanent channel does not exist."}));
		languageKeys.put("ChatListener.ChannelIsOff"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>Du hast diesen Channel ausgeschaltet. Bitte schalte ihn zum Benutzen wieder an.",
						"<gray>You have turned off this channel. Please turn it on again to use it."}));
		languageKeys.put("ChatListener.YourAreOnTheSpecificServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist auf einem Server, wo man in diesem Channel nicht schreiben kann!",
						"<red>You are on a server where you can not write in this channel!"}));
		languageKeys.put("ChatListener.ContainsBadWords"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Einer deiner geschriebenen Wörter ist im Wortfilter enthalten, bitte unterlasse solche Ausdrücke!",
						"<red>One of your written words is included in the word filter, please refrain from such expressions!"}));
		languageKeys.put("ChatListener.YouAreMuted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist für <white>%time% <red>gemutet!",
						"<red>You are muted for <white>%time%<red>!"}));
		languageKeys.put("ChatListener.PleaseWaitALittle"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Bitte warte noch bis <white>%time%<red>, dann kannst du im Channel <reset>%channel% <red>wieder etwas schreibst.",
						"<red>Please wait until <white>%time%<red> to write something again in the channel <reset>%channel%<red>."}));
		languageKeys.put("ChatListener.PleaseWaitALittleWithSameMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Bitte warte noch bis <white>%time%<red>, dann kannst du im Channel <reset>%channel% <red>wieder die selbe Nachricht schreiben.",
						"<red>Please wait until <white>%time% <red>to write again the same message in the channel <reset>%channel%<red>."}));
		languageKeys.put("ChatListener.PlayerIgnoreYou"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler <white>%player% <red>ignoriert dich!",
						"<red>The player <white>%player% <red>ignores you!"}));
		languageKeys.put("ChatListener.PlayerIgnoreYouButYouBypass"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler <white>%player% <red>ignoriert dich, jedoch konntest du das umgehen!",
						"<red>The <white>%player% <red>ignores you, however you were able to bypass that!"}));
		languageKeys.put("ChatListener.PlayerHasPrivateChannelOff"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler <white>%player% <red>hat das Empfangen von privaten Nachrichten deaktiviert!",
						"<red>The player <white>%player% <red>has private messaging disabled!"}));
		languageKeys.put("ChatListener.StringTrim"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Bitte schreibe Nachrichten mit Inhalt.",
						"<red>Please write messages with content."}));
		languageKeys.put("ChatListener.ItemIsMissing"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<white>Nicht gefunden<gray>]",
						"<gray>[<white>Not found<gray>]"}));
		languageKeys.put("ChatListener.PrivateChatHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<light_purple>Klick hier, um im Privaten mit <white>%player% <light_purple>zu schreiben.",
						"<light_purple>Click here to write in private with <white>%player% <light_purple>."}));
		languageKeys.put("ChatListener.ChannelHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"%channelcolor%Klick hier, um im %channel% Channel zu schreiben.",
						"%channelcolor%Click here to write in the %channel% channel."}));
		languageKeys.put("ChatListener.CommandRunHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<dark_red>Klick hier, um den Befehl auszuführen.",
						"<dark_red>Click here to execute the command."}));
		languageKeys.put("ChatListener.CommandSuggestHover",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick hier, um den Befehl in der Chatzeile zu erhalten.",
						"<yellow>Click here to get the command in the chat line."}));
		languageKeys.put("ChatListener.Website.Replacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<white>Web<gray>seite",
						"<white>web<gray>site"}));
		languageKeys.put("ChatListener.Website.NotAllowReplacer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<white>[<gray>Zensiert<white>]",
						"<white>[<gray>Censord<white>]"}));
		languageKeys.put("ChatListener.Website.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick hier, um diese Webseite zu kopieren.~!~<aqua>",
						"<yellow>Click here to copy this web page.~!~<aqua>"}));
		languageKeys.put("ChatListener.Website.NotAllowHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>In diesem Channel ist das Posten von Webseiten nicht erlaubt.",
						"<red>Posting web pages is not allowed in this channel."}));
		languageKeys.put("ChatListener.Mention.MentionHover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>%player% hat <white>%target% <yellow>erwähnt!",
						"<yellow>%player% has mentioned <white>%target%<yellow>!"}));
		languageKeys.put("ChatListener.Emoji.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Dieses Emoji wurde mit <white>%emoji% <yellow>generiert!",
						"<yellow>This emoji was generated with <white>%emoji%<yellow>!"}));
		
		languageKeys.put("CmdMsg.PrivateChannelsNotActive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Private Channel ist global deaktiviert!",
						"<red>The private Channel is globally disabled!"}));
		languageKeys.put("CmdMsg.PleaseEnterAMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Bitte schreibe eine Nachricht mit Inhalt!",
						"<red>Please write a message with content!"}));
		languageKeys.put("CmdMsg.YouHaveNoPrivateMessagePartner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast mit keinem Spieler geschrieben!",
						"<red>You have not written with any player!"}));
		languageKeys.put("CmdMsg.IsAfk"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler ist afk!",
						"<yellow>The player is afk!"}));
		/*
		 * Editor
		 */
		languageKeys.put("CmdEditor.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der ChatEditor ist aktiviert. <red>Du kannst nun nicht mehr am normalen Chat teilnehmen.",
						"<yellow>The ChatEditor is active. <red>You can no longer participate in the normal chat."}));
		languageKeys.put("CmdEditor.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der ChatEditor ist deaktiviert. <green>Du kannst nun am normalen Chat teilnehmen.",
						"<yellow>The ChatEditor is deactive. <green>You can now participate in the normal chat."}));
		
		/*
		 * Mail
		 */
		languageKeys.put("CmdMail.Base.NoUnreadMail", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast keine ungelesenen Mails!",
						"<red>You have no unread mails!"}));
		languageKeys.put("CmdMail.Base.Read.Click", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<aqua>Read<gray>]",
						"<gray>[<aqua>Read<gray>]"}));
		languageKeys.put("CmdMail.Base.Read.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick hier, um die Mail zu lesen.",
						"<yellow>Click here to read the mail."}));
		languageKeys.put("CmdMail.Base.SendPlus.Click", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<red>Reply<gray>]",
						"<gray>[<red>Reply<gray>]"}));
		languageKeys.put("CmdMail.Base.SendPlus.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick hier, um eine Antwort an alle (Verfasser sowie CC) zu schreiben.",
						"<yellow>Click here to write a reply to all (authors as well as CC)."}));
		languageKeys.put("CmdMail.Base.SendMinus.Click", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<red>Reply<dark_red>All<gray>]",
						"<gray>[<red>Reply<dark_red>All<gray>]"}));
		languageKeys.put("CmdMail.Base.SendMinus.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick hier, um eine Antwort nur an Verfasser zu schreiben.",
						"<yellow>Click here to write a reply to author only."}));
		languageKeys.put("CmdMail.Base.Forward.Click", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<light_purple>Fwd<gray>]",
						"<gray>[<light_purple>Fwd<gray>]"}));
		languageKeys.put("CmdMail.Base.Forward.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick hier, um die Mail weiterzuleiten.",
						"<yellow>Click here to forward the mail."}));
		languageKeys.put("CmdMail.Base.Subject.Text", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						" <gold>{<white>%sender%<gold>} <light_purple>>> <reset>%subject%",
						" <gold>{<white>%sender%<gold>} <light_purple>>> <reset>%subject%"}));
		languageKeys.put("CmdMail.Base.Subject.TextII", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						" <gold>{<yellow>%reciver%<gold>} <light_purple><< <reset>%subject%",
						" <gold>{<yellow>%reciver%<gold>} <light_purple><< <reset>%subject%"}));
		languageKeys.put("CmdMail.Base.Subject.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Gesendet am <white>%sendeddate%~!~<light_purple>Gelesen am <white>%readeddate%~!~<red>CC: <white>%cc%",
						"<yellow>Sended on the <white>%sendeddate%~!~<red>CC: <white>%cc%"}));
		languageKeys.put("CmdMail.Base.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>===== <aqua>%mailscount% <white>Ungelesene Nachrichten<yellow>=====",
						"<yellow>===== <aqua>%mailscount% <white>Unreaded messages<yellow>====="}));
		//Forward
		languageKeys.put("CmdMail.Forward.CCHasAlreadyTheMail", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler hat diese Mail schon bekommen!",
						"<red>The player has already received this mail!"}));
		languageKeys.put("CmdMail.Forward.Sended", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<aqua>Mail<gray>] <yellow>Du hast dem Spieler <white>%player% <yellow>eine Mail weitergeleitet!",
						"<gray>[<aqua>Mail<gray>] <yellow>You have forwarded <white>%player% <yellow>your mail to the player!"}));
		
		languageKeys.put("CmdMail.Send.HasNewMail", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<aqua>Mail<gray>] <yellow>Der Spieler <white>%player% <yellow>hat dir eine Mail weitergeleitet!",
						"<gray>[<aqua>Mail<gray>] <yellow>The <white>%player% <yellow>has forwarded you a mail!"}));
		//LastMails
		languageKeys.put("CmdMail.LastReceivedMails.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<red>Seite %page% <white>der letzten empfangenen Mails von <aqua>%player%<yellow>=====",
						"<yellow>=====<red>Seite %page% <white>the last received mails von <aqua>%player%<yellow>====="}));
		languageKeys.put("CmdMail.LastReceivedMails.NoMail", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast keine Mails!",
						"<red>You have no mails!"}));
		languageKeys.put("CmdMail.LastSendedMails.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<red>Seite %page% <white>der letzten gesendeten Mails von <aqua>%player%<yellow>=====",
						"<yellow>=====<red>Seite %page% <white>the last sended mails von <aqua>%player%<yellow>====="}));
		languageKeys.put("CmdMail.LastSendedMails.NoMail", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast keine Mails!",
						"<red>You have no mails!"}));
		//Read
		languageKeys.put("CmdMail.Read.MailNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Diese Mail existiert nicht!",
						"<red>This mail does not exist!"}));
		languageKeys.put("CmdMail.Read.CannotReadOthersMails", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du darfst diese Mail nicht lesen, da sie nicht an dich adressiert ist!",
						"<red>You must not read this mail, it is not addressed to you!"}));
		languageKeys.put("CmdMail.Read.NoChannelIsNullChannel",
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Deine Mail kann nicht verarbeitet werden, da der Channel, welcher für das Verarbeiten der Mailnachricht zustandig ist, nicht existiert!",
						"<red>Your mail cannot be processed, because the channel which is used for processing the mail message does not exist!"}));
		languageKeys.put("CmdMail.Read.Headline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>==========<gray>[<red>Mail <white>%id%<gray>]<yellow>==========",
						"<yellow>==========<gray>[<red>Mail <white>%id%<gray>]<yellow>=========="}));
		languageKeys.put("CmdMail.Read.Sender", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Von: <white>%sender%",
						"<red>From: <white>%sender%"}));
		languageKeys.put("CmdMail.Read.Reciver", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>An: <white>%reciver%",
						"<red>To: <white>%reciver%"}));
		languageKeys.put("CmdMail.Read.CC", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>CC: <gray>[<white>%cc%<gray>]",
						"<red>CC: <gray>[<white>%cc%<gray>]"}));
		languageKeys.put("CmdMail.Read.Date", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Gesendet am: <reset>%sendeddate% | <red>Gelesen am: <reset>%readeddate%",
						"<red>Sended: <reset>%sendeddate% &| <green>Readed: <reset>%readeddate%"}));
		languageKeys.put("CmdMail.Read.Subject", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Betreff: <reset>%subject%",
						"<red>Subject: <reset>%subject%"}));
		languageKeys.put("CmdMail.Read.Bottomline", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>==========<gray>[<red>Mail Ende<gray>]<yellow>==========",
						"<yellow>==========<gray>[<red>Mail End<gray>]<yellow>=========="}));
		//Send
		languageKeys.put("CmdMail.Send.PlayerNotExist", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Einer der angegebenen Empfänger existiert nicht!",
						"<red>One of the specified recipients does not exist!"}));
		languageKeys.put("CmdMail.Send.OneWordMinimum", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Bitte gib mindestens 1 Wort als Nachricht an!",
						"<red>Please enter at least 1 word as message!"}));
		languageKeys.put("CmdMail.Send.Sended", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast eine Mail geschrieben.!",
						"<yellow>You have written an mail!"}));
		languageKeys.put("CmdMail.Send.SendedHover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Betreff: <reset>%subject%~!~<red>CC: <reset>%cc%",
						"<yellow>Subject: <reset>%subject%~!~<red>CC: <reset>%cc%"}));
		languageKeys.put("CmdMail.Send.HasNewMail", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<aqua>Mail<gray>] <yellow>Du hast eine neue Mail!",
						"<gray>[<aqua>Mail<gray>] <yellow>You have a new mail!"}));
		languageKeys.put("CmdMail.Send.Hover", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick auf die Nachricht um all deine neuen Mails zu sehen!",
						"<yellow>Click on the message to see all your new mails!"}));
		
		/*
		 * Scc
		 */
		languageKeys.put("CmdScc.OtherCmd", 
				new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Bitte nutze den Befehl mit einem weiteren Argument aus der Tabliste!",
						"<red>Please use the command with another argument from the tab list!"}));
		languageKeys.put("CmdScc.UsedChannelForBroadCastDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der in der config.yml definierte Channel existiert nicht für diesen Broadcast!",
						"<red>The channel selected in config.yml for a broadcast does not exist!"}));
		//Book
		languageKeys.put("CmdScc.Book.IsNotABook"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Das Item ist kein signiertes Buch!",
						"<red>The item is not a signed book!"}));
		//Broadcast
		languageKeys.put("CmdScc.Broadcast.Intro"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<gray>[<red>INFO<gray>] <reset>",
						"<gray>[<red>INFO<gray>] <reset>"}));
		//Channel
		languageKeys.put("CmdScc.Channel.ChannelDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der angegebene Channel existiert nicht!",
						"<red>The specified channel does not exist!"}));
		languageKeys.put("CmdScc.Channel.UsedChannelDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du kannst den angegebenen Channel nicht ändern, da du in diesen gar nicht schreiben darfst!",
						"<red>You can not change the specified channel, because you are not allowed to write in it!"}));
		languageKeys.put("CmdScc.Channel.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den Channel <green>%channel% <yellow>angeschaltet!",
						"<yellow>You have switched on the <green>%channel% <yellow>channel!"}));
		languageKeys.put("CmdScc.Channel.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den Channel <red>%channel% <yellow>ausgeschaltet!",
						"<yellow>You have turned off the <red>%channel% <yellow>channel!"}));
		
		languageKeys.put("CmdScc.ChannelGui.InvTitle"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"§c%player% §eChannels",
						"§c%player% §eChannels"}));
		//Ignore
		languageKeys.put("CmdScc.Ignore.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du ignorierst nun den Spieler <red>%player%<yellow>!",
						"<yellow>You now ignore the player <red>%player%<yellow>!"}));
		languageKeys.put("CmdScc.Ignore.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du ignorierst nun nicht mehr den Spieler <green>%player%<yellow>!",
						"<yellow>You are now no longer ignoring the player <green>%player%<yellow>!"}));
		languageKeys.put("CmdScc.Ignore.NoOne"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du ignorierst keinen Spieler!",
						"<yellow>You dont ignore any player!"}));
		languageKeys.put("CmdScc.Ignore.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Klick hier, um den Spieler nicht mehr zu ignorieren!",
						"<yellow>Click here to stop ignoring the player!"}));
		languageKeys.put("CmdScc.Ignore.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>===<aqua>Ignorier Liste von <white>%player%<yellow>===",
						"<yellow>===<aqua>Ignore list from <white>%player%<yellow>==="}));
		
		//Mute
		languageKeys.put("CmdScc.Mute.YouHaveBeenMuted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du wurdest bis zum <white>%time% <red>gemutet!",
						"<red>You have been muted to <white>%time%<red>!"}));
		languageKeys.put("CmdScc.Mute.YouhaveMuteThePlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast <red>%player% <yellow>bis zum <white>%time% <yellow>gemutet!",
						"<yellow>You have muted <red>%player% <yellow>to the <white>%time%<yellow>!"}));
		languageKeys.put("CmdScc.Mute.PlayerMute"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <red>%target% <yellow>wurde von <white>%player% <yellow>bis zum <white>%time% <yellow>gemutet!",
						"<yellow>The player <red>%target% <yellow>has been muted from <white>%player% <yellow>to <white>%time%<yellow>!"}));
		languageKeys.put("CmdScc.Mute.YouHaveBeenUnmute"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du wurdest entmutet!",
						"<yellow>You can join the chat again!"}));
		languageKeys.put("CmdScc.Mute.YouHaveUnmute"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den <white>%player% <yellow>entmutet!",
						"<yellow>You have the <white>%player% <yellow>unmuted!"}));
		languageKeys.put("CmdScc.Mute.PlayerUnmute"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>kann wieder schreiben.",
						"<yellow>The <white>%player%<yellow>can talk again."}));
		languageKeys.put("CmdScc.Performance.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<gray>[<red>Scc MySQLPerformance<gray>]<yellow>=====",
						"<yellow>=====<gray>[<red>Scc MysqlPerformance<gray>]<yellow>====="}));
		languageKeys.put("CmdScc.Performance.Subline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Zeitraum von <white>%begin% <yellow>bis <white>%end%",
						"<yellow>Time <white>%begin% <yellow>too <white>%end%"}));
		languageKeys.put("CmdScc.Performance.Text"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<dark_red>%server% <yellow>>> <gold>Inserts:<white>%insert% <aqua>Updates:<white>%update% <red>Deletes:<white>%delete% <yellow>Reads:<white>%read%",
						"<dark_red>%server% <yellow>>> <gold>Inserts:<white>%insert% <aqua>Updates:<white>%update% <red>Deletes:<white>%delete% <yellow>Reads:<white>%read%"}));
		
		//Option
		languageKeys.put("CmdScc.Option.Channel.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du siehst nun alle Aktiven Channels beim Login.",
						"<yellow>You will now see all active channels when you log in."}));
		languageKeys.put("CmdScc.Option.Channel.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du siehst nun nicht mehr alle Aktiven Channels beim Login.",
						"<yellow>You no longer see all active channels when you log in."}));
		languageKeys.put("CmdScc.Option.Join.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du siehst nun die Nachricht, wenn Spieler den Server verlassen oder joinen.",
						"<yellow>You will now see the message when players leave or join the server."}));
		languageKeys.put("CmdScc.Option.Join.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du siehst nun die Nachricht nicht mehr, wenn Spieler den Server verlassen oder joinen.",
						"<yellow>You no longer see the message when players leave or join the server."}));
		languageKeys.put("CmdScc.Option.Spy.Active"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du siehst nun alle Chatnachrichten, die dir sonst für dich verborgen wären.",
						"<yellow>You will now see all chat messages that would otherwise be hidden to you."}));
		languageKeys.put("CmdScc.Option.Spy.Deactive"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du siehst nun nur noch die Chatnachrichten, wozu du auch berechtigt bist.",
						"<yellow>You will now only see the chat messages that you are authorized to see."}));
		//ItemReplacer
		languageKeys.put("CmdScc.Item.InvTitle"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"§c%player% §eReplacer §6Items",
						"§c%player% §eReplacer §6Items"}));
		languageKeys.put("CmdScc.Item.YouCannotSaveItems"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du kannst keine Items vorspeichern!",
						"<red>You can't pre-store items!"}));
		languageKeys.put("CmdScc.Item.Rename.NotDefault"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der alte oder neue Name darf nicht <white>default <red>heißen!",
						"<red>The old or new name must not be <white>default<red>!"}));
		languageKeys.put("CmdScc.Item.Rename.ItemDontExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Das Item existiert nicht!",
						"<red>The item dont exist!"}));
		languageKeys.put("CmdScc.Item.Rename.NameAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Name ist schon vergeben!",
						"<red>The name is already taken!"}));
		languageKeys.put("CmdScc.Item.Rename.Renamed"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Das Item mit dem Name <white>%oldname% <yellow>wurde in <white>%newname% <yellow>umbenannt!",
						"<yellow>The item with the name <white>%oldname% &has been renamed to <white>%newname%!"}));
		languageKeys.put("CmdScc.Item.Replacers.ListEmpty"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast keine ItemReplacer!",
						"<red>You have no ItemReplacer!"}));
		languageKeys.put("CmdScc.Item.Replacers.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<gray>[<aqua>ItemReplacer<gray>]<yellow>=====",
						"<yellow>=====<gray>[<aqua>ItemReplacer<gray>]<yellow>====="}));
		
		//PermanentChannel
		languageKeys.put("CmdScc.PermanentChannel.YouAreNotInAChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist in keinem permanenten Channel!",
						"<red>You are not in a permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.YouAreNotTheOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist nicht der Ersteller in diesem permanenten Channel!",
						"<red>You are not the creator in this permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist weder der Ersteller noch ein Stellvertreter in diesem permanenten Channel!",
						"<red>You are neither the creator nor an vice in this permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.NotAChannelMember"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der angegebene Spieler ist nicht Mitglied im permanenten Channel!",
						"<red>The specified player is not a member of the permanent channel!"}));
		//Ban
		languageKeys.put("CmdScc.PermanentChannel.Ban.ViceCannotBanCreator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du kannst als Stellvertreter den Ersteller nicht bannen!",
						"<red>You cant ban the creator as a vice!"}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.OwnerCantSelfBan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Ersteller kann nicht gebannt werden!",
						"<red>The creator can not be banned!"}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.AlreadyBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler ist schon auf der gebannt!",
						"<red>The player is already on the banned!"}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.YouHasBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den Spieler <white>%player% <yellow>aus dem <dark_purple>Perma<white>nenten <yellow>Channel verbannt.",
						"<yellow>You have banned the <white>%player% <yellow>from the <dark_purple>perma<white>nent <yellow>Channel."}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.YourWereBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du wurdest vom Permanenten Channel <reset>%channel% <red>verbannt!",
						"<red>You were banned from the Permanent Channel <reset>%channel%<red>!"}));
		languageKeys.put("CmdScc.PermanentChannel.Ban.PlayerWasBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>wurde aus dem <dark_purple>Perma<white>nenten <yellow>Channel verbannt.",
						"<yellow>The player <white>%player% <yellow>has been banned from the <dark_purple>perma<white>nent <yellow>Channel."}));
		languageKeys.put("CmdScc.PermanentChannel.Unban.PlayerNotBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler ist nicht gebannt!",
						"<red>The player is not banned!"}));
		languageKeys.put("CmdScc.PermanentChannel.Unban.YouUnbanPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast <white>%player% <yellow>für den <dark_purple>Perma<white>nenten <yellow>Channel entbannt!",
						"<yellow>You have unbanned <white>%player% <yellow>for the <dark_purple>perma<white>nent <yellow>Channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.Unban.PlayerWasUnbanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>wurde für den <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel% <reset><yellow>entbannt.",
						"<yellow>The player <white>%player% <yellow>has been banned for the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<yellow>."}));
		//ChangePassword
		languageKeys.put("CmdScc.PermanentChannel.ChangePassword.Success"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast das Passwort des <dark_purple>Perma<white>nenten <yellow>Channel auf <white>%password% <yellow>geändert.",
						"<yellow>You have changed the password of the <dark_purple>perma<white>nent <yellow>Channel to <white>%password%<yellow>."}));
		//Channels
		languageKeys.put("CmdScc.PermanentChannel.Channels.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<dark_purple>[<dark_purple>Perma<white>nente <white>Channels<dark_purple>]<yellow>=====",
						"<yellow>=====<dark_purple>[<dark_purple>Perma<white>nente <white>Channels<dark_purple>]<yellow>====="}));
		//ChatColor
		languageKeys.put("CmdScc.PermanentChannel.ChatColor.NewColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Die Farben vom Chat des Channels <reset>%channel% <yellow>wurden in <white>%color%Beispielnachricht <reset><yellow>geändert.",
						"<yellow>The colors of the channel chat <reset>%channel% <yellow>have been changed to <reset>%color%example message <reset><yellow>."}));
		//Create
		languageKeys.put("CmdScc.PermanentChannel.Create.ChannelNameAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Dieser Name existiert bereits für einen Permanente Channel!",
						"<red>This name already exists for a permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.Create.MaximumAmount"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast schon die maximale Anzahl von Permanenten Channels erstellt. Lösche vorher einen von deinen Permanenten Channels, um einen neuen zu erstellen!",
						"<red>You have already created the maximum number of permanent channels. Delete one of your permanent channels before to create a new one!"}));
		languageKeys.put("CmdScc.PermanentChannel.Create.ChannelCreateWithoutPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <yellow>erstellt! Zum Schreiben am Anfang <white>%symbol% <yellow>nutzen.",
						"<yellow>You have created the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset>! To write at the beginning <white>%symbol% &use."}));
		languageKeys.put("CmdScc.PermanentChannel.Create.ChannelCreateWithPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <yellow>mit dem Passwort <white>%password% <yellow>erstellt! Zum Schreiben am Anfang <white>%symbol% <yellow>nutzen.",
						"<yellow>You have <red>reated the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset> &with the password <white>%password%! To write at the beginning <white>%symbol% &use."}));
		//Delete
		languageKeys.put("CmdScc.PermanentChannel.Delete.Confirm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Bist du sicher, dass du den Permanenten Channel <reset>%channel% <reset><red>löschen willst? Wenn ja, klicke auf diese Nachricht.",
						"<red>Are you sure you want to delete the Permanent Channel <reset>%channel%<red>? If yes, click on this message."}));
		languageKeys.put("CmdScc.PermanentChannel.Delete.Deleted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Permanente Channel <reset>%channel% <reset><red>wurde von %player% gelöscht. Alle Mitglieder verlassen somit diesen Channel.",
						"<red>The permanent channel <reset>%channel% <reset><red>has been deleted by %player%. All members leave this channel."}));
		//Info
		languageKeys.put("CmdScc.PermanentChannel.Info.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<dark_purple>[<dark_purple>Perma<white>nenter <white>Channel <reset>%channel%<reset><dark_purple>]<yellow>=====",
						"<yellow>=====<dark_purple>[<dark_purple>Perma<white>nent <white>Channel <reset>%channel%<reset><dark_purple>]<yellow>====="}));
		languageKeys.put("CmdScc.PermanentChannel.Info.ID"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel ID: <white>%id%",
						"<yellow>Channel ID: <white>%id%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Creator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Ersteller: <white>%creator%",
						"<yellow>Channel creator: <white>%creator%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Vice"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Stellvertreter: <white>%vice%",
						"<yellow>Channel vice: <white>%vice%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Members"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Mitglieder: <white>%members%",
						"<yellow>Channel members: <white>%members%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Password"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Passwort: <white>%password%",
						"<yellow>Channel password: <white>%password%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Symbol: <white>%symbol%",
						"<yellow>Channel symbol: <white>%symbol%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.ChatColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Chat Farben: <white>%color% Beispielnachricht",
						"<yellow>Channel chat colors: <white>%color% example message"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.NameColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Farben: <white>%color%",
						"<yellow>Channel colors: <white>%color%"}));
		languageKeys.put("CmdScc.PermanentChannel.Info.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Gebannte Spieler: <white>%banned%",
						"<yellow>Channel banned players: <white>%banned%"}));
		//Inherit
		languageKeys.put("CmdScc.PermanentChannel.Inherit.NewCreator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Im <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel% <reset><yellow>beerbt der Spieler <green>%creator% <yellow>den Spieler <red>%oldcreator% <yellow>als neuer Ersteller des Channels.",
						"<yellow>In the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel% <reset><yellow>inherits the player <green>%creator% &the player <red>%oldcreator% <yellow>as the new creator of the channel."}));
		//Invite
		languageKeys.put("CmdScc.PermanentChannel.Invite.Cooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast schon in der letzten Zeit jemanden eingeladen! Bitte warte bis %time%, um die nächsten Einladung zu verschicken!",
						"<red>You have already invited someone in the last time! Please wait until %time% to send the next invitation!"}));
		languageKeys.put("CmdScc.PermanentChannel.Invite.SendInvite"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den Spieler <gold>%target% <yellow>in den <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel% <reset><green>eingeladen.",
						"<yellow>You have invited the player <gold>%target% <yellow>into the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset><yellow>."}));
		languageKeys.put("CmdScc.PermanentChannel.Invite.Invitation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du wurdest vom Spieler <gold>%player% <yellow>in den <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel% <reset><green>eingeladen. Klicke auf die Nachricht zum Betreten des Channels.",
						"<yellow>You have been invited by the <gold>%player% &into the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset><yellow>. Click on the message to enter the channel."}));
		//Join
		languageKeys.put("CmdScc.PermanentChannel.Join.UnknownChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Es gibt keinen Permanenten Channel mit dem Namen <white>%name%<red>!",
						"<red>There is no permanent channel with the name <white>%name%<red>!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist in diesem Permanenten Channel gebannt und darfst nicht beitreten!",
						"<red>You are banned in this permanent channel and are not allowed to join!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.AlreadyInTheChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist schon diesem Permanenten Channel beigetreten!",
						"<red>You have already joined this permanent channel!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.ChannelHasPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Permanente Channel hat ein Passwort, bitte gib dieses beim Beitreten an!",
						"<red>The Permanent Channel has a password, please enter it when joining!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.PasswordIncorrect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Das angegebene Passwort ist nicht korrekt!",
						"<red>The specified password is not correct!"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.ChannelJoined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du bist dem <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <green>beigetreten<yellow>! ChannelSymbol: <reset>%symbol%",
						"<yellow>You have joined the <dark_purple>Perma<white>nent <yellow>Channel <reset>%channel%<reset><yellow>! ChannelSymbol: <reset>%symbol%"}));
		languageKeys.put("CmdScc.PermanentChannel.Join.PlayerIsJoined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Spieler <white>%player% <yellow>ist dem <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel% <reset><yellow>beigetreten!",
						"<yellow>Player <white>%player% <yellow>has joined the <dark_purple>Perma<white>nent <yellow>Channel <reset>%channel%<reset><yellow>!"}));
		//Kick
		languageKeys.put("CmdScc.PermanentChannel.Kick.ViceCannotKickCreator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du kannst als Stellvertreter den Ersteller nicht kicken!",
						"<red>You can't kick the creator as a vice!"}));
		languageKeys.put("CmdScc.PermanentChannel.Kick.CannotSelfKick"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du kannst dich nicht kicken!",
						"<red>You can't kick yourself!"}));
		languageKeys.put("CmdScc.PermanentChannel.Kick.YouWereKicked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du wurdest aus dem Permanenten <yellow>Channel <reset>%channel%<reset> <red>gekickt!",
						"<red>You have been kicked out of the Permanent <yellow>Channel <reset>%channel%<reset><red>!"}));
		languageKeys.put("CmdScc.PermanentChannel.Kick.YouKicked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast <white>%player% <yellow>aus dem <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <yellow>gekickt!",
						"<yellow>You have <white>%player% <yellow> kicked out of the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset> <yellow>gekickt!"}));
		languageKeys.put("CmdScc.PermanentChannel.Kick.KickedSomeone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>wurde aus dem <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <yellow>gekickt!",
						"&The player <white>%player% &has been kicked out of the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset> <yellow>gekickt!"}));
		//Leave
		languageKeys.put("CmdScc.PermanentChannel.Leave.Confirm"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red><b>Achtung!</b> <reset><red>Bist du sicher, dass du den Channel verlassen willst? Wenn der Ersteller den Permanenten Channel verlässt, wird dieser gelöscht! Bitte bestätigen mit dem Klick auf diese Nachricht.",
						"<red><b>Attention!</b> <reset><red>Are you sure you want to leave the channel? If the creator leaves the permanent channel, it will be deleted! Please confirm and just click on this message."}));
		languageKeys.put("CmdScc.PermanentChannel.Leave.CreatorLeft"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Ersteller hat den Channel verlassen und ihn somit aufgelöst. Alle Mitglieder haben somit den Permanenten Channel <reset>%channel%<reset> <red>verlassen!",
						"<red>The creator has left the channel and thus dissolved it. All members have left the permanent channel <reset>%channel%<reset> <red>!"}));
		languageKeys.put("CmdScc.PermanentChannel.Leave.YouLeft"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <yellow>verlassen!",
						"<yellow>You have left the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset> <yellow>ver!"}));
		languageKeys.put("CmdScc.PermanentChannel.Leave.PlayerLeft"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler <white>%player% <red>hat den permanenten Channel <reset>%channel%<reset> <red>verlassen!",
						"<red>The player <white>%player% <red>has left the permanent channel <reset>%channel%<reset> <red>!"}));
		//NameColor
		languageKeys.put("CmdScc.PermanentChannel.NameColor.NewColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Die Farben vom Namen des <dark_purple>Perma<white>nenten Channels <reset>%channel%<reset> <yellow>wurden in <white>%color%Beispielnachricht <reset><yellow>geändert.",
						"&The colors from the name of the <dark_purple>perma<white>nent channel <reset>%channel%<reset> &have been changed to <white>%color%example message <reset><yellow>."}));
		//Player
		languageKeys.put("CmdScc.PermanentChannel.Player.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<dark_purple>[<dark_purple>Perma<white>nente <white>Channels von <gold>%player%<dark_purple>]<yellow>=====",
						"<yellow>=====<dark_purple>[<dark_purple>Perma<white>nent <white>channels from <gold>%player%<dark_purple>]<yellow>====="}));
		languageKeys.put("CmdScc.PermanentChannel.Player.Creator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Ist Ersteller von: <reset>%creator%",
						"<yellow>Is creator from: <reset>%creator%"}));
		languageKeys.put("CmdScc.PermanentChannel.Player.Vice"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Ist Vertreter bei: <reset>%vice%",
						"<yellow>Is vice in: <reset>%vice%"}));
		languageKeys.put("CmdScc.PermanentChannel.Player.Member"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Ist Mitglied bei: <reset>%member%",
						"<yellow>Is member in: <reset>%member%"}));
		languageKeys.put("CmdScc.PermanentChannel.Player.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Ist Gebannt bei: <reset>%banned%",
						"<yellow>Is banned in: <reset>%banned%"}));
		//Rename
		languageKeys.put("CmdScc.PermanentChannel.Rename.NameAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Es gibt schon einen Permanenten Channel <reset>%channel%<reset><red>!",
						"<red>There is already a Permanent Channel <reset>%channel%<reset><red>!"}));
		languageKeys.put("CmdScc.PermanentChannel.Rename.Renaming"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der <dark_purple>Perma<white>nente <yellow>Channel <reset>%oldchannel% <reset><yellow>wurde in <reset>%channel% <reset><yellow>umbenannt.",
						"<yellow>The <dark_purple>Perma<white>nente <yellow>Channel <reset>%oldchannel% <reset>&has been renamed <reset>%channel<reset><yellow>."}));
		//Symbol
		languageKeys.put("CmdScc.PermanentChannel.Symbol.SymbolAlreadyExist"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Das Symbol <white>%symbol% <red>wird schon von dem Permanenten Channel <reset>%channel%<reset> <red>benutzt!",
						"<red>The symbol <white>%symbol% <red>is already used by the Permanent Channel <reset>%channel%<reset><red>!"}));
		languageKeys.put("CmdScc.PermanentChannel.Symbol.NewSymbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Für den <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <yellow>gibt es ein neues Symbol: <white>%symbol%",
						"<yellow>For the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset> &there is a new symbol: <white>%symbol%"}));
		//Vice
		languageKeys.put("CmdScc.PermanentChannel.Vice.Degraded"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>wurde zum Mitglied im <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <red>degradiert<yellow>!",
						"&The player <white>%player% &has been <light_purple>egraded<yellow> to a member of the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset><red>!"}));
		languageKeys.put("CmdScc.PermanentChannel.Vice.Promoted"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>wurde zum Stellvertreter im <dark_purple>Perma<white>nenten <yellow>Channel <reset>%channel%<reset> <green>befördert<yellow>!",
						"&Tehe player <white>%player% &has been &promoted<yellow> to vice in the <dark_purple>perma<white>nent <yellow>Channel <reset>%channel%<reset>!"}));
		
		//TemproraryChannel
		languageKeys.put("CmdScc.TemporaryChannel.YouAreNotInAChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist in keinem temporären Channel!",
						"<red>You are not in a temporary channel!"}));
		languageKeys.put("CmdScc.TemporaryChannel.YouAreNotTheOwner"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist nicht der Ersteller in diesem temporären Channel!",
						"<red>You are not the creator in this temporary channel!"}));
		languageKeys.put("CmdScc.TemporaryChannel.NotAChannelMember"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der angegebene Spieler ist nicht Mitglied im temporären Channel!",
						"<red>The specified player is not a member of the temporary Channel!"}));
		//Ban
		languageKeys.put("CmdScc.TemporaryChannel.Ban.CreatorCannotSelfBan"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du als Ersteller kannst dich nicht selber bannen!",
						"<red>You as the creator can not ban yourself!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.AlreadyBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler ist schon gebannt!",
						"<red>The player is already banned!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.YouHasBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den Spieler <white>%player% <yellow>aus dem <dark_purple>Temp<white>orären <yellow>Channel verbannt.",
						"<yellow>You have banned the <white>%player% <yellow>from the <dark_purple>temp<white>orary <yellow>Channel."}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.YourWereBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du wurdest vom Temporären Channel <white>%channel% <red>verbannt!",
						"<red>You have been <white>%channel% <red>banned from the Temporary Channel!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.CreatorHasBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>wurde aus dem <dark_purple>temp<white>orären <yellow>Channel verbannt.",
						"<yellow>The player <white>%player% &has been banned from the <dark_purple>temp<white>orary <yellow>Channel."}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.PlayerNotBanned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Spieler ist nicht gebannt!",
						"<red>The player is not banned!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.YouUnbanPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast <white>%player% <yellow>für den <dark_purple>temp<white>orären <yellow>Channel entbannt!",
						"<yellow>You have unbanned <white>%player% <white>or the <dark_purple>temp<white>orary <yellow>Channel!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Ban.CreatorUnbanPlayer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>wurde für den <dark_purple>Temp<white>orären <yellow>Channel entbannt.",
						"&The player <white>%player% <yellow>has been unbanned for the <dark_purple>temp<white>orary <yellow>Channel."}));
		//ChangePassword
		languageKeys.put("CmdScc.TemporaryChannel.ChangePassword.PasswordChange"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast das Passwort zu <white>%password% <yellow>geändert!",
						"<yellow>You have changed the password to <white>%password%!"}));
		//Create
		languageKeys.put("CmdScc.TemporaryChannel.Create.AlreadyInAChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist schon in dem Temporären Channel <white>%channel%<red>! Um einen neuen Temporären Channel zu eröffnen, müsst du den vorherigen erst schließen.",
						"<red>You are already in the temporary channel <white>%channel%<red>! To open a new temporary channel, you must first close the previous one."}));
		languageKeys.put("CmdScc.TemporaryChannel.Create.ChannelCreateWithoutPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den <dark_purple>temp<white>orären <yellow>Channel <white>%channel% <yellow>erstellt!",
						"<yellow>You have set the <dark_purple>temp<white>or <yellow>Channel <white>%channel%<yellow>!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Create.ChannelCreateWithPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den <dark_purple>Temp<white>orären <yellow>Channel <white>%channel% <yellow>mit dem Passwort <white>%password% <yellow>erstellt!",
						"<yellow>You have created the <dark_purple>Temp<white>or <yellow>Channel <white>%channel% &with the password <white>%password%!"}));
		//Info
		languageKeys.put("CmdScc.TemporaryChannel.Info.Headline"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>=====<dark_purple>[<dark_purple>Temp<white>orären <white>Channel <gold>%channel%<dark_purple>]<yellow>=====",
						"<yellow>=====<dark_purple>[<dark_purple>Temp<white>orary <white>Channel <gold>%channel%<dark_purple>]<yellow>====="}));
		languageKeys.put("CmdScc.TemporaryChannel.Info.Creator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Ersteller: <white>%creator%",
						"<yellow>Channel creator: <white>%creator%"}));
		languageKeys.put("CmdScc.TemporaryChannel.Info.Members"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Mitglieder: <white>%members%",
						"<yellow>Channel members: <white>%members%"}));
		languageKeys.put("CmdScc.TemporaryChannel.Info.Password"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Passwort: <white>%password%",
						"<yellow>Channel password: <white>%password%"}));
		languageKeys.put("CmdScc.TemporaryChannel.Info.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Channel Gebannte Spieler: <white>%banned%",
						"<yellow>Channel banned players: <white>%banned%"}));
		//Invite
		languageKeys.put("CmdScc.TemporaryChannel.Invite.Cooldown"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du hast schon in der letzten Zeit jemanden eingeladen! Bitte warte etwas bis zur nächsten Einladung!",
						"<red>You have already invited someone in the last time! Please wait a little until the next invitation!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Invite.SendInvite"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den Spieler <gold>%target% <yellow>in den <dark_purple>temp<white>orären <yellow>Channel <gold>%channel% <green>eingeladen.",
						"<yellow>You have invited the player <gold>%target% &into the <dark_purple>temp<white>orary <yellow>Channel <gold>%channel%<yellow>."}));
		languageKeys.put("CmdScc.TemporaryChannel.Invite.Invitation"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du wurdest vom Spieler <gold>%player% <yellow>in den <dark_purple>temp<white>orären <yellow>Channel <gold>%channel% <green>eingeladen. Klicke auf die Nachricht zum Betreten des Channels.",
						"<yellow>You have been invited by the player <gold>%player% &into the <dark_purple>temp<white>orary <yellow>Channel <gold>%channel%<yellow>. Click on the message to enter the channel."}));
		//Join
		languageKeys.put("CmdScc.TemporaryChannel.Join.AlreadyInAChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist schon in einem anderen Temporären Channel beigetreten, verlasse erst diesen!",
						"<red>You have already joined another temporary channel, leave this one first!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.UnknownChannel"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Es gibt keinen Temporären Channel mit dem Namen <white>%name%<red>!",
						"<red>There is no temporary channel with the name <white>%name%<red>!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.Banned"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du bist in diesem Temporären Channel gebannt und darfst nicht beitreten!",
						"<red>You are banned in this temporary channel and are not allowed to join!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.ChannelHasPassword"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Der Temporäre Channel hat ein Passwort, bitte gib dieses beim Beitreten an!",
						"<red>The Temporary Channel has a password, please enter it when joining!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.PasswordIncorrect"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Das angegebene Passwort ist nicht korrekt!",
						"<red>The specified password is not correct!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.ChannelJoined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du bist dem <dark_purple>temp<white>orären <yellow>Channel <white>%channel% <green>beigetreten!",
						"<yellow>You have joined the <dark_purple>temp<white>orary <yellow>Channel <white>%channel%!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Join.PlayerIsJoined"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Spieler <white>%player% <yellow>ist dem <dark_purple>temp<white>orären <yellow>Channel beigetreten!",
						"<yellow>Player <white>%player% &has joined the <dark_purple>temp<white>orary <yellow>Channel!"}));
		//Kick
		languageKeys.put("CmdScc.TemporaryChannel.Kick.CreatorCannotSelfKick"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du als Ersteller kannst dich nicht kicken!",
						"<red>You as the creator can not kick you!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Kick.YouKicked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast <white>%player% <yellow>aus dem <dark_purple>temp<white>orären <yellow>Channel %channel% gekickt!",
						"<yellow>You have kicked <white>%player% <yellow>out of the <dark_purple>temp<white>orary <yellow>Channel %channel%!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Kick.YouWereKicked"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<red>Du wurdest aus dem temporären Channel <white>%channel% <red>gekickt!",
						"<red>You have been kicked out of the Temporary Channel <white>%channel%<red>!"}));
		languageKeys.put("CmdScc.TemporaryChannel.Kick.CreatorKickedSomeone"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>wurde aus dem <dark_purple>temp<white>orären <yellow>Channel %channel% gekickt!",
						"&The player <white>%player% <yellow>has been kicked out of the <dark_purple>temp<white>orär <yellow>Channel %channel%!"}));
		//Leave
		languageKeys.put("CmdScc.TemporaryChannel.Leave.NewCreator"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du wurdest der neue Ersteller des <dark_purple>temp<white>orären <yellow>Channels <white>%channel%",
						"<yellow>You became the new creator of the <dark_purple>temp<white>orary <yellow>Channel <white>%channel%"}));
		languageKeys.put("CmdScc.TemporaryChannel.Leave.YouLeft"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den <dark_purple>temp<white>orären <yellow>Channel <white>%channel% <yellow>verlassen!",
						"<yellow>You have left the <dark_purple>temp<white>orary <yellow>Channel <white>%channel% <yellow>ever!"}));
		
		//UpdatePlayer
		languageKeys.put("CmdScc.UpdatePlayer.IsUpdated"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Du hast den Spieler <white>%player% <yellow>neu bewerten lassen! Seine aktiven Channels sind nach seinen Permissions neu eingestellt worden.",
						"<yellow>You have had the player <white>%player% <yellow>evaluated! His active channels have been reset after his permission."}));
		languageKeys.put("CmdScc.UpdatePlayer.YouWasUpdated"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"<yellow>Der Spieler <white>%player% <yellow>hat deine aktiven Channels nach deinen Permissions neu einstellen lassen.",
						"<yellow>The player <white>%player% <yellow>has your active channels reset according to your permission."}));
		/*languageKeys.put(""
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"",
				""}));
		languageKeys.put("CmdScc.TemporaryChannel.Leave."
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
						"",
						""}));*/
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
				"<gray>[<dark_red>Admin<gray>]"}));
		chatTitleKeys.put("admin.InChatColorCode"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<dark_red>"}));
		chatTitleKeys.put("admin.SuggestCommand"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"/rules"}));
		chatTitleKeys.put("admin.Hover"
				, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
				"<yellow>Die Admins sind für die administrative Arbeit auf dem Server zuständig.~!~Für Hilfe im Spielbetrieb sind sie aber die letzte Instanz.",
				"<yellow>The admins are responsible for the administrative work on the server.~!~But for help in the game operation they are the last instance."}));
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
		channelsKeys.put("private.LogInConsole"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("private.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"/msg"}));
		channelsKeys.put("private.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>[Private]"}));
		channelsKeys.put("private.InChatColorMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<light_purple>"}));
		channelsKeys.put("private.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.channel.private"}));
		channelsKeys.put("private.JoinPart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>Private <gray>= /msg"}));
		channelsKeys.put("private.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[%time%<gray>] %playername_with_prefixhighcolorcode% <yellow>>> %other_playername_with_prefixhighcolorcode% <gray>: %message%"}));
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
				"<gray>"}));
		channelsKeys.put("private.PlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
		channelsKeys.put("private.OtherPlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
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
				"proxy;<dark_green>BungeeCord;/warp spawn;<yellow>Der Proxy ist der Verwalter aller Spigotserver.",
				"hub;<green>Hub;/warp hub;<yellow>Vom Hub kommst du zu alle~!~<yellow>andere Server.",
				"nether;<red>Nether;/warp nether;<red>Die Hölle"}));
		channelsKeys.put("private.WorldConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"spawn;<green>Spawn;/warp spawn;<yellow>Vom Spawn kommst du zu alle~!~<yellow>andere Server.",
				"nether;<red>Nether;/warp nether;<red>Die Hölle"}));
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
		channelsKeys.put("permanent.LogInConsole"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("permanent.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"."}));
		channelsKeys.put("permanent.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<light_purple>[%channel%<light_purple>]"}));
		channelsKeys.put("permanent.InChatColorMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<white>"}));
		channelsKeys.put("permanent.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.channel.permanent"}));
		channelsKeys.put("permanent.JoinPart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<light_purple>Perma<gray>nent <gray>= ."}));
		channelsKeys.put("permanent.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[%time%<gray>] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%<gray>: %message%"}));
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
				"<gray>"}));
		channelsKeys.put("permanent.PlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
		channelsKeys.put("permanent.OtherPlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
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
				"proxy;<dark_green>BungeeCord;/warp spawn;<yellow>Der Proxy ist der Verwalter aller Spigotserver.",
				"hub;<green>Hub;/warp hub;<yellow>Vom Hub kommst du zu alle~!~<yellow>andere Server.",
				"nether;<red>Nether;/warp nether;<red>Die Hölle"}));
		channelsKeys.put("permanent.WorldConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"spawn;<green>Spawn;/warp spawn;<yellow>Vom Spawn kommst du zu alle~!~<yellow>andere Server.",
				"nether;<red>Nether;/warp nether;<red>Die Hölle"}));
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
		channelsKeys.put("temporary.LogInConsole"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("temporary.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				";"}));
		channelsKeys.put("temporary.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<dark_purple>[%channel%]"}));
		channelsKeys.put("temporary.InChatColorMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<dark_purple>"}));
		channelsKeys.put("temporary.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.channel.temporary"}));
		channelsKeys.put("temporary.JoinPart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<dark_purple>Temporary <gray>= ;"}));
		channelsKeys.put("temporary.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[%time%<gray>] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%<gray>: %message%"}));
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
				"<gray>"}));
		channelsKeys.put("temporary.PlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
		channelsKeys.put("temporary.OtherPlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
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
				"proxy;<dark_green>BungeeCord;/warp spawn;<yellow>Der Proxy ist der Verwalter aller Spigotserver.",
				"hub;<green>Hub;/warp hub;<yellow>Vom Hub kommst du zu alle~!~<yellow>andere Server.",
				"nether;<red>Nether;/warp nether;<red>Die Hölle"}));
		channelsKeys.put("temporary.WorldConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"spawn;<green>Spawn;/warp spawn;<yellow>Vom Spawn kommst du zu alle~!~<yellow>andere Server.",
				"nether;<red>Nether;/warp nether;<red>Die Hölle"}));
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
		channelsKeys.put("global.LogInConsole"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("global.Symbol"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"NULL"}));
		channelsKeys.put("global.InChatName"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>[G]"}));
		channelsKeys.put("global.InChatColorMessage"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
		channelsKeys.put("global.Permission"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"scc.channel.global"}));
		channelsKeys.put("global.JoinPart"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>Global <gray>= Without Symbol"}));
		channelsKeys.put("global.ChatFormat"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<gray>[%time%<gray>] %channel% %prefixall% %playername_with_prefixhighcolorcode% %suffixall%<gray>: %message%"}));
		channelsKeys.put("global.UseIncludedServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("global.IncludedServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"servername1",
				"servername2"}));
		channelsKeys.put("global.UseExcludedServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				false}));
		channelsKeys.put("global.ExcludedServer"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"servername1",
				"servername2"}));
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
				"<gray>"}));
		channelsKeys.put("global.PlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
		channelsKeys.put("global.OtherPlayernameCustomColor"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"<yellow>"}));
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
				"proxy;<dark_green>BungeeCord;/warp spawn;<yellow>Der Proxy ist der Verwalter aller Spigotserver.",
				"hub;<green>Hub;/warp hub;<yellow>Vom Hub kommst du zu alle~!~<yellow>andere Server.",
				"nether;<red>Nether;/warp nether;<red>Die Hölle"}));
		channelsKeys.put("global.WorldConverter"
				, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
				"spawn;<green>Spawn;/warp spawn;<yellow>Vom Spawn kommst du zu alle~!~<yellow>andere Server.",
				"nether;<red>Nether;/warp nether;<red>Die Hölle"}));
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
			gui.put(function+"."+settingLevel.getName()+".Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					displaynameGER,
					displaynameENG}));
			gui.put(function+"."+settingLevel.getName()+".Slot"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					slot}));
			gui.put(function+"."+settingLevel.getName()+".Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					material.toString()}));
			gui.put(function+"."+settingLevel.getName()+".Amount"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					amount}));
			if(urlTexture != null)
			{
				gui.put(function+"."+settingLevel.getName()+".PlayerHeadTexture"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					urlTexture}));
			}
			if(itemflag != null)
			{
				gui.put(function+"."+settingLevel.getName()+".Itemflag"
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						itemflag}));
			}
			if(enchantments != null)
			{
				gui.put(function+"."+settingLevel.getName()+".Enchantments"
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						enchantments}));
			}
			if(lore != null)
			{
				gui.put(function+"."+settingLevel.getName()+".Lore"
						, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, lore));
			}
			guiKeys.replace(type.toString(), gui);
		} else
		{
			LinkedHashMap<String, Language> gui = new LinkedHashMap<>();
			gui.put(function+"."+settingLevel.getName()+".Name"
					, new Language(new ISO639_2B[] {ISO639_2B.GER, ISO639_2B.ENG}, new Object[] {
					displaynameGER,
					displaynameENG}));
			gui.put(function+"."+settingLevel.getName()+".Slot"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					slot}));
			gui.put(function+"."+settingLevel.getName()+".Material"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					material.toString()}));
			gui.put(function+"."+settingLevel.getName()+".Amount"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					amount}));
			if(urlTexture != null)
			{
				gui.put(function+"."+settingLevel.getName()+".PlayerHeadTexture"
					, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
					urlTexture}));
			}
			if(itemflag != null)
			{
				gui.put(function+"."+settingLevel.getName()+".Itemflag"
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						itemflag}));
			}
			if(enchantments != null)
			{
				gui.put(function+"."+settingLevel.getName()+".Enchantments"
						, new Language(new ISO639_2B[] {ISO639_2B.GER}, new Object[] {
						enchantments}));
			}
			if(lore != null)
			{
				gui.put(function+"."+settingLevel.getName()+".Lore"
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
		setSlot(GuiType.CHANNELS, 22, GuiValues.CHANNELGUI_FUNCTION+"_Private",
				SettingsLevel.NOLEVEL, org.bukkit.Material.PAPER, 1,
				null,
				"<yellow>PrivatChat: %boolean%",
				"<yellow>PrivateChat: %boolean%",
				null,//Itemflag
				null,//Ench
				null);
	}
}
