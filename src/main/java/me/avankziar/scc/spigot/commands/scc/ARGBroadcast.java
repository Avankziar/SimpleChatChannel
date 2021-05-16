package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.handler.ChatHandler;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

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
		String message = plugin.getYamlHandler().getLang().getString("CmdScc.Broadcast.Intro");
		for (int i = 1; i < args.length; i++) 
        {
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
        }
		if(PluginSettings.settings.isBungee())
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        DataOutputStream out = new DataOutputStream(stream);
	        try {
				out.writeUTF(StaticValues.SCC_TASK_BROADCAST);
				out.writeUTF((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "Console");
				out.writeUTF(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        for(Player player : plugin.getServer().getOnlinePlayers())
	        {
	        	if(player != null)
	        	{
	        		player.sendPluginMessage(SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	        		break;
	        	}
	        }
			return;
		}
		Channel usedChannel = SimpleChatChannels.channels.get(plugin.getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
		if(usedChannel == null)
		{
			sender.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.UsedChannelForBroadCastDontExist")));
			return;
		}
		ChatHandler ch = new ChatHandler(plugin);
		ch.sendBroadCast(sender, usedChannel, message);
	}
}
