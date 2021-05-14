package main.java.me.avankziar.scc.spigot.commands;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.handlers.MatchApi;
import main.java.me.avankziar.scc.handlers.TimeHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.Mail;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MailCommandExecutor implements CommandExecutor
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public MailCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		MailCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if (!(sender instanceof Player)) 
		{
			SimpleChatChannels.log.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(cc == null)
		{
			return false;
		}
		if (args.length == 1) 
		{
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
								SimpleChatChannels.log.info("ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
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
					} else if(length > ac.maxArgsConstructor) 
					{
						player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
								ClickEvent.Action.RUN_COMMAND, SimpleChatChannels.infoCommand));
						return false;
					} else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				ClickEvent.Action.RUN_COMMAND, SimpleChatChannels.infoCommand));
		return false;
	}
	
	public void baseCommands(final Player player, int page)
	{
		int start = page*10;
		int amount = 10;
		String uuid = player.getUniqueId().toString();
		int unreadedMails = plugin.getMysqlHandler().getCount(Type.MAIL, "`id`",
				"`reciver_uuid` = ? AND `readeddate` = ?", uuid, 0);
		if(unreadedMails == 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMail.Base.NoUnreadMail")));
		} else
		{
			ArrayList<Mail> mails = ConvertHandler.convertListVI(plugin.getMysqlHandler().getList(
					Type.MAIL, "`sendeddate`", false, start, amount, "`reciver_uuid` = ? AND `readeddate` = ?", uuid, 0));
			ArrayList<ArrayList<BaseComponent>> list = new ArrayList<>();
			for(Mail mail : mails)
			{
				ArrayList<BaseComponent> sublist = new ArrayList<>();
				TextComponent tcRead = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Click"),
						ClickEvent.Action.RUN_COMMAND,
						PluginSettings.settings.getCommands(KeyHandler.MAIL_READ)+mail.getId(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Hover"));
				TextComponent tcSendPlus = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendPlus.Click"),
						ClickEvent.Action.SUGGEST_COMMAND,
						PluginSettings.settings.getCommands(KeyHandler.MAIL_SEND)+mail.getCarbonCopyNames()+" ",
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendPlus.Hover"));
				TextComponent tcSendMinus = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendMinus.Click"),
						ClickEvent.Action.SUGGEST_COMMAND,
						PluginSettings.settings.getCommands(KeyHandler.MAIL_SEND)+mail.getSender()+" ",
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendMinus.Hover"));
				TextComponent tc = ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Text")
						.replace("%sender%", mail.getSender())
						.replace("%subject%", mail.getSubject())
						, HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Hover")
						.replace("%sendeddate%", TimeHandler.getDateTime(mail.getSendedDate()))
						.replace("%cc%", (mail.getCarbonCopyNames().isEmpty() ? "/" : mail.getCarbonCopyNames())));
				sublist.add(tcRead);
				sublist.add(tcSendPlus);
				sublist.add(tcSendMinus);
				sublist.add(tc);
				list.add(sublist);
			}
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Headline")
					.replace("%mailscount%", String.valueOf(unreadedMails))));
			for(ArrayList<BaseComponent> sub : list)
			{
				TextComponent tc = ChatApi.tc("");
				tc.setExtra(sub);
				player.spigot().sendMessage(tc);
			}
		}
	}
}