package main.java.me.avankziar.scc.bungee.commands;

import java.io.IOException;
import java.util.ArrayList;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.handlers.MatchApi;
import main.java.me.avankziar.scc.handlers.TimeHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.Mail;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MailCommandExecutor extends Command
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public MailCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		super(cc.getName(), null);
		this.plugin = plugin;
		MailCommandExecutor.cc = cc;
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
					player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				baseCommands(player, Integer.parseInt(args[0]));
				return;
			}
		} else if(args.length == 0)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
								player.sendMessage(ChatApi.tctl(
										"ArgumentModule from ArgumentConstructor %ac% not found! ERROR!"
										.replace("%ac%", ac.getName())));
								return;
							}
							return;
						} else
						{
							player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
							return;
						}
					} else if(length > ac.maxArgsConstructor) 
					{
						player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
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
		player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				ClickEvent.Action.RUN_COMMAND, SimpleChatChannels.infoCommand));
		return;
	}
	
	public void baseCommands(final ProxiedPlayer player, int page)
	{
		int start = page*10;
		int amount = 10;
		String uuid = player.getUniqueId().toString();
		int unreadedMails = plugin.getMysqlHandler().getCount(Type.MAIL, "`id`",
				"`reciver_uuid` = ? AND `readeddate` = ?", uuid, 0);
		if(unreadedMails == 0)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Base.NoUnreadMail")));
		} else
		{
			ArrayList<Mail> mails = ConvertHandler.convertListVI(plugin.getMysqlHandler().getList(
					Type.MAIL, "`sendeddate`", false, start, amount, "`reciver_uuid` = ? AND `readeddate` = ?", uuid, 0));
			ArrayList<ArrayList<BaseComponent>> list = new ArrayList<>();
			for(Mail mail : mails)
			{
				String cc = mail.getCarbonCopyNames();
				boolean ccExist = true;
				String senders = mail.getSender();
				String sep = plugin.getYamlHandler().getConfig().getString("Mail.SubjectMessageSeperator");
				if(cc.isEmpty() || cc.length() < 3)
				{
					ccExist = false;
				}
				ArrayList<BaseComponent> sublist = new ArrayList<>();
				TextComponent tcRead = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Click"),
						ClickEvent.Action.RUN_COMMAND,
						PluginSettings.settings.getCommands(KeyHandler.MAIL_READ)+mail.getId(),
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Hover"));
				TextComponent tcSendPlus = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendPlus.Click"),
						ClickEvent.Action.SUGGEST_COMMAND,
						PluginSettings.settings.getCommands(KeyHandler.MAIL_SEND)+cc+" Re:"+mail.getSubject()+" "+sep+" ",
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendPlus.Hover"));
				TextComponent tcSendMinus = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendMinus.Click"),
						ClickEvent.Action.SUGGEST_COMMAND,
						PluginSettings.settings.getCommands(KeyHandler.MAIL_SEND)+senders+" Re:"+mail.getSubject()+" "+sep+" ",
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendMinus.Hover"));
				TextComponent tcForward = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Click"),
						ClickEvent.Action.SUGGEST_COMMAND,
						PluginSettings.settings.getCommands(KeyHandler.MAIL_FORWARD)+mail.getId()+" ",
						HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Hover"));
				TextComponent tc = ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Text")
						.replace("%sender%", senders)
						.replace("%subject%", mail.getSubject())
						, HoverEvent.Action.SHOW_TEXT,
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Hover")
						.replace("%sendeddate%", TimeHandler.getDateTime(mail.getSendedDate()))
						.replace("%cc%", (mail.getCarbonCopyNames().isEmpty() ? "/" : mail.getCarbonCopyNames())));
				sublist.add(tcRead);
				if(ccExist)
				{
					sublist.add(tcSendPlus);
				}
				sublist.add(tcSendMinus);
				sublist.add(tcForward);
				sublist.add(tc);
				list.add(sublist);
			}
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Headline")
					.replace("%mailscount%", String.valueOf(unreadedMails))));
			for(ArrayList<BaseComponent> sub : list)
			{
				TextComponent tc = ChatApi.tc("");
				tc.setExtra(sub);
				player.sendMessage(tc);
			}
		}
	}
}