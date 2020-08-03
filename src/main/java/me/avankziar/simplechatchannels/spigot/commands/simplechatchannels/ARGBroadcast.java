package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import net.md_5.bungee.api.chat.TextComponent;

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
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String msg = "";
		for (int i = 1; i < args.length; i++) 
        {
			msg += args[i] + " ";
        }
		if(plugin.getUtility().getWordfilter(msg))
		{
			///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
			return;
		}
		TextComponent MSG = ChatApi.tc("");
		MSG.setExtra(utility.broadcast(player, 0, "Global", msg, 
				ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.Broadcast.Intro"))));
		for(Player all : Bukkit.getOnlinePlayers())
		{
			all.spigot().sendMessage(MSG);
		}
		return;
	}
}