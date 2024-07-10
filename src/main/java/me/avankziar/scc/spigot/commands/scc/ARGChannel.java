package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class ARGChannel extends ArgumentModule
{
	private SCC plugin;
	
	public ARGChannel(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[1];
		updateUsedChannel(plugin, player, channel);
	}
	
	public static boolean updateUsedChannel(SCC plugin, final Player player, String channelString)
	{
		Channel channel = plugin.getChannel(channelString);
		if(channel == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.ChannelDontExist")));
			return false;
		}
		if(!Utility.playerUsedChannels.containsKey(player.getUniqueId().toString())
				|| !Utility.playerUsedChannels.get(player.getUniqueId().toString()).containsKey(channel.getUniqueIdentifierName()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.UsedChannelDontExist")));
			return false;
		}
		UsedChannel usedChannel = Utility.playerUsedChannels.get(player.getUniqueId().toString()).get(channel.getUniqueIdentifierName());
		if(usedChannel.isUsed())
		{
			usedChannel.setUsed(false);
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.Deactive")
					.replace("%channel%", usedChannel.getUniqueIdentifierName())));
		} else
		{
			usedChannel.setUsed(true);
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.Active")
					.replace("%channel%", usedChannel.getUniqueIdentifierName())));
		}
		Utility.playerUsedChannels.get(player.getUniqueId().toString()).replace(channel.getUniqueIdentifierName(), usedChannel);
		plugin.getMysqlHandler().updateData(MysqlType.USEDCHANNEL, usedChannel,
				"`uniqueidentifiername` = ? AND `player_uuid` = ?", usedChannel.getUniqueIdentifierName(), usedChannel.getPlayerUUID());
		return true;
	}
}
