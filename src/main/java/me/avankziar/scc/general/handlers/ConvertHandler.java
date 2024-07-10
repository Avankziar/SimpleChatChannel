package main.java.me.avankziar.scc.general.handlers;

import java.util.ArrayList;

import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.general.objects.ItemJson;
import main.java.me.avankziar.scc.general.objects.Mail;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.general.objects.UsedChannel;

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
				return el;
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
				return el;
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
				return el;
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
				return el;
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
				return el;
			}
		}
		return el;
	}
	
	public static ArrayList<Mail> convertListVI(ArrayList<?> list)
	{
		ArrayList<Mail> el = new ArrayList<>();
		for(Object o : list)
		{
			if(o instanceof Mail)
			{
				el.add((Mail) o);
			} else
			{
				return el;
			}
		}
		return el;
	}
}
