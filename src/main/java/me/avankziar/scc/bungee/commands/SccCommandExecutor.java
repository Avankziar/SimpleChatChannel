package main.java.me.avankziar.scc.bungee.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.commands.tree.BaseConstructor;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.general.handlers.MatchApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SccCommandExecutor extends Command
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public SccCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		super(cc.getName(), null);
		this.plugin = plugin;
		SccCommandExecutor.cc = cc;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) 
	{
		ProxiedPlayer player = null;
		if (sender instanceof ProxiedPlayer) 
		{
			player = (ProxiedPlayer) sender;
		}
		if(cc == null)
		{
			return;
		}
		if (args.length == 1) 
		{
			if(cc.canConsoleAccess() && player == null)
			{
				if(MatchApi.isInteger(args[0]))
				{
					baseCommands(player, Integer.parseInt(args[0]));
					return;
				}
			} else
			{
				if(MatchApi.isInteger(args[0]))
				{
					if(!player.hasPermission(cc.getPermission()))
					{
						player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
						return;
					}
					baseCommands(player, Integer.parseInt(args[0]));
					return;
				}
			}
		} else if(args.length == 0)
		{
			if(cc.canConsoleAccess() && player == null)
			{
				baseCommands(player, 0);
			} else
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				baseCommands(player, 0);
				return;
			}
		}
		int length = args.length-1;
		ArrayList<ArgumentConstructor> aclist = cc.subcommands;
		for(int i = 0; i <= length; i++)
		{
			for(ArgumentConstructor ac : aclist)
			{
				if(args[i].equalsIgnoreCase(ac.getName()))
				{
					if(length >= ac.minArgsConstructor && length <= ac.maxArgsConstructor)
					{
						ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
						if(ac.canConsoleAccess() && player == null)
						{
							if(am != null)
							{
								try
								{
									am.run(sender, args);
								} catch (IOException e)
								{
									e.printStackTrace();
								}
							} else
							{
								SCC.logger.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName()));
								return;
							}
						} else if(player != null)
						{
							if(player.hasPermission(ac.getPermission()))
							{
								if(am != null)
								{
									try
									{
										am.run(sender, args);
									} catch (IOException e)
									{
										e.printStackTrace();
									}
								} else
								{
									SCC.logger.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
											.replace("%ac%", ac.getName()));
									player.sendMessage(ChatApiOld.tctl(
											"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
											.replace("%ac%", ac.getName())));
									return;
								}
								return;
							} else
							{
								player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
								return;
							}
						} else
						{
							SCC.logger.info("Cannot access ArgumentModule! Command is not for ConsoleAccess and Executer is Console "
									+ "or Executor is Player and a other Error set place!"
									.replace("%ac%", ac.getName()));
						}
					}/* else if(length > ac.maxArgsConstructor) 
					{
						if(player == null)
						{
							SimpleChatChannels.log.warning(plugin.getYamlHandler().getLang().getString("InputIsWrong"));
						} else
						{
							player.sendMessage(ChatApiOld.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
									ClickEvent.Action.RUN_COMMAND, SimpleChatChannels.infoCommand));
						}
						return;
					}*/ else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		if(player == null){
			SCC.logger.warning(plugin.getYamlHandler().getLang().getString("InputIsWrong"));
		} else
		{
			player.sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
					"RUN_COMMAND", SCC.infoCommand));
		}
		return;
	}
	
	public void baseCommands(final ProxiedPlayer player, int page)
	{
		int count = 0;
		int start = page*10;
		int end = page*10+9;
		int last = 0;
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("BaseInfo.Headline")));
		for(BaseConstructor bc : plugin.getHelpList())
		{
			if(count >= start && count <= end)
			{
				if(player.hasPermission(bc.getPermission()))
				{
					sendInfo(player, bc);
				}
			}
			count++;
			last++;
		}
		boolean lastpage = false;
		if(end >= last)
		{
			lastpage = true;
		}
		pastNextPage(player, page, lastpage, SCC.infoCommand);
	}
	
	private void sendInfo(ProxiedPlayer player, BaseConstructor bc)
	{
		player.sendMessage(ChatApiOld.clickHover(
				bc.getHelpInfo(),
				"SUGGEST_COMMAND", bc.getSuggestion(),
				"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("GeneralHover")));
	}
	
	public static void pastNextPage(ProxiedPlayer player,
			int page, boolean lastpage, String cmdstring, String...objects)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		List<BaseComponent> pages = new ArrayList<>();
		if(page!=0)
		{
			String cmd = cmdstring+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			pages.add(ChatApiOld.click(SCC.getPlugin().getYamlHandler().getLang().getString("BaseInfo.Past"), "RUN_COMMAND", cmd));
		}
		if(!lastpage)
		{
			String cmd = cmdstring+" "+String.valueOf(i);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			if(pages.size()==1)
			{
				pages.add(ChatApiOld.tctl(" | "));
			}
			pages.add(ChatApiOld.click(SCC.getPlugin().getYamlHandler().getLang().getString("BaseInfo.Next"), "RUN_COMMAND", cmd));
		}
		player.sendMessage(ChatApiOld.tctl(pages));
	}
}