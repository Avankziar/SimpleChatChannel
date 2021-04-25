package main.java.me.avankziar.simplechatchannels.handlers;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import main.java.me.avankziar.simplechatchannels.objects.ItemJson;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.objects.UsedChannel;

public class ConvertHandler
{
	public static ArrayList<ChatUser> convertListI(ArrayList<?> list)
	{
		ArrayList<ChatUser> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof ChatUser)
			{
				el.add((ChatUser) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<IgnoreObject> convertListII(ArrayList<?> list)
	{
		ArrayList<IgnoreObject> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof IgnoreObject)
			{
				el.add((IgnoreObject) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<PermanentChannel> convertListIII(ArrayList<?> list)
	{
		ArrayList<PermanentChannel> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof PermanentChannel)
			{
				el.add((PermanentChannel) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<ItemJson> convertListIV(ArrayList<?> list)
	{
		ArrayList<ItemJson> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof ItemJson)
			{
				el.add((ItemJson) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
	
	public static ArrayList<UsedChannel> convertListV(ArrayList<?> list)
	{
		ArrayList<UsedChannel> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof UsedChannel)
			{
				el.add((UsedChannel) o);
			} else
			{
				return null;
			}
		}
		return el;
	}
}
