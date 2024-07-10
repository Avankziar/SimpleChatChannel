package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.Channel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGBroadcastServer extends ArgumentModule
{
	private SCC plugin;
	
	public ARGBroadcastServer(SCC plugin, ArgumentConstructor argumentConstructor)
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
			sender.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.UsedChannelForBroadCastDontExist")));
			return;
		}
		ChatHandler ch = new ChatHandler(plugin);
		ch.sendBroadCast(sender, usedChannel, message, player.getServer().getInfo().getName());
	}
}