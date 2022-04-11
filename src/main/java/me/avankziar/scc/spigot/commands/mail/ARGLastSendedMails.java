package main.java.me.avankziar.scc.spigot.commands.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.handlers.MatchApi;
import main.java.me.avankziar.scc.handlers.TimeHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.Mail;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.SccCommandExecutor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.objects.BypassPermission;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGLastSendedMails extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGLastSendedMails(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
				return;
			}
			if(!other.equals(player.getUniqueId().toString())
					&& !player.hasPermission(BypassPermission.MAIL_READOTHER))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
			other = uuid.toString();
		}
		int start = page*10;
		int amount = 10;
		int totalcount = plugin.getMysqlHandler().getCount(Type.MAIL, "`id`", "`sender_uuid` = ?", other);
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.LastSendedMails.NoMail")));
			return;
		}
		ArrayList<Mail> mails = ConvertHandler.convertListVI(plugin.getMysqlHandler().getList(
				Type.MAIL, "`id`", true, start, amount, "`sender_uuid` = ?", other));
		ArrayList<ArrayList<BaseComponent>> list = new ArrayList<>();
		for(Mail mail : mails)
		{
			ArrayList<BaseComponent> sublist = new ArrayList<>();
			TextComponent tcRead = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Click"),
					ClickEvent.Action.RUN_COMMAND,
					PluginSettings.settings.getCommands(KeyHandler.MAIL_READ)+mail.getId(),
					HoverEvent.Action.SHOW_TEXT,
					plugin.getYamlHandler().getLang().getString("CmdMail.Base.Read.Hover"));
			TextComponent tcForward = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Click"),
					ClickEvent.Action.SUGGEST_COMMAND,
					PluginSettings.settings.getCommands(KeyHandler.MAIL_FORWARD)+mail.getId()+" ",
					HoverEvent.Action.SHOW_TEXT,
					plugin.getYamlHandler().getLang().getString("CmdMail.Base.Forward.Hover"));
			TextComponent tc = ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.TextII")
					.replace("%reciver%", mail.getReciver())
					.replace("%subject%", mail.getSubject())
					, HoverEvent.Action.SHOW_TEXT,
					plugin.getYamlHandler().getLang().getString("CmdMail.Base.Subject.Hover")
					.replace("%sendeddate%", TimeHandler.getDateTime(mail.getSendedDate()))
					.replace("%readeddate%", mail.getReadedDate() == 0 ? "/" : TimeHandler.getDateTime(mail.getReadedDate()))
					.replace("%cc%", (mail.getCarbonCopyNames().isEmpty() ? "/" : mail.getCarbonCopyNames())));
			sublist.add(tcRead);
			sublist.add(tcForward);
			sublist.add(tc);
			list.add(sublist);
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.LastSendedMails.Headline")
				.replace("%page%", String.valueOf(page))
				.replace("%player%", othern)));
		for(ArrayList<BaseComponent> sub : list)
		{
			TextComponent tc = ChatApi.tc("");
			tc.setExtra(sub);
			player.spigot().sendMessage(tc);
		}
		SccCommandExecutor.pastNextPage(player, page, last, PluginSettings.settings.getCommands(KeyHandler.MAIL_LASTRECEIVEDMAILS));
	}
}