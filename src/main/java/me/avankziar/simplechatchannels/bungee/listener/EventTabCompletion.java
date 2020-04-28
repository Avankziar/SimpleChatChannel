package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
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
					}
					event.getSuggestions().clear();
					return;
				}
			}
		}
	}
	
	public boolean equal(int i, ProxiedPlayer player, HashMap<String, CommandModule> commandList, List<String> list,
			TabCompleteEvent event, String[] cc)
	{
		for (String commandString : commandList.keySet()) 
		{
			CommandModule mod = commandList.get(commandString);
			if (player.hasPermission(mod.permission)) 
			{
				if(i == 1)
				{
					if (commandString.startsWith(cc[1].toLowerCase())) 
					{
						list.add(commandString);
					}
				} else if(i >= 2)
				{
					if (commandString.startsWith(cc[1].toLowerCase())) 
					{
						if(mod.nextArgumentChain != null)
						{
							if(i == 2)
							{
								for(String[] nac : mod.nextArgumentChain)
								{
									replacerEqual(list,nac[0]);
								}
							} else if(i >= 3)
							{
								if(i == 3)
								{
									for(String[] nac : mod.nextArgumentChain)
									{
										try
										{
											if(nac[0].equalsIgnoreCase(cc[2]))
											{
												replacerEqual(list, nac[1]);
											}
										} catch(IndexOutOfBoundsException e) {} 
										catch(NullPointerException e){}
									}
								} else if(i >= 4)
								{
									if(i == 4)
									{
										for(String[] nac : mod.nextArgumentChain)
										{
											try
											{
												if(nac[0].equalsIgnoreCase(cc[2])
														&& nac[1].equalsIgnoreCase(cc[3]))
												{
													replacerEqual(list, nac[1]);
												}
											} catch(IndexOutOfBoundsException e) {} 
											catch(NullPointerException e){}
										}
									} else if(i >= 5)
									{
										if(i == 5)
										{
											for(String[] nac : mod.nextArgumentChain)
											{
												try
												{
													if(nac[0].equalsIgnoreCase(cc[2])
															&& nac[1].equalsIgnoreCase(cc[3])
															&& nac[2].equalsIgnoreCase(cc[4]))
													{
														replacerEqual(list, nac[1]);
													}
												} catch(IndexOutOfBoundsException e) {} 
												catch(NullPointerException e){}
											}
										}
									}
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
		return true;
	}
	
	public boolean notequal(int i, ProxiedPlayer player, HashMap<String, CommandModule> commandList, List<String> list,
			TabCompleteEvent event, String[] cc)
	{
		for (String commandString : commandList.keySet()) 
		{
			CommandModule mod = commandList.get(commandString);
			if (player.hasPermission(mod.permission)) 
			{
				if(i == 1)
				{
					list.add(commandString);
				} else if(i >= 2)
				{
					if (commandString.startsWith(cc[1].toLowerCase())) 
					{
						if(mod.nextArgumentChain != null)
						{
							if(i == 2)
							{
								for(String[] nac : mod.nextArgumentChain)
								{
									replacerNotEqual(list,nac[0],cc[2]);
								}
							} else if(i >= 3)
							{
								if(i == 3)
								{
									for(String[] nac : mod.nextArgumentChain)
									{
										try
										{
											if(nac[0].equalsIgnoreCase(cc[2]))
											{
												replacerNotEqual(list, nac[1], cc[3]);
											}
										} catch(IndexOutOfBoundsException e) {/*nothing*/} 
										catch(NullPointerException e){/*nothing*/}
									}
								} else if(i >= 4)
								{
									if(i == 4)
									{
										for(String[] nac : mod.nextArgumentChain)
										{
											try
											{
												if(nac[0].equalsIgnoreCase(cc[2])
														&& nac[1].equalsIgnoreCase(cc[3]))
												{
													replacerNotEqual(list, nac[2], cc[4]);
												}
											} catch(IndexOutOfBoundsException e) {/*nothing*/} 
											catch(NullPointerException e){/*nothing*/}
										}
									} else if(i >= 5)
									{
										if(i == 5)
										{
											for(String[] nac : mod.nextArgumentChain)
											{
												try
												{
													if(nac[0].equalsIgnoreCase(cc[2])
															&& nac[1].equalsIgnoreCase(cc[3])
															&& nac[2].equalsIgnoreCase(cc[4]))
													{
														replacerNotEqual(list, nac[1], cc[5]);
													}
												} catch(IndexOutOfBoundsException e) {/*nothing*/} 
												catch(NullPointerException e){/*nothing*/}
											}
										}
									}
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
		return true;
	}
	
	public void replacerEqual(List<String> list, String s)
	{
		try
		{
			if(s.equalsIgnoreCase("<Player>") || s.equalsIgnoreCase("[Player]")
					|| s.equalsIgnoreCase("<Players>") || s.equalsIgnoreCase("[Players]"))
			{
				list.add(s);
				for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
	    		{
					list.add(pp.getName());
	    		}
			} else if(s.equalsIgnoreCase("<Channelname>"))
			{
				for(PermanentChannel pc : PermanentChannel.getPermanentChannel())
				{
					list.add(pc.getName());
				}
			} else
			{
				list.add(s);
			}
		} catch(IndexOutOfBoundsException e) {} catch(NullPointerException e) {}
		return;
	}
	
	public void replacerNotEqual(List<String> list, String s, String search)
	{
		try
		{
			if(s.equalsIgnoreCase("<Player>") || s.equalsIgnoreCase("[Player]")
					|| s.equalsIgnoreCase("<Players>") || s.equalsIgnoreCase("[Players]"))
			{
				list.add(s);
				for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
	    		{
					if(Utility.containsIgnoreCase(pp.getName(), search))
					{
						list.add(pp.getName());
					}
	    		}
			} else if(s.equalsIgnoreCase("<Channelname>"))
			{
				for(PermanentChannel pc : PermanentChannel.getPermanentChannel())
				{
					if(Utility.containsIgnoreCase(pc.getName(), search))
					{
						list.add(pc.getName());
					}
				}
			} else
			{
				if(Utility.containsIgnoreCase(s, search))
				{
					list.add(s);
				}
			}
		} catch(IndexOutOfBoundsException e)
		{
			//nothing
		} catch(NullPointerException e)
		{
			//Nothing
		}
		return;
	}
	
	/*if(cc.length == 1 && space == 1)
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
	/*} else
	{
		event.getSuggestions().clear();
		return;
	}*/
	
	/*
	int ccl = 1;
						int sp = 1;
						while(ccl < 6)
						{
							if(cc.length == ccl && sp == space)
							{
								player.sendMessage(plugin.getUtility().tc(+ccl+" "+sp));
								for (String commandString : commandList.keySet()) 
								{
									CommandModule mod = commandList.get(commandString);
									if (player.hasPermission(mod.permission)) 
									{
										
										if(ccl == 1)
										{
											list.add(commandString);
										} else if(ccl >= 2)
										{
											player.sendMessage(plugin.getUtility().tc("1"));
											if (commandString.startsWith(cc[1].toLowerCase())) 
											{
												player.sendMessage(plugin.getUtility().tc("2"));
												if(mod.nextArgumentChain != null)
												{
													player.sendMessage(plugin.getUtility().tc("3"));
													if(ccl == 2)
													{
														for(String[] nac : mod.nextArgumentChain)
														{
															player.sendMessage(plugin.getUtility().tc("4"));
															//replacerEqual(list,nac[0]);
															try
															{
																if(nac[0].equalsIgnoreCase("<Player>") || nac[0].equalsIgnoreCase("[Player]")
																		|| nac[0].equalsIgnoreCase("<Players>") || nac[0].equalsIgnoreCase("[Players]"))
																{
																	list.add(nac[0]);
																	for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
														    		{
																		list.add(pp.getName());
														    		}
																} else if(nac[0].equalsIgnoreCase("<Channelname>"))
																{
																	for(PermanentChannel pc : PermanentChannel.getPermanentChannel())
																	{
																		list.add(pc.getName());
																	}
																} else
																{
																	list.add(nac[0]);
																}
															} catch(IndexOutOfBoundsException e) {} catch(NullPointerException e) {}
														}
													} else if(ccl >= 3)
													{
														player.sendMessage(plugin.getUtility().tc("5"));
														if(ccl == 3)
														{
															for(String[] nac : mod.nextArgumentChain)
															{
																try
																{
																	if(nac[0].equalsIgnoreCase(cc[2]))
																	{
																		replacerEqual(list, nac[1]);
																	}
																} catch(IndexOutOfBoundsException e) {} 
																catch(NullPointerException e){}
															}
														} else if(ccl >= 4)
														{
															if(ccl == 4)
															{
																for(String[] nac : mod.nextArgumentChain)
																{
																	try
																	{
																		if(nac[0].equalsIgnoreCase(cc[2])
																				&& nac[1].equalsIgnoreCase(cc[3]))
																		{
																			replacerEqual(list, nac[1]);
																		}
																	} catch(IndexOutOfBoundsException e) {} 
																	catch(NullPointerException e){}
																}
															} else if(ccl >= 5)
															{
																if(ccl == 5)
																{
																	for(String[] nac : mod.nextArgumentChain)
																	{
																		try
																		{
																			if(nac[0].equalsIgnoreCase(cc[2])
																					&& nac[1].equalsIgnoreCase(cc[3])
																					&& nac[2].equalsIgnoreCase(cc[4]))
																			{
																				replacerEqual(list, nac[1]);
																			}
																		} catch(IndexOutOfBoundsException e) {} 
																		catch(NullPointerException e){}
																	}
																}
															}
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
							ccl++;
							player.sendMessage(plugin.getUtility().tc(+ccl+" "+sp));
							if(cc.length == ccl && sp == space)
							{
								for (String commandString : commandList.keySet()) 
								{
									CommandModule mod = commandList.get(commandString);
									if (player.hasPermission(mod.permission)) 
									{
										if(ccl == 1)
										{
											if (commandString.startsWith(cc[1].toLowerCase())) 
											{
												list.add(commandString);
											}
										} else if(ccl >= 2)
										{
											if (commandString.startsWith(cc[1].toLowerCase())) 
											{
												if(mod.nextArgumentChain != null)
												{
													if(ccl == 2)
													{
														for(String[] nac : mod.nextArgumentChain)
														{
															replacerNotEqual(list,nac[0],cc[2]);
														}
													} else if(ccl >= 3)
													{
														if(ccl == 3)
														{
															for(String[] nac : mod.nextArgumentChain)
															{
																try
																{
																	if(nac[0].equalsIgnoreCase(cc[2]))
																	{
																		replacerNotEqual(list, nac[1], cc[3]);
																	}
																} catch(IndexOutOfBoundsException e) {} 
																catch(NullPointerException e){}
															}
														} else if(ccl >= 4)
														{
															if(ccl == 4)
															{
																for(String[] nac : mod.nextArgumentChain)
																{
																	try
																	{
																		if(nac[0].equalsIgnoreCase(cc[2])
																				&& nac[1].equalsIgnoreCase(cc[3]))
																		{
																			replacerNotEqual(list, nac[2], cc[4]);
																		}
																	} catch(IndexOutOfBoundsException e) {} 
																	catch(NullPointerException e){}
																}
															} else if(ccl >= 5)
															{
																if(ccl == 5)
																{
																	for(String[] nac : mod.nextArgumentChain)
																	{
																		try
																		{
																			if(nac[0].equalsIgnoreCase(cc[2])
																					&& nac[1].equalsIgnoreCase(cc[3])
																					&& nac[2].equalsIgnoreCase(cc[4]))
																			{
																				replacerNotEqual(list, nac[1], cc[5]);
																			}
																		} catch(IndexOutOfBoundsException e) {} 
																		catch(NullPointerException e){}
																	}
																}
															}
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
							sp++;
						}
					}*/
}
