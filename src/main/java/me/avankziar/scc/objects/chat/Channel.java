package main.java.me.avankziar.scc.objects.chat;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Channel
{
	public enum ChatFormatPlaceholder
	{
		TIME("%time%"), TIMES("%times%"),
		SERVER("%server%"), OTHER_SERVER("%other_server%"),
		WORLD("%world%"), OTHER_WORLD("%other_world%"),
		NEWLINE("%newline%"),
		
		CHANNEL("%channel%"),
		PREFIXHIGH("%prefixhigh%"), PREFIXALL("%prefixall%"), PREFIXLOW("%prefixlow%"),
		PLAYERNAME("%playername%"), PLAYERNAME_WITH_CUSTOMCOLOR("%playername_with_customcolor%"),
		PLAYERNAME_WITH_PREFIXHIGHCOLORCODE("%playername_with_prefixhighcolorcode%"),
		PLAYERNAME_WITH_SUFFIXHIGHCOLORCODE("%playername_with_suffixhighcolorcode%"),
		SUFFIXHIGH("%suffixhigh%"), SUFFIXALL("%suffixall%"), SUFFIXLOW("%suffixlow%"),
		ROLEPLAYNAME("%roleplayname%"),
		
		OTHER_PREFIXHIGH("%other_prefixhigh%"), OTHER_PREFIXALL("%other_prefixall%"), OTHER_PREFIXLOW("%other_prefixlow%"),
		OTHER_PLAYERNAME("%other_playername%"), OTHER_PLAYERNAME_WITH_CUSTOMCOLOR("%other_playername_with_customcolor%"),
		OTHER_PLAYERNAME_WITH_PREFIXHIGHCOLORCODE("%other_playername_with_prefixhighcolorcode%"),
		OTHER_PLAYERNAME_WITH_SUFFIXHIGHCOLORCODE("%other_playername_with_suffixhighcolorcode%"),
		OTHER_SUFFIXHIGH("%other_suffixhigh%"), OTHER_SUFFIXALL("%other_suffixall%"), OTHER_SUFFIXLOW("%other_suffixlow%"),
		OTHER_ROLEPLAYNAME("%other_roleplayname%"),
		
		MESSAGE("%message%"),
		UNDEFINE("");
		
		ChatFormatPlaceholder(String placeholder)
		{
			this.placeholder = placeholder;
		}
		
		private final String placeholder;
		
		public String getPlaceholder()
		{
			return placeholder;
		}
		
		public static Channel.ChatFormatPlaceholder getEnum(String ph)
		{
			for(Channel.ChatFormatPlaceholder v : values())
			{
				if(v.getPlaceholder().equals(ph))
				{
					return v;
				}
			}
			return Channel.ChatFormatPlaceholder.UNDEFINE;
		}
	}
	/**
	 * The unique identifier name.
	 */
	private String uniqueIdentifierName;
	/**
	 * The symbol to use the Channel.
	 */
	private String symbol;
	/**
	 * The in chat name with color code & or &# 
	 */
	private String inChatName;
	/**
	 * The using chatcolor for the message.
	 */
	private String inChatColorMessage;
	/**
	 * The permission of this channel to use.
	 */
	private String permission;
	/**
	 * The Joinmessage part.
	 */
	private String joinPart;
	/**
	 * The chatformat for this channel.
	 * @param %time%<br>The time in the format HH:mm
	 * @param %times%<br>The time in the format HH:mm:ss
	 * @param %channel%<br>The channelname. Mean the value inChatName.
	 * @param %prefixhigh%<br>The prefix with the most weight
	 * @param %prefixlow%<br>The prefix with the lowest weight
	 * @param %prefixall%<br>All prefix which the player has
	 * @param %playername%<br>The playername without colorcode
	 * @param %playername_with_prefixhighcolorcode%<br>The playername with the colorcode of the highst prefix
	 * @param %playername_with_suffixhighcolorcode%<br>The playername with the colorcode of the highst suffix
	 * @param %playeruuid%<br>The playeruuid
	 * @param %playeruuid_with_prefixhighcolorcode%<br>The playeruuid with the colorcode of the highst prefix
	 * @param %playeruuid_with_suffixhighcolorcode%<br>The playeruuid with the colorcode of the highst suffix
	 * @param %suffixhigh%<br>The suffix with the most weight
	 * @param %suffixlow%<br>The suffix with the lowest weight
	 * @param %suffixall%<br>All suffix which the player has
	 * @param %message%<br>The message. The message is always wit the inChatColorMessage value as colorcode.
	 */
	private String chatFormat;
	/**
	 * Specifies all server, where the player must be, to recieve the message.
	 */
	private ArrayList<String> includedServer;
	/*
	 * Specifies all server, where the player must not be, to recieve the message.
	 */
	private ArrayList<String> excludedServer;
	/**
	 * Specifies whether the channel is for a Specific Server only.
	 */	
	private boolean specificServer;
	/**
	 * Specifies whether the channel is for a Specific world only.
	 */
	private boolean specificWorld;
	/**
	 * Specifies whether the channel is for local in a specific radius only.
	 */
	private int blockRadius;
	/**
	 * Define, how much time must go by, before the player can send a message in the same channel.
	 */
	private long timeBetweenMessages;
	/**
	 * Define, how much time must go by, before the player can send the same message in the same channel.
	 */
	private long timeBetweenSameMessages;
	/**
	 * Define, how much percent the last message of the player in this channel, must be the different, as the last.
	 */
	private double percentOfSimilarity;
	
	private String timeColor;
	
	private String playernameCustomColor;
	
	private String otherplayernameCustomColor;
	
	private String seperatorBetweenPrefix;
	
	private String seperatorBetweenSuffix;
	
	private String mentionSound;
	
	private LinkedHashMap<String, String> serverReplacerMap;
	
	private LinkedHashMap<String, String> serverCommandMap;
	
	private LinkedHashMap<String, String> serverHoverMap;
	
    private LinkedHashMap<String, String> worldReplacerMap;
	
	private LinkedHashMap<String, String> worldCommandMap;
	
	private LinkedHashMap<String, String> worldHoverMap;
	
	private boolean useColor;
	
	private boolean useItemReplacer;
	
	private boolean useBookReplacer;
	
	private boolean useRunCommandReplacer;
	
	private boolean useSuggestCommandReplacer;
	
	private boolean useWebsiteReplacer;
	
	private boolean useEmojiReplacer;
	
	private boolean useMentionReplacer;
	
	private boolean usePositionReplacer;
	
	public Channel(String uniqueIdentifierName, String symbol,
			String inChatName, String inChatColorMessage, String permission, String joinPart, String chatFormat,
			ArrayList<String> includedServer, ArrayList<String> excludedServer,
			boolean specificServer, boolean specificWorld, int blockRadius,
			long timeBetweenMessages, long timeBetweenSameMessages, double percentOfSimilarity,
			String timeColor, String playernameCustomColor, String otherplayernameCustomColor,
			String seperatorBetweenPrefix, String seperatorBetweenSuffix, String mentionSound,
			LinkedHashMap<String, String> serverReplacerMap, LinkedHashMap<String, String> serverCommandMap,
			LinkedHashMap<String, String> serverHoverMap, LinkedHashMap<String, String> worldReplacerMap, 
			LinkedHashMap<String, String> worldCommandMap, LinkedHashMap<String, String> worldHoverMap,
			boolean useColor, boolean useItemReplacer, boolean useBookReplacer, boolean useRunCommandReplacer,
			boolean useSuggestCommandReplacer, boolean useWebsiteReplacer,
			boolean useEmojiReplacer, boolean useMentionReplacer, boolean usePositionReplacer)
	{
		setUniqueIdentifierName(uniqueIdentifierName);
		setSymbol(symbol);
		setInChatName(inChatName);
		setInChatColorMessage(inChatColorMessage);
		setPermission(permission);
		setJoinPart(joinPart);
		setChatFormat(chatFormat);
		setIncludedServer(includedServer);
		setExcludedServer(excludedServer);
		setSpecificServer(specificServer);
		setSpecificWorld(specificWorld);
		setBlockRadius(blockRadius);
		setTimeBetweenMessages(timeBetweenMessages);
		setTimeBetweenSameMessages(timeBetweenSameMessages);
		setPercentOfSimilarity(percentOfSimilarity);
		setTimeColor(timeColor);
		setPlayernameCustomColor(otherplayernameCustomColor);
		setOtherplayernameCustomColor(otherplayernameCustomColor);
		setSeperatorBetweenPrefix(seperatorBetweenPrefix);
		setSeperatorBetweenSuffix(seperatorBetweenSuffix);
		setMentionSound(mentionSound);
		setServerReplacerMap(serverReplacerMap);
		setServerCommandMap(serverCommandMap);
		setServerHoverMap(serverHoverMap);
		setWorldReplacerMap(worldReplacerMap);
		setWorldCommandMap(worldCommandMap);
		setWorldHoverMap(worldHoverMap);
		setUseColor(useColor);
		setUseItemReplacer(useItemReplacer);
		setUseBookReplacer(useBookReplacer);
		setUseRunCommandReplacer(useRunCommandReplacer);
		setUseSuggestCommandReplacer(useSuggestCommandReplacer);
		setUseWebsiteReplacer(useWebsiteReplacer);
		setUseEmojiReplacer(useEmojiReplacer);
		setUseMentionReplacer(useMentionReplacer);
		setUsePositionReplacer(usePositionReplacer);
	}

	public String getUniqueIdentifierName()
	{
		return uniqueIdentifierName;
	}

	public void setUniqueIdentifierName(String uniqueIdentifierName)
	{
		this.uniqueIdentifierName = uniqueIdentifierName;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getInChatName()
	{
		return inChatName;
	}

	public void setInChatName(String inChatName)
	{
		this.inChatName = inChatName;
	}

	public String getInChatColorMessage()
	{
		return inChatColorMessage;
	}

	public void setInChatColorMessage(String inChatColorMessage)
	{
		this.inChatColorMessage = inChatColorMessage;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public String getJoinPart()
	{
		return joinPart;
	}

	public void setJoinPart(String joinPart)
	{
		this.joinPart = joinPart;
	}

	public String getChatFormat()
	{
		return chatFormat;
	}

	public void setChatFormat(String chatFormat)
	{
		this.chatFormat = chatFormat;
	}

	/**
	 * @return the includedServer
	 */
	public ArrayList<String> getIncludedServer()
	{
		return includedServer;
	}

	/**
	 * @param includedServer the includedServer to set
	 */
	public void setIncludedServer(ArrayList<String> includedServer)
	{
		this.includedServer = includedServer;
	}

	/**
	 * @return the excludedServer
	 */
	public ArrayList<String> getExcludedServer()
	{
		return excludedServer;
	}

	/**
	 * @param excludedServer the excludedServer to set
	 */
	public void setExcludedServer(ArrayList<String> excludedServer)
	{
		this.excludedServer = excludedServer;
	}

	public boolean isSpecificServer()
	{
		return specificServer;
	}

	public void setSpecificServer(boolean specificServer)
	{
		this.specificServer = specificServer;
	}

	public boolean isSpecificWorld()
	{
		return specificWorld;
	}

	public void setSpecificWorld(boolean specificWorld)
	{
		this.specificWorld = specificWorld;
	}

	public int getBlockRadius()
	{
		return blockRadius;
	}

	public void setBlockRadius(int blockRadius)
	{
		this.blockRadius = blockRadius;
	}

	public long getTimeBetweenMessages()
	{
		return timeBetweenMessages;
	}

	public void setTimeBetweenMessages(long timeBetweenMessages)
	{
		this.timeBetweenMessages = timeBetweenMessages;
	}

	public long getTimeBetweenSameMessages()
	{
		return timeBetweenSameMessages;
	}

	public void setTimeBetweenSameMessages(long timeBetweenSameMessages)
	{
		this.timeBetweenSameMessages = timeBetweenSameMessages;
	}

	public double getPercentOfSimilarity()
	{
		return percentOfSimilarity;
	}

	public void setPercentOfSimilarity(double percentOfSimilarity)
	{
		this.percentOfSimilarity = percentOfSimilarity;
	}

	public String getTimeColor()
	{
		return timeColor;
	}

	public void setTimeColor(String timeColor)
	{
		this.timeColor = timeColor;
	}

	public String getPlayernameCustomColor()
	{
		return playernameCustomColor;
	}

	public void setPlayernameCustomColor(String playernameCustomColor)
	{
		this.playernameCustomColor = playernameCustomColor;
	}

	public String getOtherplayernameCustomColor()
	{
		return otherplayernameCustomColor;
	}

	public void setOtherplayernameCustomColor(String otherplayernameCustomColor)
	{
		this.otherplayernameCustomColor = otherplayernameCustomColor;
	}

	public String getSeperatorBetweenPrefix()
	{
		return seperatorBetweenPrefix;
	}

	public void setSeperatorBetweenPrefix(String seperatorBetweenPrefix)
	{
		this.seperatorBetweenPrefix = seperatorBetweenPrefix;
	}

	public String getSeperatorBetweenSuffix()
	{
		return seperatorBetweenSuffix;
	}

	public void setSeperatorBetweenSuffix(String seperatorBetweenSuffix)
	{
		this.seperatorBetweenSuffix = seperatorBetweenSuffix;
	}

	public String getMentionSound()
	{
		return mentionSound;
	}

	public void setMentionSound(String mentionSound)
	{
		this.mentionSound = mentionSound;
	}

	public LinkedHashMap<String, String> getServerReplacerMap()
	{
		return serverReplacerMap;
	}

	public void setServerReplacerMap(LinkedHashMap<String, String> serverReplacerMap)
	{
		this.serverReplacerMap = serverReplacerMap;
	}

	public LinkedHashMap<String, String> getServerCommandMap()
	{
		return serverCommandMap;
	}

	public void setServerCommandMap(LinkedHashMap<String, String> serverCommandMap)
	{
		this.serverCommandMap = serverCommandMap;
	}

	public LinkedHashMap<String, String> getServerHoverMap()
	{
		return serverHoverMap;
	}

	public void setServerHoverMap(LinkedHashMap<String, String> serverHoverMap)
	{
		this.serverHoverMap = serverHoverMap;
	}

	public LinkedHashMap<String, String> getWorldReplacerMap()
	{
		return worldReplacerMap;
	}

	public void setWorldReplacerMap(LinkedHashMap<String, String> worldReplacerMap)
	{
		this.worldReplacerMap = worldReplacerMap;
	}

	public LinkedHashMap<String, String> getWorldCommandMap()
	{
		return worldCommandMap;
	}

	public void setWorldCommandMap(LinkedHashMap<String, String> worldCommandMap)
	{
		this.worldCommandMap = worldCommandMap;
	}

	public LinkedHashMap<String, String> getWorldHoverMap()
	{
		return worldHoverMap;
	}

	public void setWorldHoverMap(LinkedHashMap<String, String> worldHoverMap)
	{
		this.worldHoverMap = worldHoverMap;
	}

	public boolean isUseColor()
	{
		return useColor;
	}

	public void setUseColor(boolean useColor)
	{
		this.useColor = useColor;
	}

	public boolean isUseItemReplacer()
	{
		return useItemReplacer;
	}

	public void setUseItemReplacer(boolean useItemReplacer)
	{
		this.useItemReplacer = useItemReplacer;
	}

	public boolean isUseBookReplacer()
	{
		return useBookReplacer;
	}

	public void setUseBookReplacer(boolean useBookReplacer)
	{
		this.useBookReplacer = useBookReplacer;
	}

	public boolean isUseRunCommandReplacer()
	{
		return useRunCommandReplacer;
	}

	public void setUseRunCommandReplacer(boolean useRunCommandReplacer)
	{
		this.useRunCommandReplacer = useRunCommandReplacer;
	}

	public boolean isUseSuggestCommandReplacer()
	{
		return useSuggestCommandReplacer;
	}

	public void setUseSuggestCommandReplacer(boolean useSuggestCommandReplacer)
	{
		this.useSuggestCommandReplacer = useSuggestCommandReplacer;
	}

	public boolean isUseWebsiteReplacer()
	{
		return useWebsiteReplacer;
	}

	public void setUseWebsiteReplacer(boolean useWebsiteReplacer)
	{
		this.useWebsiteReplacer = useWebsiteReplacer;
	}

	public boolean isUseEmojiReplacer()
	{
		return useEmojiReplacer;
	}

	public void setUseEmojiReplacer(boolean useEmojiReplacer)
	{
		this.useEmojiReplacer = useEmojiReplacer;
	}

	public boolean isUseMentionReplacer()
	{
		return useMentionReplacer;
	}

	public void setUseMentionReplacer(boolean useMentionReplacer)
	{
		this.useMentionReplacer = useMentionReplacer;
	}

	public boolean isUsePositionReplacer()
	{
		return usePositionReplacer;
	}

	public void setUsePositionReplacer(boolean usePositionReplacer)
	{
		this.usePositionReplacer = usePositionReplacer;
	}
}
