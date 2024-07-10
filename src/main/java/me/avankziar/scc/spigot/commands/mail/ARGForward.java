package main.java.me.avankziar.scc.spigot.commands.mail;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.MatchApi;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.Mail;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

public class ARGForward extends ArgumentModule
{
	private SCC plugin;
	
	public ARGForward(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String idstring = args[1];
		UUID other = Utility.convertNameToUUID(args[2]);
		if(!MatchApi.isInteger(idstring))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NotNumber")));
			return;
		}
		if(other == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Mail mail = (Mail) plugin.getMysqlHandler().getData(MysqlType.MAIL, "`id` = ?", Integer.parseInt(idstring));
		if(mail == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Read.MailNotExist")));
			return;
		}
		if(!mail.getReciverUUID().toString().equalsIgnoreCase(player.getUniqueId().toString())
				&& !mail.getSenderUUID().toString().equalsIgnoreCase(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(mail.getCarbonCopyUUIDs().contains(other.toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Forward.CCHasAlreadyTheMail")));
			return;
		}
		Mail fwd = new Mail(0, mail.getSenderUUID(), mail.getSender().toString(), other, args[2],
				mail.getCarbonCopyUUIDs(), mail.getCarbonCopyNames(), mail.getSendedDate(), 0L,
				"Fwd("+player.getName()+"):"+mail.getSubject(), mail.getRawText());
		plugin.getMysqlHandler().create(MysqlType.MAIL, fwd);
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMail.Forward.Sended")
				.replace("%player%", args[2])));
		Player target = plugin.getServer().getPlayer(other);
		if(target != null)
		{
			target.spigot().sendMessage(ChatApi.tctl(ChatApi.clickHover(
					plugin.getYamlHandler().getLang().getString("CmdMail.Send.HasNewMail").replace("%player%", player.getName()),
					"RUN_COMMAND",
					PluginSettings.settings.getCommands(KeyHandler.MAIL).trim(),
					"SHOW_TEXT",
					plugin.getYamlHandler().getLang().getString("CmdMail.Send.Hover"))));
		}
	}
}