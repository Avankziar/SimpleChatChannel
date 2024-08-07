package main.java.me.avankziar.scc.bungee.commands;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MessageCommandExecutor extends Command
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public MessageCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		super(cc.getName(), null);
		this.plugin = plugin;
		MessageCommandExecutor.cc = cc;
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (!(sender instanceof ProxiedPlayer)) 
		{
			plugin.getLogger().info("/%cmd% is only for ProxiedPlayer!".replace("%cmd%", cc.getName()));
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if(!player.hasPermission(cc.getPermission()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(args.length <= 1)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.PleaseEnterAMessage")));
			return;
		}
		String otherPlayer = args[0];
		ProxiedPlayer other = plugin.getProxy().getPlayer(otherPlayer);
		if(other == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotOnline")));
			return;
		}
		String message = "";
		int i = 1;
		while(i < args.length)
		{
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
			i++;
		}
		ChatHandler ch = new ChatHandler(plugin);
		if(!ch.prePreCheck(player, message))
		{
			return;
		}
		ch.startPrivateChat(player, other, message);
	}
}