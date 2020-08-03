package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGOptionSpy extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGOptionSpy(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		ChatUser cu = ChatUser.getChatUser(player.getUniqueId());
		if(cu == null)
		{
			return;
		}
		if(cu.isOptionSpy())
		{
			cu.setOptionSpy(false);
			ChatUser.addChatUser(cu);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
					"`player_uuid` = ?", player.getUniqueId().toString());
			///Du hast den Channel %channel% &causgeblendet!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("CmdScc.ChannelToggle.ChannelOff")
					.replace("%channel%", "Spy")));
		} else
		{
			cu.setOptionSpy(true);
			ChatUser.addChatUser(cu);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
					"`player_uuid` = ?", player.getUniqueId().toString());
			///Du hast den Channel %channel% &aeingeblendet!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("CmdScc.ChannelToggle.ChannelOn")
					.replace("%channel%", "Spy")));
		}
	}
}
