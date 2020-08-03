package main.java.me.avankziar.simplechatchannels.objects;

import java.util.ArrayList;

public class PermanentChannel
{
	private int id;
	private String name;
	private String creator;
	private ArrayList<String> vice = new ArrayList<String>();
	private ArrayList<String> members = new ArrayList<String>();
	private static ArrayList<PermanentChannel> permanentChannel = new ArrayList<PermanentChannel>();
	private String password;
	private String symbolExtra;
	private String nameColor;
	private String chatColor;
	private ArrayList<String> banned = new ArrayList<String>();
	
	public PermanentChannel(int id, String name, String creator, ArrayList<String> vice,
			ArrayList<String> members, String password, ArrayList<String> banned,
			String symbolextra, String namecolor, String chatcolor)
	{
		setId(id);
		setName(name);
		setCreator(creator);
		setVice(vice);
		setMembers(members);
		setPassword(password);
		setBanned(banned);
		setSymbolExtra(symbolextra);
		setNameColor(namecolor);
		setChatColor(chatcolor);
	}
	
	public static PermanentChannel getChannelFromPlayer(String playeruuid)
	{
		PermanentChannel c = null;
		for (PermanentChannel pc : permanentChannel)
		{
			for(String p : pc.getMembers())
			{
				if(p.equals(playeruuid))
				{
					c = pc;
					break;
				}
			}
		}
		return c;
	}
	
	public static PermanentChannel getChannelFromName(String name)
	{
		PermanentChannel c = null;
		for (PermanentChannel cc : permanentChannel)
		{
			if(cc.getName().equals(name))
			{
				c = cc;
				break;
			}
		}
		return c;
	}
	
	public static PermanentChannel getChannelFromSymbol(String symbol)
	{
		PermanentChannel c = null;
		for (PermanentChannel cc : permanentChannel)
		{
			if(cc.getSymbolExtra().equals(symbol))
			{
				c = cc;
				break;
			}
		}
		return c;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public ArrayList<String> getVice()
	{
		return vice;
	}

	public void setVice(ArrayList<String> vice)
	{
		this.vice = vice;
	}

	public ArrayList<String> getMembers()
	{
		return members;
	}
	
	public void addVice(String player)
	{
		if(!vice.contains(player))
		{
			vice.add(player);
		}
	}
	
	public void removeVice(String player)
	{
		if(vice.contains(player))
		{
			vice.remove(player);
		}
	}

	public void setMembers(ArrayList<String> members)
	{
		this.members = members;
	}
	
	public void addMembers(String player)
	{
		if(!members.contains(player))
		{
			members.add(player);
		}
	}
	
	public void removeMembers(String player)
	{
		if(members.contains(player))
		{
			members.remove(player);
		}
	}

	public static ArrayList<PermanentChannel> getPermanentChannel()
	{
		return permanentChannel;
	}

	public static void setPermanentChannel(ArrayList<PermanentChannel> permanentChannel)
	{
		PermanentChannel.permanentChannel = permanentChannel;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getSymbolExtra()
	{
		return symbolExtra;
	}

	public void setSymbolExtra(String symbolExtra)
	{
		this.symbolExtra = symbolExtra;
	}

	public String getNameColor()
	{
		return nameColor;
	}

	public void setNameColor(String nameColor)
	{
		this.nameColor = nameColor;
	}

	public String getChatColor()
	{
		return chatColor;
	}

	public void setChatColor(String chatColor)
	{
		this.chatColor = chatColor;
	}

	public ArrayList<String> getBanned()
	{
		return banned;
	}

	public void setBanned(ArrayList<String> banned)
	{
		this.banned = banned;
	}
	
	public void addBanned(String player)
	{
		if(!banned.contains(player))
		{
			banned.add(player);
		}
	}
	
	public void removeBanned(String player)
	{
		if(banned.contains(player))
		{
			banned.remove(player);
		}
	}
	
	public static void addCustomChannel(PermanentChannel cc)
	{
		PermanentChannel r = null;
		for(PermanentChannel pc : permanentChannel)
		{
			if(pc.getId() == cc.getId())
			{
				r = pc;
			}
		}
		if(r != null)
		{
			permanentChannel.remove(r);
		}
		permanentChannel.add(cc);
	}
	
	public static void removeCustomChannel(PermanentChannel cc)
	{
		PermanentChannel r = null;
		for(PermanentChannel pc : permanentChannel)
		{
			if(pc.getId() == cc.getId())
			{
				r = pc;
			}
		}
		permanentChannel.remove(r);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
