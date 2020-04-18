package main.java.me.avankziar.simplechatchannels.spigot.interfaces;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class TemporaryChannel 
{
	private String name;
	private Player creator;
	private ArrayList<Player> members = new ArrayList<Player>();
	private static ArrayList<TemporaryChannel> customchannel = new ArrayList<TemporaryChannel>();
	private String password;
	private ArrayList<Player> banned = new ArrayList<Player>();
	
	public TemporaryChannel(String name, Player creator, ArrayList<Player> members, String password,
			ArrayList<Player> banned)
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

	public Player getCreator() 
	{
		return creator;
	}

	public void setCreator(Player creator) 
	{
		this.creator = creator;
	}

	public ArrayList<Player> getMembers() 
	{
		return members;
	}

	public void setMembers(ArrayList<Player> members) 
	{
		this.members = members;
	}
	
	public void addMembers(Player player)
	{
		if(!members.contains(player))
		{
			members.add(player);
		}
	}
	
	public void removeMembers(Player player)
	{
		if(members.contains(player))
		{
			members.remove(player);
		}
	}
	
	public static TemporaryChannel getCustomChannel(Player player)
	{
		TemporaryChannel c = null;
		for (TemporaryChannel cc : customchannel)
		{
			for(Player p : cc.getMembers())
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

	public void setPassword(String password) {
		this.password = password;
	}
	
	public ArrayList<Player> getBanned() 
	{
		return banned;
	}

	public void setBanned(ArrayList<Player> banned) 
	{
		this.banned = banned;
	}
	public void addBanned(Player player)
	{
		if(!banned.contains(player))
		{
			banned.add(player);
		}
	}
	
	public void removeBanned(Player player)
	{
		if(banned.contains(player))
		{
			banned.remove(player);
		}
	}
}
