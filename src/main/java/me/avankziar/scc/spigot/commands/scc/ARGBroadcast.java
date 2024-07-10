package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.handler.ChatHandlerAdventure;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

public class ARGBroadcast extends ArgumentModule
{
	private SCC plugin;
	
	public ARGBroadcast(SCC plugin, ArgumentConstructor argumentConstructor)
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
				out.writeUTF("null");
			} catch (IOException e) {
				e.printStackTrace();
			}
	        for(Player player : plugin.getServer().getOnlinePlayers())
	        {
	        	if(player != null)
	        	{
	        		player.sendPluginMessage(SCC.getPlugin(), StaticValues.SCC_TOPROXY, stream.toByteArray());
	        		break;
	        	}
	        }
			return;
		}
		Channel usedChannel = SCC.channels.get(plugin.getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
		if(usedChannel == null)
		{
			sender.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.UsedChannelForBroadCastDontExist")));
			return;
		}
		ChatHandlerAdventure ch = new ChatHandlerAdventure(plugin);
		ch.sendBroadCast(sender, usedChannel, message, null);
	}
}
