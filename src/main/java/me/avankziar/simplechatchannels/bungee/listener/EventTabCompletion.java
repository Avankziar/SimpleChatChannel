package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventTabCompletion implements Listener
{	
	public EventTabCompletion()
	{
		
	}
	
	@EventHandler
	public void onTab(TabCompleteEvent event)
	{
		String c = event.getCursor();
		String[] cc = c.split(" ");
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		if (event.getSender() instanceof ProxiedPlayer) 
		{
			if (c.length()>=1) 
			{
				if(cc[0].equalsIgnoreCase("/scc") || cc[0].equalsIgnoreCase("/simplechatchannels"))
				{
					HashMap<String, CommandModule> commandList = SimpleChatChannels.sccarguments;
					List<String> list = new ArrayList<String>();
					if(cc.length == 1 && c.contains(" "))
					{
						for (String commandString : commandList.keySet()) 
						{
							CommandModule mod = commandList.get(commandString);
							if (player.hasPermission(mod.permission)) 
							{
								list.add(commandString);
							}
						}
						Collections.sort(list);
						event.getSuggestions().clear();
						event.getSuggestions().addAll(list);
						return;
					} else if (cc.length == 2) 
					{
						if (!cc[1].equals("")) 
						{
							for (String commandString : commandList.keySet()) 
							{
								CommandModule mod = commandList.get(commandString);
								if (player.hasPermission(mod.permission))
								{
									if (commandString.startsWith(cc[1].toLowerCase())) 
									{
										list.add(commandString);
									}
								}
							}
							Collections.sort(list);
							event.getSuggestions().clear();
							event.getSuggestions().addAll(list);
							return;
						}
						/*
						 * Die ursprünglich hier angesetzte else Anweisung funktioniert nicht,
						 * da durch die else if (cc.length == 2) der Code bis da garnicht ankommt
						 * weil wenn nur "/scc " geschrieben wurde, ist das immer noch nur 1 Element
						 * und keine zwei. Erst wenn "/scc a" geschrieben wurde oder ähnliches erkennt
						 * er es als 2 Elemente. 
						 */
					} else if(cc.length == 3)
					{
						return; //Müsste man mal überlegen
					}
				}
			}
		}
	}
}