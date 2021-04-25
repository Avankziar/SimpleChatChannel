package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler.Type;
import main.java.me.avankziar.simplechatchannels.bungee.objects.chat.Channel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.UsedChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channelString = args[0];
		if(!SimpleChatChannels.channels.containsKey(channelString))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.ChannelDontExist")));
			return;
		}
		Channel channel = SimpleChatChannels.channels.get(channelString);
		if(!Utility.playerUsedChannels.containsKey(player.getUniqueId().toString())
				|| Utility.playerUsedChannels.get(player.getUniqueId().toString()).containsKey(channel.getUniqueIdentifierName()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.UsedChannelDontExist")));
			return;
		}
		UsedChannel usedChannel = Utility.playerUsedChannels.get(player.getUniqueId().toString()).get(channel.getUniqueIdentifierName());
		if(usedChannel.isUsed())
		{
			usedChannel.setUsed(false);
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.Deactive")));
		} else
		{
			usedChannel.setUsed(true);
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Channel.Active")));
		}
		Utility.playerUsedChannels.get(player.getUniqueId().toString()).replace(channel.getUniqueIdentifierName(), usedChannel);
		plugin.getMysqlHandler().updateData(Type.USEDCHANNEL, usedChannel,
				"`uniqueidentifiername` = ? AND `player_uuid` = ?", usedChannel.getUniqueIdentifierName(), usedChannel.getPlayerUUID());
	}
}
