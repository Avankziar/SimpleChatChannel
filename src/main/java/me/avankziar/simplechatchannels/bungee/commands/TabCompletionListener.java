package main.java.me.avankziar.simplechatchannels.bungee.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabCompletionListener implements Listener
{	
	private SimpleChatChannels plugin;
	
	public TabCompletionListener(SimpleChatChannels plugin)
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
		String tab = event.getCursor().substring(1);
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
		debug(player, "cS:"+countSpace+" | "+Arrays.toString(args));
		if(countSpace > args.length) //Sind mehr leerzeichen als Values in dem Array vorhanden, fügt man eines hinzu.
		{
			args = AddToStringArray(args, "");
		}
		debug(player, "cS:"+countSpace+" | "+Arrays.toString(args));
		int length = args.length-1;
		ArrayList<ArgumentConstructor> aclist = cc.subcommands;
		debug(player, "====================================");
		debug(player, "CC: "+cc.getName()+" "+cc.getPath()+" | "+Arrays.toString(args)+" "+length);
		ArrayList<String> OneArgumentBeforeList = new ArrayList<>();
		ArgumentConstructor lastAc = null;
		for(ArgumentConstructor ac : aclist)
		{
			OneArgumentBeforeList.add(ac.getName());
		}
		boolean isBreak = false;
		for(int i = 0; i <= length; i++)
		{
			//ACHTUNG! Ausnahmefall msg!!
			if(cmd.equalsIgnoreCase(SimpleChatChannels.baseCommandIVName) && length == 0)
			{
				event.getSuggestions().clear();
				event.getSuggestions().addAll(getReturnTabList(plugin.getPlayers(), args[length]));
				return;
			}
			//ACHTUNG! Ausnahmefall!!
			
			isBreak = false;
			debug(player, "Tab: i "+i+" "+length);
			for(int j = 0; j <= aclist.size()-1; j++)
			{
				//debug(player, "Tab: j "+j+" "+(aclist.size()-1));
				ArgumentConstructor ac = aclist.get(j);
				debug(player, "Tab: i="+i+" "+ac.getName()+" '"+args[i]+"'");
				if(args[i].isEmpty()) //Wenn egalweches argument leer ist
				{
					//debug(player, "Tab: string is empty");
					event.getSuggestions().clear();
					event.getSuggestions().addAll(getReturnList(ac, args[i], i, player, OneArgumentBeforeList, false));
					return;
				} else
				{
					//debug(player, "Tab: args[i] else "+ac.argument+" '"+args[i]+"'");
					if(i == length)
					{
						if(!args[i].equals(""))
						{
							debug(player, "Tab: string not empty");
							event.getSuggestions().clear();
							event.getSuggestions().addAll(getReturnList(ac, args[i], i, player, OneArgumentBeforeList, true));
							return;
						} else
						{
							debug(player, "Tab: string empty");
							event.getSuggestions().clear();
							event.getSuggestions().addAll(getReturnList(ac, args[i], i, player, OneArgumentBeforeList, false));
							return;
						}
					} else
					{
						if(args[i].equals(ac.getName()))
						{
							debug(player, "Tab: args[i] equals && i != length => ac.subargument++");
							OneArgumentBeforeList.clear();
							OneArgumentBeforeList.addAll(ac.tabList.get(i));
							//Subargument um ein erhöhen
							aclist = ac.subargument;
							isBreak = true;
							lastAc = ac;
							break;
						}
						if(j == aclist.size()-1)
						{
							aclist = new ArrayList<>(); //Wenn keins der Argumente an der spezifischen Position gepasst hat, abbrechen.
							debug(player, "Tab: args[i] Not Equal Any AcList.Ac => Set Empty list");
						}
					}
				}
			}
			if(!isBreak)
			{
				debug(player, "isBreak");
				if(lastAc != null)
				{
					debug(player, "lastAc != null");
					event.getSuggestions().clear();
					event.getSuggestions().addAll(getReturnTabList(lastAc.tabList.get(length), args[length]));
					//Return null, wenn die Tabliste nicht existiert! Aka ein halbes break;
					return;
				}
				if(i == length || aclist.isEmpty()) //Wenn das ende erreicht ist oder die aclist vorher leer gesetzt worden ist
				{
					debug(player, "==> Breaking!");
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
				if(s.startsWith(argsi)) //TODO argsi oder tabList anscheinend null
				{
					list.add(s);
				}
			}
		}
		Collections.sort(list);
		return list;
	}
	
	private List<String> getReturnList(ArgumentConstructor ac, String args, int i, ProxiedPlayer player,
			List<String> OneArgumentBeforeList, boolean startsWith)
	{
		debug(player, "getReturnList() "+i+" "+startsWith);
		List<String> returnlist = new ArrayList<String>();
		debug(player, "OABL: "+OneArgumentBeforeList.toString());
		for(String argc : OneArgumentBeforeList)
		{
			//debug(player, "Loop: argc => "+argc);
			if(startsWith)
			{
				if(argc.startsWith(args))
				{
					ArgumentConstructor argcon = ac.getSubArgument(argc);
					if(argcon != null)
					{
						debug(player, "Loop: argcon "+argcon.getPermission());
						if(player.hasPermission(argcon.getPermission()))
						{
							returnlist.add(argc);
						}
					} else
					{
						returnlist.add(argc);
					}
					debug(player, "Loop: argcon => "+(argcon!=null));
				}
			} else
			{
				ArgumentConstructor argcon = ac.getSubArgument(argc);
				if(argcon != null)
				{
					debug(player, "Loop: argcon "+argcon.getPermission());
					if(player.hasPermission(argcon.getPermission()))
					{
						returnlist.add(argc);
					}
				} else
				{
					returnlist.add(argc);
				}
				debug(player, "Loop: argcon => "+(argcon!=null));
			}
		}
		Collections.sort(returnlist);
		debug(player, returnlist.toString());
		return returnlist;
	}
	
	private void debug(ProxiedPlayer player, String s)
	{
		boolean bo = false;
		if(bo)
		{
			player.sendMessage(ChatApi.tctl(s));
		}
	}
	
	public String[] AddToStringArray(String[] oldArray, String newString)
	{
	    String[] newArray = Arrays.copyOf(oldArray, oldArray.length+1);
	    newArray[oldArray.length] = newString;
	    return newArray;
	}
}
