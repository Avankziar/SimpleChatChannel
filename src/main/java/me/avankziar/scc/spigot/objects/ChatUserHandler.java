package main.java.me.avankziar.scc.spigot.objects;

import java.util.LinkedHashMap;
import java.util.UUID;

import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.chat.UsedChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;

public class ChatUserHandler
{
	public static ChatUser getChatUser(UUID uuid)
	{
		return (ChatUser) SimpleChatChannels.getPlugin().getMysqlHandler().getData(Type.CHATUSER, "`player_uuid` = ?", uuid.toString());
	}
	
	public static ChatUser getChatUser(String name)
	{
		return (ChatUser) SimpleChatChannels.getPlugin().getMysqlHandler().getData(Type.CHATUSER, "`player_name` = ?", name);
	}
	
	public static LinkedHashMap<String, UsedChannel> getUsedChannels(UUID uuid)
	{
		return Utility.playerUsedChannels.get(uuid.toString());
	}
}
