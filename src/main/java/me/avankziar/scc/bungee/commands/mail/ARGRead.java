package main.java.me.avankziar.scc.bungee.commands.mail;

import java.io.IOException;
import java.util.ArrayList;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.bungee.objects.BypassPermission;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.MatchApi;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.Mail;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGRead extends ArgumentModule
{
	private SCC plugin;
	
	public ARGRead(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String idstring = args[1];
		boolean isAdmin = false;
		if(!MatchApi.isInteger(idstring))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NotNumber")));
			return;
		}
		Mail mail = (Mail) plugin.getMysqlHandler().getData(MysqlType.MAIL, "`id` = ?", Integer.parseInt(idstring));
		if(mail == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.MailNotExist")));
			return;
		}
		long now = System.currentTimeMillis();
		String readdate = TimeHandler.getDateTime(now);
		if(!mail.getReciverUUID().toString().equalsIgnoreCase(player.getUniqueId().toString()))
		{
			if(!player.hasPermission(BypassPermission.MAIL_READOTHER) 
					&& !mail.getSenderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString()))
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.CannotReadOthersMails")));
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
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.NoChannelIsNullChannel")));
			return;
		}
		String[] ccsplit = mail.getCarbonCopyNames().split("@");
		ChatHandler ch = new ChatHandler(plugin);
		ArrayList<BaseComponent> bclist = ch.getMessageParser(player, mail.getRawText(), usedChannel, usedChannel.getInChatColorMessage())
										.getComponents();
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Headline")
				.replace("%id%", String.valueOf(mail.getId())))); //Headline
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Sender")
				.replace("%sender%", mail.getSender()))); //Sender
		
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Reciver")
				.replace("%reciver%", mail.getReciver()))); //Empfänger
		
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.CC")
				.replace("%cc%", String.join(", ", ccsplit)))); //CC
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Date")
				.replace("%sendeddate%", TimeHandler.getDateTime(mail.getSendedDate()))
				.replace("%readeddate%", readdate))); //Datum
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Subject")
				.replace("%subject%", mail.getSubject()))); //Subject
		player.sendMessage(ChatApiOld.tctl(bclist)); //Message
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.Bottomline"))); //Bottomline
		if(!isAdmin)
		{
			mail.setReadedDate(now);
			plugin.getMysqlHandler().updateData(MysqlType.MAIL, mail, "`id` = ?", mail.getId());
		}
	}
}