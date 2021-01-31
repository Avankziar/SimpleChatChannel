package main.java.me.avankziar.simplechatchannels.objects;

import java.util.ArrayList;

public class ChatUser
{
	private String uuid;
	private String name;
	private boolean canChat;
	private long muteTime;
	private boolean channelGlobal;
	private boolean channelTrade;
	private boolean channelAuction;
	private boolean channelSupport;
	private boolean channelLocal;
	private boolean channelWorld;
	private boolean channelTeam;
	private boolean channelAdmin;
	private boolean channelGroup;
	private boolean channelEvent;
	private boolean channelPrivateMessage;
	private boolean channelTemporary;
	private boolean channelPermanent;
	private boolean optionSpy;
	private boolean optionJoinMessage;
	private static ArrayList<ChatUser> allChatUser = new ArrayList<>();
	
	public ChatUser(String uuid, String name, boolean canChat, long muteTime,
			boolean channelGlobal, boolean channelTrade, boolean channelAuction, boolean channelSupport,
			boolean channelLocal, boolean channelWorld, boolean channelTeam, boolean channelAdmin,
			boolean channelGroup, boolean channelEvent, boolean channelPrivateMessage,
			boolean channelTemporary, boolean channelPermanent, boolean optionSpy, boolean optionJoinMessage)
	{
		setUUID(uuid);
		setName(name);
		setCanChat(canChat);
		setMuteTime(muteTime);
		setChannelGlobal(channelGlobal);
		setChannelTrade(channelTrade);
		setChannelAuction(channelAuction);
		setChannelSupport(channelSupport);
		setChannelLocal(channelLocal);
		setChannelWorld(channelWorld);
		setChannelTeam(channelTeam);
		setChannelAdmin(channelAdmin);
		setChannelGroup(channelGroup);
		setChannelEvent(channelEvent);
		setChannelPrivateMessage(channelPrivateMessage);
		setChannelTemporary(channelTemporary);
		setChannelPermanent(channelPermanent);
		setOptionSpy(optionSpy);
		setOptionJoinMessage(optionJoinMessage);
		
	}

	public String getUUID()
	{
		return uuid;
	}

	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isCanChat()
	{
		return canChat;
	}

	public void setCanChat(boolean canChat)
	{
		this.canChat = canChat;
	}

	public long getMuteTime()
	{
		return muteTime;
	}

	public void setMuteTime(long muteTime)
	{
		this.muteTime = muteTime;
	}

	public boolean isChannelGlobal()
	{
		return channelGlobal;
	}

	public void setChannelGlobal(boolean channelGlobal)
	{
		this.channelGlobal = channelGlobal;
	}

	public boolean isChannelTrade()
	{
		return channelTrade;
	}

	public void setChannelTrade(boolean channelTrade)
	{
		this.channelTrade = channelTrade;
	}

	public boolean isChannelAuction()
	{
		return channelAuction;
	}

	public void setChannelAuction(boolean channelAuction)
	{
		this.channelAuction = channelAuction;
	}

	public boolean isChannelSupport()
	{
		return channelSupport;
	}

	public void setChannelSupport(boolean channelSupport)
	{
		this.channelSupport = channelSupport;
	}

	public boolean isChannelLocal()
	{
		return channelLocal;
	}

	public void setChannelLocal(boolean channelLocal)
	{
		this.channelLocal = channelLocal;
	}

	public boolean isChannelWorld()
	{
		return channelWorld;
	}

	public void setChannelWorld(boolean channelWorld)
	{
		this.channelWorld = channelWorld;
	}

	public boolean isChannelTeam()
	{
		return channelTeam;
	}

	public void setChannelTeam(boolean channelTeam)
	{
		this.channelTeam = channelTeam;
	}

	public boolean isChannelAdmin()
	{
		return channelAdmin;
	}

	public void setChannelAdmin(boolean channelAdmin)
	{
		this.channelAdmin = channelAdmin;
	}

	public boolean isChannelGroup()
	{
		return channelGroup;
	}

	public void setChannelGroup(boolean channelGroup)
	{
		this.channelGroup = channelGroup;
	}

	public boolean isChannelEvent()
	{
		return channelEvent;
	}

	public void setChannelEvent(boolean channelEvent)
	{
		this.channelEvent = channelEvent;
	}

	public boolean isChannelPrivateMessage()
	{
		return channelPrivateMessage;
	}

	public void setChannelPrivateMessage(boolean channelPrivateMessage)
	{
		this.channelPrivateMessage = channelPrivateMessage;
	}

	public boolean isChannelTemporary()
	{
		return channelTemporary;
	}

