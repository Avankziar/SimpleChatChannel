package main.java.me.avankziar.simplechatchannels.bungee.commands;

import java.util.List;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class _CommandHelper
{
	private SimpleChatChannels plugin;
	private String scc = "CmdScc.";
	
	public _CommandHelper(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	//REMOVEME alt
	/*public void channeltoggle(ProxiedPlayer player, String channel, String replacer)
	{
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
		if(cu != null)
		{
			if(ChatUser.getBoolean(cu, channel))
			{
				cu = ChatUser.updateBoolean(cu, channel, false);
				ChatUser.addChatUser(cu);
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
						"`player_uuid` = ?", player.getUniqueId().toString());
				player.sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getLang().getString(scc+"ChannelToggle.ChannelOff")
						.replace("%channel%", replacer)));
			} else
			{
				cu = ChatUser.updateBoolean(cu, channel, true);
				ChatUser.addChatUser(cu);
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
						"`player_uuid` = ?", player.getUniqueId().toString());
				player.sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getLang().getString(scc+"ChannelToggle.ChannelOn")
						.replace("%channel%", replacer)));
			}
		}
		return;
	}
	
	/*public void optiontoggle(ProxiedPlayer player, String optionperm,
			String option, String replacer)
	{
		if(((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), option, "player_uuid")))
		{
			plugin.getMysqlHandler().updateDataI(player, false, option);
			///Du hast den Channel %channel% &causgeblendet!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(scc+"ChannelToggle.ChannelOff")
					.replace("%channel%", replacer)));
		} else
		{
			plugin.getMysqlHandler().updateDataI(player, true, option);
			///Du hast den Channel %channel% &aeingeblendet!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(scc+"ChannelToggle.ChannelOn")
					.replace("%channel%", replacer)));
		}
	}*/
}
