package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.objects.ChatUserHandler;

public class ARGOptionJoin extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGOptionJoin(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
		if(cu == null)
		{
			return;
		}
		if(cu.isOptionChannelMessage())
		{
			cu.setOptionChannelMessage(false);
			ChatUser.addChatUser(cu);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
					"`player_uuid` = ?", player.getUniqueId().toString());
			///Du hast den Channel %channel% &causgeblendet!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("CmdScc.ChannelToggle.ChannelOff")
					.replace("%channel%", "Join Message")));
		} else
		{
			cu.setOptionChannelMessage(true);
			ChatUser.addChatUser(cu);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
					"`player_uuid` = ?", player.getUniqueId().toString());
			///Du hast den Channel %channel% &aeingeblendet!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("CmdScc.ChannelToggle.ChannelOn")
					.replace("%channel%", "Join Message")));
		}
		return;
	}
}