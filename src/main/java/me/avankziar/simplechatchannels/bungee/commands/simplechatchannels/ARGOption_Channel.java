package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.ChatUserHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGOption_Channel extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGOption_Channel(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
		if(cu == null)
		{
			return;
		}
		if(cu.isOptionChannelMessage())
		{
			cu.setOptionChannelMessage(false);
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Option.Channel.Deactive")));
		} else
		{
			cu.setOptionChannelMessage(true);
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Option.Channel.Active")));
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
				"`player_uuid` = ?", cu.getUUID());
	}
}
