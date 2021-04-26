package main.java.me.avankziar.scc.objects.chat;

import main.java.me.avankziar.scc.handlers.MatchApi;

public class ChatTitle
{
	public String replaceWithColorCode(String name)
	{
		/*
		 * If multiple ChatColors are applied.
		 * For example: &4@0-5;&c6-17
		 */
		if(getInChatColorCode().contains(";"))
		{
			String s = "";
			String[] function = getInChatColorCode().split(";");
			for(String f : function)
			{
				if(!f.contains("@"))
				{
					continue;
				}
				String[] intern = f.split("@");
				if(!intern[1].contains("-"))
				{
					continue;
				}
				String[] values = intern[1].split("-");
				if(!MatchApi.isInteger(values[0]) || !MatchApi.isInteger(values[1]))
				{
					continue;
				}
				int begin = Integer.parseInt(values[0]);
				if(begin < 0)
				{
					begin = 0;
				}
				if(begin > name.length())
				{
					begin = name.length();
				}
				int end = Integer.parseInt(values[1]);
				if(end < 0) 
				{
					end = 0;
				}
				if(end > name.length())
				{
					end = name.length();
				}
				s += intern[0]+name.substring(begin, end);
			}
			return s;
		} else
		/*
		 * If only one ChatColor are applied.
		 */
		{
			return getInChatColorCode()+"&r"+name;
		}
	}
	/**
	 * The unique identifier name.
	 */
	private String uniqueIdentifierName;
	/**
	 * if true, it is a prefix, if false, if suffix.
	 */
	private boolean isPrefix;
	/**
	 * The in chat name with color code & or &# 
	 */
	private String inChatName;
	/**
	 * The chattitle colorcode
	 */
	private String inChatColorCode;
	/**
	 * The hover of the ChatTitle;
	 */
	private String hover;
	/**
	 * The permission of this chattitle.
	 */
	private String permission;
	/**
	 * The weight of this chattitle. The more weight, the more important is the chattitle. And the more this is further in front.
	 */
	private int weight;
	
	public ChatTitle(String uniqueIdentifierName, boolean isPrefix, String inChatName, String inChatColorCode,
			String hover, String permission, int weight)
	{
		setUniqueIdentifierName(uniqueIdentifierName);
		setPrefix(isPrefix);
		setInChatName(inChatName);
		setInChatColorCode(inChatColorCode);
		setHover(hover);
		setPermission(permission);
		setWeight(weight);
	}

	public boolean isPrefix()
	{
		return isPrefix;
	}

	public void setPrefix(boolean isPrefix)
	{
		this.isPrefix = isPrefix;
	}

	public String getUniqueIdentifierName()
	{
		return uniqueIdentifierName;
	}

	public void setUniqueIdentifierName(String uniqueIdentifierName)
	{
		this.uniqueIdentifierName = uniqueIdentifierName;
	}

	public String getInChatName()
	{
		return inChatName;
	}

	public void setInChatName(String inChatName)
	{
		this.inChatName = inChatName;
	}

	public String getInChatColorCode()
	{
		return inChatColorCode;
	}

	public void setInChatColorCode(String inChatColorCode)
	{
		this.inChatColorCode = inChatColorCode;
	}

	public String getHover()
	{
		return hover;
	}

	public void setHover(String hover)
	{
		this.hover = hover;
	}

	public String getPermission()
	{
		return permission;
	}

	public void setPermission(String permission)
	{
		this.permission = permission;
	}

	public int getWeight()
	{
		return weight;
	}

	public void setWeight(int weight)
	{
		this.weight = weight;
	}

}
