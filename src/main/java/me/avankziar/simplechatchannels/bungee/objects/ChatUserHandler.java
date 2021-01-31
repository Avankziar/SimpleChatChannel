package main.java.me.avankziar.simplechatchannels.bungee.objects;

import java.util.UUID;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler.Type;

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
