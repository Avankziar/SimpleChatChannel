package main.java.me.avankziar.scc.spigot.commands;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.handlers.MatchApi;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.Mail;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

public class MailCommandExecutor implements CommandExecutor
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public MailCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		MailCommandExecutor.cc = cc;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if (!(sender instanceof Player)) 
		{
			SCC.logger.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
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
					} else if(length > ac.maxArgsConstructor) 
					{
						player.spigot().sendMessage(ChatApi.tctl(ChatApi.click(
								plugin.getYamlHandler().getLang().getString("InputIsWrong"),
								"RUN_COMMAND", SCC.infoCommand)));
						return false;
					} else
					{
						aclist = ac.subargument;
						break;
					}
				}
			}
		}
		player.spigot().sendMessage(ChatApi.tctl(ChatApi.click(
				plugin.getYamlHandler().getLang().getString("InputIsWrong"),
				"RUN_COMMAND", SCC.infoCommand)));
		return false;
	}
	
	public void baseCommands(final Player player, int page)
	{
		int start = page*10;
		int amount = 10;
		String uuid = player.getUniqueId().toString();
		int unreadedMails = plugin.getMysqlHandler().getCount(MysqlType.MAIL,
				"`reciver_uuid` = ? AND `readeddate` = ?", uuid, 0);
		if(unreadedMails == 0)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Base.NoUnreadMail")));
		} else
		{
			ArrayList<Mail> mails = ConvertHandler.convertListVI(plugin.getMysqlHandler().getList(
					MysqlType.MAIL, "`sendeddate` ASC", start, amount, "`reciver_uuid` = ? AND `readeddate` = ?", uuid, 0));
			ArrayList<String> list = new ArrayList<>();
			for(Mail mail : mails)
			{
				ArrayList<String> sublist = new ArrayList<>();
				String cc = mail.getCarbonCopyNames();
				boolean ccExist = true;
				String senders = mail.getSender();
				String sep = plugin.getYamlHandler().getConfig().getString("Mail.SubjectMessageSeperator");
				if(cc.isEmpty() || cc.length() < 3)
				{
					ccExist = false;
				}
				String tcRead = ChatApi.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Click"),
						"RUN_COMMAND",
						PluginSettings.settings.getCommands(KeyHandler.MAIL_READ)+mail.getId(),
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Hover"));
				String tcSendPlus = ChatApi.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendPlus.Click"),
						"SUGGEST_COMMAND",
						PluginSettings.settings.getCommands(KeyHandler.MAIL_SEND)+cc+" Re:"+mail.getSubject()+" "+sep+" ",
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendPlus.Hover"));
				String tcSendMinus = ChatApi.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendMinus.Click"),
						"SUGGEST_COMMAND",
						PluginSettings.settings.getCommands(KeyHandler.MAIL_SEND)+senders+" Re:"+mail.getSubject()+" "+sep+" ",
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.SendMinus.Hover"));
				String tcForward = ChatApi.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Click"),
						"SUGGEST_COMMAND",
						PluginSettings.settings.getCommands(KeyHandler.MAIL_FORWARD)+mail.getId()+" ",
						"SHOW_TEXT",
						plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Hover"));
				String tc = ChatApi.hover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Text")
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
				list.add(String.join("", sublist));
			}
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Headline")
					.replace("%mailscount%", String.valueOf(unreadedMails))));
			for(String sub : list)
			{
				player.spigot().sendMessage(ChatApi.tctl(sub));
			}
		}
	}
}