package main.java.me.avankziar.simplechatchannels.bungee.commands;

import java.util.Arrays;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Command;

public class MessageCommandExecutor extends Command
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public MessageCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
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
			SimpleChatChannels.log.info("/%cmd% is only for ProxiedPlayer!".replace("%cmd%", cc.getName()));
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if(cc == null)
		{
			return;
		}
		if(cc.getPermission() != null)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
				return;
			}
		}
		if(args.length<2)
		{
			return;
		}
		String toPlayer = args[0];
		if(plugin.getProxy().getPlayer(toPlayer) == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return;
		}
		String[] argss = Arrays.copyOfRange(args, 1, args.length);
		String message = "@"+toPlayer+" "+String.join(" ", argss);
		plugin.getProxy().getPluginManager().callEvent(new ChatEvent(player, null, message));
	}

}
