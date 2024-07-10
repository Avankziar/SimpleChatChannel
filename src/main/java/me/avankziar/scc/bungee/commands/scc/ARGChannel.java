package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channelString = args[1];
		Channel channel = plugin.getChannel(channelString);
		if(channel == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.ChannelDontExist")));
			return;
		}
		if(!Utility.playerUsedChannels.containsKey(player.getUniqueId().toString())
				|| !Utility.playerUsedChannels.get(player.getUniqueId().toString()).containsKey(channel.getUniqueIdentifierName()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.UsedChannelDontExist")));
			return;
		}
		UsedChannel usedChannel = Utility.playerUsedChannels.get(player.getUniqueId().toString()).get(channel.getUniqueIdentifierName());
		if(usedChannel.isUsed())
		{
			usedChannel.setUsed(false);
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.Deactive")
					.replace("%channel%", usedChannel.getUniqueIdentifierName())));
		} else
		{
			usedChannel.setUsed(true);
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.Active")
					.replace("%channel%", usedChannel.getUniqueIdentifierName())));
		}
		Utility.playerUsedChannels.get(player.getUniqueId().toString()).replace(channel.getUniqueIdentifierName(), usedChannel);
		plugin.getMysqlHandler().updateData(MysqlType.USEDCHANNEL, usedChannel,
				"`uniqueidentifiername` = ? AND `player_uuid` = ?", usedChannel.getUniqueIdentifierName(), usedChannel.getPlayerUUID());
	}
}
