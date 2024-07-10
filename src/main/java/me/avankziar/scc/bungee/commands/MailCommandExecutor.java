package main.java.me.avankziar.scc.bungee.commands;

import java.io.IOException;
import java.util.ArrayList;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.handlers.MatchApi;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.Mail;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MailCommandExecutor extends Command
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public MailCommandExecutor(SCC plugin, CommandConstructor cc)
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
			SCC.logger.info("/%cmd% is only for ProxiedPlayer!".replace("%cmd%", cc.getName()));
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
					player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				baseCommands(player, Integer.parseInt(args[0]));
				return;
			}
		} else if(args.length == 0)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
					} else if(length > ac.maxArgsConstructor) 
					{
						player.sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
								"RUN_COMMAND", SCC.infoCommand));
						return;
					} else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		player.sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				"RUN_COMMAND", SCC.infoCommand));
		return;
	}
	
	public void baseCommands(final ProxiedPlayer player, int page)
	{
		int start = page*10;
		int amount = 10;
		String uuid = player.getUniqueId().toString();
		int unreadedMails = plugin.getMysqlHandler().getCount(MysqlType.MAIL,
				"`reciver_uuid` = ? AND `readeddate` = ?", uuid, 0);
		if(unreadedMails == 0)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Base.NoUnreadMail")));
		} else
		{
			ArrayList<Mail> mails = ConvertHandler.convertListVI(plugin.getMysqlHandler().getList(
					MysqlType.MAIL, "`sendeddate` ASC", start, amount, "`reciver_uuid` = ? AND `readeddate` = ?", uuid, 0));
			ArrayList<ArrayList<BaseComponent>> list = new ArrayList<>();
			for(Mail mail : mails)
			{
				ArrayList<BaseComponent> sublist = new ArrayList<>();
				String cc = mail.getCarbonCopyNames();
				boolean ccExist = true;
				String senders = mail.getSender();
				String sep = plugin.getYamlHandler().getConfig().getString("Mail.SubjectMessageSeperator");
				if(cc.isEmpty() || cc.length() < 3)
				{
					ccExist = false;
				}
				TextComponent tcRead = ChatApiOld.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Click"),
						"RUN_COMMAND",
						PluginSettings.settings.getCommands(KeyHandler.MAIL_READ)+mail.getId(),
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Hover"));
				TextComponent tcSendPlus = ChatApiOld.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendPlus.Click"),
						"SUGGEST_COMMAND",
						PluginSettings.settings.getCommands(KeyHandler.MAIL_SEND)+cc+" Re:"+mail.getSubject()+" "+sep+" ",
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendPlus.Hover"));
				TextComponent tcSendMinus = ChatApiOld.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendMinus.Click"),
						"SUGGEST_COMMAND",
						PluginSettings.settings.getCommands(KeyHandler.MAIL_SEND)+senders+" Re:"+mail.getSubject()+" "+sep+" ",
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendMinus.Hover"));
				TextComponent tcForward = ChatApiOld.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Click"),
						"SUGGEST_COMMAND",
						PluginSettings.settings.getCommands(KeyHandler.MAIL_FORWARD)+mail.getId()+" ",
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Hover"));
				TextComponent tc = ChatApiOld.hover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Text")
						.replace("%sender%", senders)
						.replace("%subject%", mail.getSubject()), 
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Hover")
						.replace("%sendeddate%", TimeHandler.getDateTime(mail.getSendedDate()))
						.replace("%readeddate%", TimeHandler.getDateTime(mail.getSendedDate()))
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
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Headline")
					.replace("%mailscount%", String.valueOf(unreadedMails))));
			for(ArrayList<BaseComponent> sub : list)
			{
				player.sendMessage(ChatApiOld.tctl(sub));
			}
		}
	}
}