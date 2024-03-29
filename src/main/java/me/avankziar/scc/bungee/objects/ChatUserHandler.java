package main.java.me.avankziar.scc.bungee.objects;

import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.objects.ChatUser;

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

}
