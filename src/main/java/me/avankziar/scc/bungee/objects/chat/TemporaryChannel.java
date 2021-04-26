package main.java.me.avankziar.scc.bungee.objects.chat;

import java.util.ArrayList;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TemporaryChannel 
{
	private String name;
	private ProxiedPlayer creator;
	private ArrayList<ProxiedPlayer> members = new ArrayList<ProxiedPlayer>();
	private static ArrayList<TemporaryChannel> customchannel = new ArrayList<TemporaryChannel>();
	private String password;
	private ArrayList<ProxiedPlayer> banned = new ArrayList<ProxiedPlayer>();
	
	public TemporaryChannel(String name, ProxiedPlayer creator, ArrayList<ProxiedPlayer> members, String password,
			ArrayList<ProxiedPlayer> banned)
	{
		setName(name);
		setCreator(creator);
		setMembers(members);
		setPassword(password);
		setBanned(banned);
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public ProxiedPlayer getCreator() 
	{
		return creator;
	}

	public void setCreator(ProxiedPlayer creator) 
	{
		this.creator = creator;
	}

	public ArrayList<ProxiedPlayer> getMembers() 
	{
		return members;
	}

	public void setMembers(ArrayList<ProxiedPlayer> members) 
	{
		this.members = members;
	}
	
	public void addMembers(ProxiedPlayer player)
	{
		if(!members.contains(player))
		{
			members.add(player);
		}
	}
	
	public void removeMembers(ProxiedPlayer player)
	{
		if(members.contains(player))
		{
			members.remove(player);
		}
	}
	
	public static ArrayList<TemporaryChannel> getCustomChannel()
	{
		return customchannel;
	}
	
	public static TemporaryChannel getCustomChannel(ProxiedPlayer player)
	{
		TemporaryChannel c = null;
		for (TemporaryChannel cc : customchannel)
		{
			for(ProxiedPlayer p : cc.getMembers())
			{
				if(p.getName().equals(player.getName()))
				{
					c = cc;
					break;
				}
			}
		}
		return c;
	}
	
	public static TemporaryChannel getCustomChannel(String name)
	{
		TemporaryChannel c = null;
		for (TemporaryChannel cc : customchannel)
		{
			if(cc.getName().equalsIgnoreCase(name))
			{
				c = cc;
				break;
			}
		}
		return c;
	}
	
	public static void addCustomChannel(TemporaryChannel cc)
	{
		customchannel.add(cc);
	}
	
	public static void removeCustomChannel(TemporaryChannel cc)
	{
		customchannel.remove(cc);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
	}

	public ArrayList<ProxiedPlayer> getBanned() 
	{
		return banned;
	}

	public void setBanned(ArrayList<ProxiedPlayer> banned) 
	{
		this.banned = banned;
	}
	public void addBanned(ProxiedPlayer player)
	{
		if(!banned.contains(player))
		{
			banned.add(player);
		}
	}
	
	public void removeBanned(ProxiedPlayer player)
	{
		if(banned.contains(player))
		{
			banned.remove(player);
		}
	}
}
