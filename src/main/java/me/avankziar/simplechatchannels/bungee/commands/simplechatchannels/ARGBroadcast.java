package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGBroadcast extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGBroadcast(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String msg = "";
		for (int i = 1; i < args.length; i++) 
        {
			msg += args[i] + " ";
        }
		if(plugin.getUtility().getWordfilter(msg))
		{
			///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
			return;
		}
		TextComponent MSG = ChatApi.tc("");
		MSG.setExtra(plugin.getUtility().broadcast(player, 0, "Global", msg, 
				ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.Broadcast.Intro"))));
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			all.sendMessage(MSG);
		}
		return;
	}
}