	public void setChannelTemporary(boolean channelTemporary)
	{
		this.channelTemporary = channelTemporary;
	}

	public boolean isChannelPermanent()
	{
		return channelPermanent;
	}

	public void setChannelPermanent(boolean channelPermanent)
	{
		this.channelPermanent = channelPermanent;
	}

	public boolean isOptionSpy()
	{
		return optionSpy;
	}

	public void setOptionSpy(boolean optionSpy)
	{
		this.optionSpy = optionSpy;
	}

	public boolean isOptionJoinMessage()
	{
		return optionJoinMessage;
	}

	public void setOptionJoinMessage(boolean optionJoinMessage)
	{
		this.optionJoinMessage = optionJoinMessage;
	}

	public static ArrayList<ChatUser> getAllChatUser()
	{
		return allChatUser;
	}

	public static void setAllChatUser(ArrayList<ChatUser> allChatUser)
	{
		ChatUser.allChatUser = allChatUser;
	}
	
	public static void addChatUser(ChatUser user)
	{
		ChatUser cu = null;
		for(ChatUser chatuser : ChatUser.getAllChatUser())
		{
			if(chatuser.getUUID().equalsIgnoreCase(user.getUUID()))
			{
				cu = chatuser;
				break;
			}
		}
		if(cu != null)
		{
			ChatUser.getAllChatUser().remove(cu);
		}
		ChatUser.getAllChatUser().add(user);
	}
	
	public static void removeChatUser(ChatUser user)
	{
		ChatUser cu = null;
		for(ChatUser chatuser : ChatUser.getAllChatUser())
		{
			if(chatuser.getUUID().equalsIgnoreCase(user.getUUID()))
			{
				cu = chatuser;
				break;
			}
		}
		if(cu != null)
		{
			ChatUser.getAllChatUser().remove(cu);
		}
	}
	
	public static boolean getBoolean(ChatUser chatuser, String mysql_channel)
	{
		if(mysql_channel.toLowerCase().contains("global"))
		{
			return chatuser.isChannelGlobal();
		} else if(mysql_channel.toLowerCase().contains("trade"))
		{
			return chatuser.isChannelTrade();
		} else if(mysql_channel.toLowerCase().contains("auction"))
		{
			return chatuser.isChannelAuction();
		} else if(mysql_channel.toLowerCase().contains("support"))
		{
			return chatuser.isChannelSupport();
		} else if(mysql_channel.contains("local"))
		{
			return chatuser.isChannelLocal();
		} else if(mysql_channel.toLowerCase().contains("world"))
		{
			return chatuser.isChannelWorld();
		} else if(mysql_channel.toLowerCase().contains("team"))
		{
			return chatuser.isChannelTeam();
		} else if(mysql_channel.toLowerCase().contains("admin"))
		{
			return chatuser.isChannelAdmin();
		} else if(mysql_channel.toLowerCase().contains("group"))
		{
			return chatuser.isChannelGroup();
		} else if(mysql_channel.toLowerCase().contains("pm"))
		{
			return chatuser.isChannelPrivateMessage();
		} else if(mysql_channel.toLowerCase().contains("temp"))
		{
			return chatuser.isChannelTemporary();
		} else if(mysql_channel.toLowerCase().contains("perma"))
		{
			return chatuser.isChannelPermanent();
		} else if(mysql_channel.toLowerCase().contains("event"))
		{
			return chatuser.isChannelEvent();
		}
		return false;
	}
	
	public static ChatUser updateBoolean(ChatUser chatuser, String mysql_channel, boolean boo)
	{
		if(mysql_channel.toLowerCase().contains("global"))
		{
			chatuser.setChannelGlobal(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("trade"))
		{
			chatuser.setChannelTrade(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("auction"))
		{
			chatuser.setChannelAuction(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("support"))
		{
			chatuser.setChannelSupport(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("local"))
		{
			chatuser.setChannelLocal(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("world"))
		{
			chatuser.setChannelWorld(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("team"))
		{
			chatuser.setChannelTeam(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("admin"))
		{
			chatuser.setChannelAdmin(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("group"))
		{
			chatuser.setChannelGroup(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("pm"))
		{
			chatuser.setChannelPrivateMessage(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("temp"))
		{
			chatuser.setChannelTemporary(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("perma"))
		{
			chatuser.setChannelPermanent(boo);
			return chatuser;
		} else if(mysql_channel.toLowerCase().contains("event"))
		{
			chatuser.setChannelEvent(boo);
			return chatuser;
		}
		return chatuser;
	}
}
