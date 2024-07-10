package main.java.me.avankziar.scc.spigot.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.commands.tree.BaseConstructor;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.general.handlers.MatchApi;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class SccCommandExecutor implements CommandExecutor
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public SccCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		SccCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(cc == null)
		{
			return false;
		}
		if (args.length == 1) 
		{
			if (!(sender instanceof Player)) 
			{
				SCC.logger.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(MatchApi.isInteger(args[0]))
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return false;
				}
				baseCommands(player, Integer.parseInt(args[0]));
				return true;
			}
		} else if(args.length == 0)
		{
			if (!(sender instanceof Player)) 
			{
				SCC.logger.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
				return false;
			}
			Player player = (Player) sender;
			if(!player.hasPermission(cc.getPermission()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return false;
			}
			baseCommands(player, 0); //Base and Info Command
			return true;
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
						if(!(sender instanceof Player) && ac.canConsoleAccess())
						{
							ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
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
								sender.spigot().sendMessage(ChatApi.tctl(
										"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName())));
								return false;
							}
							return false;
						} else
						{
							if (!(sender instanceof Player)) 
							{
								SCC.logger.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
								return false;
							}
							Player player = (Player) sender;
							if(player.hasPermission(ac.getPermission()))
							{
								ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
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
									player.spigot().sendMessage(ChatApi.tctl(
											"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
											.replace("%ac%", ac.getName())));
									return false;
								}
								return false;
							} else
							{
								player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
								return false;
							}
						}
						
					}/* else if(length > ac.maxArgsConstructor) 
					{
						player.spigot().sendMessage(ChatApi.tctl(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, SimpleChatChannels.infoCommand));
						return false;
					}*/ else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		sender.spigot().sendMessage(ChatApi.tctl(
				ChatApi.click(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				"RUN_COMMAND", SCC.infoCommand)));
		return false;
	}
	
	public void baseCommands(final Player player, int page)
	{
		int count = 0;
		int start = page*10;
		int end = page*10+9;
		int last = 0;
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("BaseInfo.Headline")));
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
	
	private void sendInfo(Player player, BaseConstructor bc)
	{
		player.spigot().sendMessage(ChatApi.tctl(ChatApi.clickHover(
				bc.getHelpInfo(),
				"SUGGEST_COMMAND", bc.getSuggestion(),
				"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("GeneralHover"))));
	}
	
	public static void pastNextPage(Player player,
			int page, boolean lastpage, String cmdstring, String...objects)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		List<String> pages = new ArrayList<>();
		if(page!=0)
		{
			String msg2 = SCC.getPlugin().getYamlHandler().getLang().getString("BaseInfo.Past");
			String cmd = cmdstring+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			pages.add(ChatApi.click(msg2, "SUGGEST_COMMAND", cmd));
		}
		if(!lastpage)
		{
			String msg1 = SCC.getPlugin().getYamlHandler().getLang().getString("BaseInfo.Next");
			String cmd = cmdstring+" "+String.valueOf(i);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			if(pages.size()==1)
			{
				pages.add(" | ");
			}
			pages.add(ChatApi.click(msg1, "SUGGEST_COMMAND", cmd));
		}
		player.spigot().sendMessage(ChatApi.tctl(String.join("", pages)));
	}
}