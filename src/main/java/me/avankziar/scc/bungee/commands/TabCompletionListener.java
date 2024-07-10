package main.java.me.avankziar.scc.bungee.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabCompletionListener implements Listener
{	
	private SCC plugin;
	
	public TabCompletionListener(SCC plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onTabs(TabCompleteEvent event)
	{
		if(!(event.getSender() instanceof ProxiedPlayer))
		{
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String tab = event.getCursor();
		try
		{
			tab = tab.substring(1);
		} catch(ArrayIndexOutOfBoundsException e){}
		String[] tabArgs = tab.split(" ");
		if(tabArgs.length < 0)
		{
			return;
		}
		String cmd = tabArgs[0];
		CommandConstructor cc = plugin.getCommandFromPath(cmd);
		if(cc == null)
		{
			cc = plugin.getCommandFromPath(cmd);
		}
		if(cc == null)
		{
			return;
		}
		//Ab hier finden für Bungeecord das statt, um es in der art aufzubauen wie der TabC auf spigot
		String tabCount = event.getCursor().substring(1+cmd.length());
		int countSpace = 0;
		for(int i = 0; i < tabCount.length(); i++)
		{
			if(tabCount.charAt(i) == ' ')
			{
				countSpace++; //Dazu zählt man die Leerzeichen.
			}
		}
		String[] args = Arrays.copyOfRange(tabArgs, 1, tabArgs.length); //Löscht den cmd aus dem Array
		if(countSpace > args.length) //Sind mehr leerzeichen als Values in dem Array vorhanden, fügt man eines hinzu.
		{
			args = AddToStringArray(args, "");
		}
		
		/*
		 * ------
		 * Ab hier wie Spigot
		 */
		
		int length = args.length-1;
		ArrayList<ArgumentConstructor> aclist = cc.subcommands;
		ArrayList<String> OneArgumentBeforeList = new ArrayList<>();
		ArgumentConstructor lastAc = null;
		for(ArgumentConstructor ac : aclist)
		{
			OneArgumentBeforeList.add(ac.getName());
		}
		boolean isBreak = false;
		for(int i = 0; i <= length; i++)
		{
			//ACHTUNG! Ausnahmefall msg & re !!
			if((cmd.equalsIgnoreCase(SCC.baseCommandIVName)
					|| cmd.equalsIgnoreCase(SCC.baseCommandVName)) && length == 0)
			{
				if(cmd.equalsIgnoreCase(SCC.baseCommandVName)
						&& SCC.rePlayers.containsKey(player.getUniqueId().toString()))
				{
					event.getSuggestions().clear();
					event.getSuggestions().addAll(SCC.rePlayers.get(player.getUniqueId().toString()));
				} else
				{
					event.getSuggestions().clear();
					event.getSuggestions().addAll(getReturnTabList(SCC.onlinePlayers, args[length]));
					return;
				}
			}
			//ACHTUNG! Ausnahmefall!!
			
			isBreak = false;
			for(int j = 0; j <= aclist.size()-1; j++)
			{
				ArgumentConstructor ac = aclist.get(j);				
				/*
				 * Wenn das aktuelle Argument leer ist, so loop durch die aclist.
				 */
				if(args[i].isEmpty())
				{
					event.getSuggestions().clear();
					event.getSuggestions().addAll(listIfArgumentIsEmpty(aclist, player));
					return;
				} else
				/*
				 * Wenn das aktuelle Argument NICHT leer ist, so loop durch die aclist und checke ob das Argument mit "xx" anfängt.
				 */
				{
					int c = countHowMuchAreStartsWithIgnoreCase(aclist, args[i]);
					if(c > 1)
					{
						/*
						 * Wenn mehr als 1 Argument mit dem Chateintrag startet, so liefere eine Liste mit allen diesen zurück.
						 */
						List<String> list = listIfArgumentIsnotEmpty(aclist, args[i], player);
						event.getSuggestions().clear();
						event.getSuggestions().addAll(list);
						return;
					}
					if(ac.getName().toLowerCase().startsWith(args[i].toLowerCase()))
					{
						if(ac.getName().length() > args[i].length())
						{
							/*
							 * Wenn das Argument noch nicht vollständig ausgeschrieben ist, so return das.
							 */
							ArrayList<String> list = new ArrayList<>();
							list.add(ac.getName());
							event.getSuggestions().clear();
							event.getSuggestions().addAll(list);
							return;
						}
						/*
						 * Das Argument startet mit dem Argumentenname. aclist mit den Subargumenten vom Argument setzten.
						 * Sowie den innern Loop brechen.
						 */
						aclist = ac.subargument;
						isBreak = true;
						lastAc = ac;
						break;
					}
					if(j == aclist.size()-1)
					{
						/*
						 * Wenn keins der Argumente an der spezifischen Position gepasst hat, abbrechen. Und leere aclist setzten.
						 */
						aclist = new ArrayList<>();
					}
				}
			}
			if(!isBreak)
			{
				if(lastAc != null)
				{
					List<String> list = getReturnTabList(lastAc.tabList.get(length), args[length]);
					event.getSuggestions().clear();
					event.getSuggestions().addAll(list);
					return;
					//Return leer, wenn die Tabliste nicht existiert! Aka ein halbes break;
				}
				if(i == length || aclist.isEmpty()) //Wenn das ende erreicht ist oder die aclist vorher leer gesetzt worden ist
				{
					break;
				}
			}
		}
		return;
	}
	
	private List<String> getReturnTabList(ArrayList<String> tabList, String argsi)
	{
		ArrayList<String> list = new ArrayList<>();
		if(tabList != null && argsi != null)
		{
			for(String s : tabList)
			{
				if(s.startsWith(argsi))
				{
					list.add(s);
				}
			}
		}
		Collections.sort(list);
		return list;
	}
	
	private List<String> listIfArgumentIsEmpty(ArrayList<ArgumentConstructor> subarg, ProxiedPlayer player)
	{
		List<String> returnlist = new ArrayList<String>();
		for(ArgumentConstructor ac : subarg)
		{
			if(ac != null)
			{
				if(player.hasPermission(ac.getPermission()))
				{
					returnlist.add(ac.getName());
				}
			}
		}
		return returnlist;
	}
	
	private List<String> listIfArgumentIsnotEmpty(ArrayList<ArgumentConstructor> subarg, String arg, ProxiedPlayer player)
	{
		List<String> returnlist = new ArrayList<String>();
		for(ArgumentConstructor ac : subarg)
		{
			if(ac != null)
			{
				String acn = ac.getName();
				if(acn.toLowerCase().startsWith(arg.toLowerCase()))
				{
					if(player.hasPermission(ac.getPermission()))
					{
						if(!returnlist.contains(acn))
						{
							returnlist.add(ac.getName());
						}
					}
				}
			}
		}
		return returnlist;
	}
	
	private int countHowMuchAreStartsWithIgnoreCase(ArrayList<ArgumentConstructor> subarg, String arg)
	{
		int i = 0;
		for(ArgumentConstructor ac : subarg)
		{
			if(ac.getName().toLowerCase().startsWith(arg.toLowerCase()))
			{
				i++;
			}
		}
		return i;
	}
	
	public String[] AddToStringArray(String[] oldArray, String newString)
	{
	    String[] newArray = Arrays.copyOf(oldArray, oldArray.length+1);
	    newArray[oldArray.length] = newString;
	    return newArray;
	}
}
