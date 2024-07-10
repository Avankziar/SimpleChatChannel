package main.java.me.avankziar.scc.bungee.objects;

import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;

public class ChatUserHandler
{
	public static ChatUser getChatUser(UUID uuid)
	{
		return (ChatUser) SCC.getPlugin().getMysqlHandler().getData(MysqlType.CHATUSER, "`player_uuid` = ?", uuid.toString());
	}
	
	public static ChatUser getChatUser(String name)
	{
		return (ChatUser) SCC.getPlugin().getMysqlHandler().getData(MysqlType.CHATUSER, "`player_name` = ?", name);
	}

}
