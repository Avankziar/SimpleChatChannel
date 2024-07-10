package main.java.me.avankziar.scc.velocity.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.velocity.SCC;

public class TabCompletion
{	
	private SCC plugin;
	
	public TabCompletion()
	{
		this.plugin = SCC.getPlugin();
	}
	
	public List<String> getTabs(CommandSource source, String cmd, String[] args)
	{
		if(!(source instanceof Player))
		{
			return List.of();
		}
		Player player = (Player) source;
		
		if(args.length < 0)
		{
			return List.of();
		}
		CommandConstructor cc = plugin.getCommandFromPath(cmd);
		if(cc == null)
		{
			cc = plugin.getCommandFromPath(cmd);
		}
		if(cc == null)
		{
			return List.of();
		}
		
		/*String cursor = cmd + " "+ String.join(" ", arg);
		String tabCount = cursor.substring(1+cmd.length());
		int countSpace = 0;
		for(int i = 0; i < tabCount.length(); i++)
		{
			if(tabCount.charAt(i) == ' ')
			{
				countSpace++; //Dazu zählt man die Leerzeichen.
			}
		}
		String[] args = arg;//Arrays.copyOfRange(tabArgs, 1, tabArgs.length); //Löscht den cmd aus dem Array
		if(countSpace > args.length) //Sind mehr leerzeichen als Values in dem Array vorhanden, fügt man eines hinzu.
		{
			args = AddToStringArray(args, "");
		}*/
		
		ArrayList<String> reList = new ArrayList<>();
		
		int length = args.length-1;
		ArrayList<ArgumentConstructor> aclist = cc.subcommands;
		ArrayList<String> OneArgumentBeforeList = new ArrayList<>();
		ArgumentConstructor lastAc = null;
		for(ArgumentConstructor ac : aclist)
		{
			OneArgumentBeforeList.add(ac.getName());
		}
		boolean isBreak = false;
		if(length < 0)
		{
			/*
			 * Wenn das aktuelle Argument leer ist, so loop durch die aclist.
			 * Ausnahme wenn es /msg oder /re ist
			 */
			if(cmd.equalsIgnoreCase(SCC.baseCommandIVName)
					|| cmd.equalsIgnoreCase(SCC.baseCommandVName))
			{
				if(cmd.equalsIgnoreCase(SCC.baseCommandVName)
						&& SCC.rePlayers.containsKey(player.getUniqueId().toString()))
				{
					reList.addAll(SCC.rePlayers.get(player.getUniqueId().toString()));
				} else
				{
					return getReturnTabList(SCC.onlinePlayers, args[length]);
				}
			}
			return listIfArgumentIsEmpty(aclist, player);
		}
		for(int i = 0; i <= length; i++)
		{
			//ACHTUNG! Ausnahmefall msg & re !!
			if((cmd.equalsIgnoreCase(SCC.baseCommandIVName)
					|| cmd.equalsIgnoreCase(SCC.baseCommandVName)) && length == 0)
			{
				if(cmd.equalsIgnoreCase(SCC.baseCommandVName)
						&& SCC.rePlayers.containsKey(player.getUniqueId().toString()))
				{
					reList.addAll(SCC.rePlayers.get(player.getUniqueId().toString()));
				} else
				{
					return getReturnTabList(SCC.onlinePlayers, args[length]);
				}
			}
			//ACHTUNG! Ausnahmefall!!
			
			isBreak = false;
			for(int j = 0; j <= aclist.size()-1; j++)
			{
				ArgumentConstructor ac = aclist.get(j);				
				/*
				 * Wenn das aktuelle Argument NICHT leer ist, so loop durch die aclist und checke ob das Argument mit "xx" anfängt.
				 */
				ArrayList<ArgumentConstructor> c = countHowMuchAreStartsWithIgnoreCase(aclist, args[i]);
				if(c.size() > 1)
				{
					/*
					 * Wenn mehr als 1 Argument mit dem Chateintrag startet, so liefere eine Liste mit allen diesen zurück.
					 */
					ArrayList<String> notPassed = new ArrayList<>();
					for(String s : listIfArgumentIsnotEmpty(c, args[i], player))
					{
						if(s.equalsIgnoreCase(args[i]))
						{
							if(i+1 <= length)
							{
								/*
								 * Das Argument startet mit dem Argumentenname. aclist mit den Subargumenten vom Argument setzten.
								 * Sowie den innern Loop brechen.
								 */
								if(ac.subargument.size() == 0)
								{
									/*
									 * Keine Subargumente sind vorhanden, returne die tablist
									 */
									return getReturnTabList(ac.tabList.get(length), args[length]);
								}
								aclist = ac.subargument;
								isBreak = true;
								lastAc = ac;
								break;
							} else
							{
								/*
								 * Das Argument passt perfekt mit dem angegeben zusammen, nun nehme nur dieses!
								 */
								return new ArrayList<String>(Arrays.asList(args[i]));
							}
						} else
						{
							notPassed.add(s);
						}
					}
					if(notPassed.size() > 0)
					{
						return notPassed;
					}
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
						return list;
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
			if(!isBreak)
			{
				if(lastAc != null)
				{
					return getReturnTabList(lastAc.tabList.get(length), args[length]);
					//Return leer, wenn die Tabliste nicht existiert! Aka ein halbes break;
				}
				if(i == length || aclist.isEmpty()) //Wenn das ende erreicht ist oder die aclist vorher leer gesetzt worden ist
				{
					break;
				}
			}
		}
		return reList;
	}
	
	private ArrayList<String> getReturnTabList(ArrayList<String> tabList, String argsi)
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
	
	private ArrayList<String> listIfArgumentIsEmpty(ArrayList<ArgumentConstructor> subarg, Player player)
	{
		ArrayList<String> returnlist = new ArrayList<String>();
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
	
	private ArrayList<String> listIfArgumentIsnotEmpty(ArrayList<ArgumentConstructor> subarg, String arg, Player player)
	{
		ArrayList<String> returnlist = new ArrayList<String>();
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
	
	private ArrayList<ArgumentConstructor> countHowMuchAreStartsWithIgnoreCase(ArrayList<ArgumentConstructor> subarg, String arg)
	{
		ArrayList<ArgumentConstructor> l = new ArrayList<>();
		for(ArgumentConstructor ac : subarg)
		{
			if(ac.getName().toLowerCase().startsWith(arg.toLowerCase()))
			{
				l.add(ac);
			}
		}
		return l;
	}
	
	public String[] AddToStringArray(String[] oldArray, String newString)
	{
	    String[] newArray = Arrays.copyOf(oldArray, oldArray.length+1);
	    newArray[oldArray.length] = newString;
	    return newArray;
	}
}
