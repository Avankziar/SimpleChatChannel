package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.Channel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGBroadcastServer extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGBroadcastServer(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String message = plugin.getYamlHandler().getLang().getString("CmdScc.Broadcast.Intro");
		for (int i = 1; i < args.length; i++) 
        {
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
        }
		Channel usedChannel = plugin.getChannel(plugin.getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
		if(usedChannel == null)
		{
			sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.UsedChannelForBroadCastDontExist")));
			return;
		}
		ChatHandler ch = new ChatHandler(plugin);
		ch.sendBroadCast(sender, usedChannel, message, player.getServer().getInfo().getName());
	}
}