package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.handler.ChatHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.chat.Channel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;

public class ARGBroadcast extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGBroadcast(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		String message = "";
		for (int i = 1; i < args.length; i++) 
        {
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
        }
		Channel usedChannel = SimpleChatChannels.channels.get(plugin.getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
		if(usedChannel == null)
		{
			
			sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.UsedChannelForBroadCastDontExist")));
			return;
		}
		ChatHandler ch = new ChatHandler(plugin);
		ch.sendBroadCast(sender, usedChannel, message);
	}
}
