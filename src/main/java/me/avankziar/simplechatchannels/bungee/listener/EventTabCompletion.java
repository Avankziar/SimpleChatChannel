package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventTabCompletion implements Listener
{	
	private SimpleChatChannels plugin;
	
	public EventTabCompletion(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onTab(TabCompleteEvent event)
	{
		String c = event.getCursor();
		String[] cc = c.split(" ");
		int space = plugin.getUtility().countCharacters(c, " ");
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		if (event.getSender() instanceof ProxiedPlayer) 
		{
			if (c.length()>=1) 
			{
				if(cc[0].equalsIgnoreCase("/scc") || cc[0].equalsIgnoreCase("/simplechatchannels"))
				{
					HashMap<String, CommandModule> commandList = SimpleChatChannels.sccarguments;
					List<String> list = new ArrayList<String>();
					if(cc.length == 1 && space == 1)
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
					} else if(cc.length == 2 && space == 1)
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
					} else if(cc.length == 2 && space == 2)
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
										if(mod.secondargument != null)
										{
											for(String secondargument : mod.secondargument)
											{
												list.add(secondargument);
											}
										}
									}
								}
							}
							Collections.sort(list);
							event.getSuggestions().clear();
							event.getSuggestions().addAll(list);
							return;
						}
					} else if(cc.length == 3 && space == 2)
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
										if(mod.secondargument != null)
										{
											for(String secondargument : mod.secondargument)
											{
												if(secondargument.equalsIgnoreCase("<Player>") 
														|| secondargument.equalsIgnoreCase("[Player]")
														|| secondargument.equalsIgnoreCase("<Players>") 
														|| secondargument.equalsIgnoreCase("[Players]"))
												{
													list.add(secondargument);
													for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
										    		{
														if(Utility.containsIgnoreCase(pp.getName(), cc[2].toLowerCase()))
														{
										    				list.add(pp.getName());
														}
										    		}
												} else
												{
													if (secondargument.startsWith(cc[2].toLowerCase())) 
													{
														list.add(secondargument);
													}
												}
											}
										}
									}
								}
							}
							Collections.sort(list);
							event.getSuggestions().clear();
							event.getSuggestions().addAll(list);
							return;
						}
					} else
					{
						event.getSuggestions().clear();
						return;
					}
				}
			}
		}
	}
}
