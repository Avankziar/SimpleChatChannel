package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.UsedChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;

public class ARGChannel extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGChannel(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String channelString = args[0];
		if(!SimpleChatChannels.channels.containsKey(channelString))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.ChannelDontExist")));
			return;
		}
		Channel channel = SimpleChatChannels.channels.get(channelString);
		if(!Utility.playerUsedChannels.containsKey(player.getUniqueId().toString())
				|| Utility.playerUsedChannels.get(player.getUniqueId().toString()).containsKey(channel.getUniqueIdentifierName()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.UsedChannelDontExist")));
			return;
		}
		UsedChannel usedChannel = Utility.playerUsedChannels.get(player.getUniqueId().toString()).get(channel.getUniqueIdentifierName());
		if(usedChannel.isUsed())
		{
			usedChannel.setUsed(false);
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.Deactive")));
		} else
		{
			usedChannel.setUsed(true);
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.Active")));
		}
		Utility.playerUsedChannels.get(player.getUniqueId().toString()).replace(channel.getUniqueIdentifierName(), usedChannel);
		plugin.getMysqlHandler().updateData(Type.USEDCHANNEL, usedChannel,
				"`uniqueidentifiername` = ? AND `player_uuid` = ?", usedChannel.getUniqueIdentifierName(), usedChannel.getPlayerUUID());
	}
}
