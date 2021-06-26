package main.java.me.avankziar.scc.bungee.commands.mail;

import java.io.IOException;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.handlers.MatchApi;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.Mail;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGForward extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGForward(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String idstring = args[1];
		UUID other = Utility.convertNameToUUID(args[2]);
		if(!MatchApi.isInteger(idstring))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NotNumber")));
			return;
		}
		if(other == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Mail mail = (Mail) plugin.getMysqlHandler().getData(Type.MAIL, "`id` = ?", Integer.parseInt(idstring));
		if(mail == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.MailNotExist")));
			return;
		}
		if(!mail.getReciverUUID().toString().equalsIgnoreCase(player.getUniqueId().toString())
				&& !mail.getSenderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(mail.getCarbonCopyUUIDs().contains(other.toString()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Forward.CCHasAlreadyTheMail")));
			return;
		}
		Mail fwd = new Mail(0, mail.getSenderUUID(), mail.getSender().toString(), other, args[2],
				mail.getCarbonCopyUUIDs(), mail.getCarbonCopyNames(), mail.getSendedDate(), 0L,
				"Fwd("+player.getName()+"):"+mail.getSubject(), mail.getRawText());
		plugin.getMysqlHandler().create(Type.MAIL, fwd);
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Forward.Sended")
				.replace("%player%", args[2])));
		ProxiedPlayer target = plugin.getProxy().getPlayer(other);
		if(target != null)
		{
			target.sendMessage(ChatApi.apiChat(
					plugin.getYamlHandler().getLang().getString("CmdMail.Send.HasNewMail").replace("%player%", player.getName()),
					ClickEvent.Action.RUN_COMMAND,
					PluginSettings.settings.getCommands(KeyHandler.MAIL).trim(),
					HoverEvent.Action.SHOW_TEXT,
					plugin.getYamlHandler().getLang().getString("CmdMail.Send.Hover")));
		}
	}
}