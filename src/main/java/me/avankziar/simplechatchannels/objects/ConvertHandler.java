package main.java.me.avankziar.simplechatchannels.objects;

import java.util.ArrayList;

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
}
