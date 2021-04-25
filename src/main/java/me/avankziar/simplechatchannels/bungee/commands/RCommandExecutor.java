package main.java.me.avankziar.simplechatchannels.bungee.commands;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.handler.ChatHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class RCommandExecutor extends Command
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public RCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		super(cc.getName(), null);
		this.plugin = plugin;
		RCommandExecutor.cc = cc;
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
		if(!player.hasPermission(cc.getPermission()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(args.length <= 1)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.PleaseEnterAMessage")));
			return;
		}
		if(!SimpleChatChannels.rPlayers.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.YouHaveNoPrivateMessagePartner")));
			return;
		}
		ProxiedPlayer other = plugin.getProxy().getPlayer(SimpleChatChannels.rPlayers.get(player.getUniqueId().toString()));
		if(other == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotOnline")));
			return;
		}
		String message = "";
		int i = 0;
		while(i < args.length)
		{
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
		}
		ChatHandler ch = new ChatHandler(plugin);
		if(ch.prePreCheck(player, message))
		{
			return;
		}
		ch.startPrivateChat(player, other, message);
	}
}