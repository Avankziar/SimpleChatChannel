package main.java.me.avankziar.simplechatchannels.bungee.commands;

import java.util.ArrayList;
import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.BaseConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.MatchApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SccCommandExecutor extends Command
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public SccCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		super(cc.getName(), null);
		this.plugin = plugin;
		SccCommandExecutor.cc = cc;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) 
	{
		if (!(sender instanceof ProxiedPlayer)) 
		{
			SimpleChatChannels.log.info("/%cmd% is only for ProxiedPlayer!".replace("%cmd%", cc.getName()));
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if(cc == null)
		{
			return;
		}
		if (args.length == 1) 
		{
			if(MatchApi.isInteger(args[0]))
			{
				if(!player.hasPermission(cc.getPermission()))
				{
					///Du hast dafür keine Rechte!
					player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
					return;
				}
				baseCommands(player, Integer.parseInt(args[0])); //Base and Info Command
				return;
			}
		} else if(args.length == 0)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
				return;
			}
			baseCommands(player, 0); //Base and Info Command
			return;
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
						if(player.hasPermission(ac.getPermission()))
						{
							ArgumentModule am = plugin.getArgumentMap().get(ac.getPath());
							if(am != null)
							{
								am.run(sender, args);
							} else
							{
								SimpleChatChannels.log.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName()));
								player.sendMessage(ChatApi.tctl(
										"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName())));
								return;
							}
							return;
						} else
						{
							///Du hast dafür keine Rechte!
							player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
							return;
						}
					} else if(length > ac.maxArgsConstructor) 
					{
						///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
						player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, SimpleChatChannels.infoCommand));
						return;
					} else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
		player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("InputIsWrong"),
				ClickEvent.Action.RUN_COMMAND, SimpleChatChannels.infoCommand));
		return;
	}
	
	public void baseCommands(final ProxiedPlayer player, int page)
	{
		int count = 0;
		int start = page*10;
		int end = page*10+9;
		int last = 0;
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(
				SimpleChatChannels.infoCommandPath+".BaseInfo.Headline")));
		for(BaseConstructor bc : plugin.getHelpList())
		{
			if(count >= start && count <= end)
			{
				if(player.hasPermission(bc.getPermission()))
				{
					sendInfo(player, bc.getPath(), bc.getSuggestion());
				}
			}
			count++;
			last++;
		}
		boolean lastpage = false;
		if(end > last)
		{
			lastpage = true;
		}
		pastNextPage(player, SimpleChatChannels.infoCommandPath, page, lastpage, SimpleChatChannels.infoCommand);
	}
	
	private void sendInfo(ProxiedPlayer player, String path, String suggestion)
	{
		player.sendMessage(ChatApi.apiChat(
				plugin.getYamlHandler().getL().getString(SimpleChatChannels.infoCommandPath+".BaseInfo."+path),
				ClickEvent.Action.SUGGEST_COMMAND, suggestion,
				HoverEvent.Action.SHOW_TEXT,plugin.getYamlHandler().getL().getString("GeneralHover")));
	}
	
	public void pastNextPage(ProxiedPlayer player, String path,
			int page, boolean lastpage, String cmdstring, String...objects)
	{
		if(page==0 && lastpage)
		{
			return;
		}
		int i = page+1;
		int j = page-1;
		TextComponent MSG = ChatApi.tctl("");
		List<BaseComponent> pages = new ArrayList<BaseComponent>();
		if(page!=0)
		{
			TextComponent msg2 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+".BaseInfo.Past"));
			String cmd = cmdstring+" "+String.valueOf(j);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg2.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			pages.add(msg2);
		}
		if(!lastpage)
		{
			TextComponent msg1 = ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(path+".BaseInfo.Next"));
			String cmd = cmdstring+" "+String.valueOf(i);
			for(String o : objects)
			{
				cmd += " "+o;
			}
			msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
			if(pages.size()==1)
			{
				pages.add(ChatApi.tc(" | "));
			}
			pages.add(msg1);
		}
		MSG.setExtra(pages);	
		player.sendMessage(MSG);
	}

}
