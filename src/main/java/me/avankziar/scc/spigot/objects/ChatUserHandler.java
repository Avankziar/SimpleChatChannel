package main.java.me.avankziar.scc.spigot.objects;

import java.util.LinkedHashMap;
import java.util.UUID;

import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;

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
	
	public static LinkedHashMap<String, UsedChannel> getUsedChannels(UUID uuid)
	{
		return Utility.playerUsedChannels.get(uuid.toString());
	}
}
