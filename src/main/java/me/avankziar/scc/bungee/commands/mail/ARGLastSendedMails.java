package main.java.me.avankziar.scc.bungee.commands.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.SccCommandExecutor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.BypassPermission;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
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

public class ARGLastSendedMails extends ArgumentModule
{
	private SCC plugin;
	
	public ARGLastSendedMails(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String othern = player.getName();
		String other = player.getUniqueId().toString();
		int page = 0;
		if(args.length >= 2)
		{
			String s = args[1];
			if(MatchApi.isInteger(s))
			{
				page = Integer.parseInt(s);
			}
		}
		if(args.length >= 3)
		{
			othern = args[2];
			UUID uuid = Utility.convertNameToUUID(args[2]);
			if(uuid == null)
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
				return;
			}
			if(!other.equals(player.getUniqueId().toString())
					&& !player.hasPermission(BypassPermission.MAIL_READOTHER))
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
			other = uuid.toString();
		}
		int start = page*10;
		int amount = 10;
		int totalcount = plugin.getMysqlHandler().getCount(MysqlType.MAIL, "`sender_uuid` = ?", other);
		boolean last = false;
		if((start+amount) > totalcount)
		{
			last = true;
			start = totalcount-10;
			if(start < 0)
			{
				start = 0;
			}
		}
		if(totalcount < 1)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.LastSendedMails.NoMail")));
			return;
		}
		ArrayList<Mail> mails = ConvertHandler.convertListVI(plugin.getMysqlHandler().getList(
				MysqlType.MAIL, "`id` DESC", start, amount, "`sender_uuid` = ?", other));
		ArrayList<ArrayList<BaseComponent>> list = new ArrayList<>();
		for(Mail mail : mails)
		{
			ArrayList<BaseComponent> sublist = new ArrayList<>();
			TextComponent tcRead = ChatApiOld.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Click"),
					"RUN_COMMAND",
					PluginSettings.settings.getCommands(KeyHandler.MAIL_READ)+mail.getId(),
					"SHOW_TEXT",
					plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Hover"));
			TextComponent tcForward = ChatApiOld.clickHover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Click"),
					"SUGGEST_COMMAND",
					PluginSettings.settings.getCommands(KeyHandler.MAIL_FORWARD)+mail.getId()+" ",
					"SHOW_TEXT",
					plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Hover"));
			TextComponent tc = ChatApiOld.hover(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.TextII")
					.replace("%reciver%", mail.getReciver())
					.replace("%subject%", mail.getSubject()),
					"SHOW_TEXT",
					plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Hover")
					.replace("%sendeddate%", TimeHandler.getDateTime(mail.getSendedDate()))
					.replace("%readeddate%", mail.getReadedDate() == 0 ? "/" : TimeHandler.getDateTime(mail.getReadedDate()))
					.replace("%cc%", (mail.getCarbonCopyNames().isEmpty() ? "/" : mail.getCarbonCopyNames())));
			sublist.add(tcRead);
			sublist.add(tcForward);
			sublist.add(tc);
			list.add(sublist);
		}
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.LastSendedMails.Headline")
				.replace("%page%", String.valueOf(page))
				.replace("%player%", othern)));
		for(ArrayList<BaseComponent> sub : list)
		{
			player.sendMessage(ChatApiOld.tctl(sub));
		}
		SccCommandExecutor.pastNextPage(player, page, last, PluginSettings.settings.getCommands(KeyHandler.MAIL_LASTRECEIVEDMAILS));
	}
}