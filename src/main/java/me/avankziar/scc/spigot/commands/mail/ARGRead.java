package main.java.me.avankziar.scc.spigot.commands.mail;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.handlers.MatchApi;
import main.java.me.avankziar.scc.handlers.TimeHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.Mail;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.handler.ChatHandler;
import main.java.me.avankziar.scc.spigot.objects.BypassPermission;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGRead extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGRead(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String idstring = args[1];
		boolean isAdmin = false;
		if(!MatchApi.isInteger(idstring))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NotNumber")));
			return;
		}
		Mail mail = (Mail) plugin.getMysqlHandler().getData(Type.MAIL, "`id` = ?", Integer.parseInt(idstring));
		if(mail == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.MailNotExist")));
			return;
		}
		long now = System.currentTimeMillis();
		String readdate = TimeHandler.getDateTime(now);
		if(!mail.getReciverUUID().toString().equalsIgnoreCase(player.getUniqueId().toString()))
		{
			if(!player.hasPermission(BypassPermission.MAIL_READOTHER) 
					&& !mail.getSenderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.CannotReadOthersMails")));
				return;
			} else if(player.hasPermission(BypassPermission.MAIL_READOTHER) 
					&& !mail.getSenderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString()))
			{
				isAdmin = true;
				readdate = "/";
			} else
			{
				readdate = TimeHandler.getDateTime(mail.getReadedDate());
			}
		}
		String usingChannel = plugin.getYamlHandler().getConfig().getString("Mail.UseChannelForMessageParser");
		Channel usedChannel = plugin.getChannel(usingChannel);
		if(usedChannel == null )
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.NoChannelIsNullChannel")));
			return;
		}
		String[] ccsplit = mail.getCarbonCopyNames().split("@");
		ChatHandler ch = new ChatHandler(plugin);
		ArrayList<BaseComponent> bclist = ch.getMessageParser(player, mail.getRawText(), usedChannel, usedChannel.getInChatColorMessage())
										.getComponents();
		TextComponent tc = ChatApi.tc("");
		tc.setExtra(bclist);
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Headline")
				.replace("%id%", String.valueOf(mail.getId())))); //Headline
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Sender")
				.replace("%sender%", mail.getSender()))); //Sender
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Reciver")
				.replace("%reciver%", mail.getReciver()))); //Empfänger
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.CC")
				.replace("%cc%", String.join(", ", ccsplit)))); //CC
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Date")
				.replace("%sendeddate%", TimeHandler.getDateTime(mail.getSendedDate()))
				.replace("%readeddate%", readdate))); //Datum
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Subject")
				.replace("%subject%", mail.getSubject()))); //Subject
		player.spigot().sendMessage(tc); //Message
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Bottomline"))); //Bottomline
		if(!isAdmin)
		{
			mail.setReadedDate(System.currentTimeMillis());
			plugin.getMysqlHandler().updateData(Type.MAIL, mail, "`id` = ?", mail.getId());
		}
	}
}