package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGUpdatePlayer extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGUpdatePlayer(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer players = (ProxiedPlayer) sender;
		String playername = args[1];
		ProxiedPlayer target = plugin.getProxy().getPlayer(playername);
		ChatUser cuo = ChatUser.getChatUser(args[1]);
		if(cuo == null || target == null)
		{
			players.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.NoPlayerExist")));
			return;
		}
		cuo.setChannelGlobal(target.hasPermission("scc.channels.global"));
		cuo.setChannelTrade(target.hasPermission("scc.channels.trade"));
		cuo.setChannelAuction(target.hasPermission("scc.channels.auction"));
		cuo.setChannelSupport(target.hasPermission("scc.channels.support"));
		cuo.setChannelTeam(target.hasPermission("scc.channels.team"));
		cuo.setChannelAdmin(target.hasPermission("scc.channels.admin"));
		cuo.setChannelEvent(target.hasPermission("scc.channels.event"));
		cuo.setChannelLocal(target.hasPermission("scc.channels.local"));
		cuo.setChannelWorld(target.hasPermission("scc.channels.world"));
		cuo.setChannelGroup(target.hasPermission("scc.channels.group"));
		cuo.setChannelPrivateMessage(target.hasPermission("scc.channels.pm"));
		cuo.setChannelTemporary(target.hasPermission("scc.channels.pm"));
		cuo.setChannelPermanent(target.hasPermission("scc.channels.pm"));
		cuo.setOptionSpy(target.hasPermission("scc.option.spy"));
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cuo, "`player_uuid` = ?", cuo.getUUID());
		ChatUser.addChatUser(cuo);
		
		players.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdScc.Updatetarget.IsUpdated")
				.replace("%target%", playername)));
		target.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdScc.Updatetarget.YouWasUpdated")
				.replace("%target%", target.getName())));
		return;
	}
}