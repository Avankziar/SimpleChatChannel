package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;

public class ARGUpdatePlayer extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGUpdatePlayer(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String playername = args[1];
		OfflinePlayer target = Bukkit.getOfflinePlayer(playername);
		ChatUser cuo = ChatUser.getChatUser(args[1]);
		if(cuo == null || target.getPlayer() == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.NoPlayerExist")));
			return;
		}
		cuo.setChannelGlobal(target.getPlayer().hasPermission("scc.channels.global"));
		cuo.setChannelTrade(target.getPlayer().hasPermission("scc.channels.trade"));
		cuo.setChannelAuction(target.getPlayer().hasPermission("scc.channels.auction"));
		cuo.setChannelSupport(target.getPlayer().hasPermission("scc.channels.support"));
		cuo.setChannelTeam(target.getPlayer().hasPermission("scc.channels.team"));
		cuo.setChannelAdmin(target.getPlayer().hasPermission("scc.channels.admin"));
		cuo.setChannelEvent(target.getPlayer().hasPermission("scc.channels.event"));
		cuo.setChannelLocal(target.getPlayer().hasPermission("scc.channels.local"));
		cuo.setChannelWorld(target.getPlayer().hasPermission("scc.channels.world"));
		cuo.setChannelGroup(target.getPlayer().hasPermission("scc.channels.group"));
		cuo.setChannelPrivateMessage(target.getPlayer().hasPermission("scc.channels.pm"));
		cuo.setChannelTemporary(target.getPlayer().hasPermission("scc.channels.pm"));
		cuo.setChannelPermanent(target.getPlayer().hasPermission("scc.channels.pm"));
		cuo.setOptionSpy(target.getPlayer().hasPermission("scc.option.spy"));
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cuo, "`player_uuid` = ?", cuo.getUUID());
		ChatUser.addChatUser(cuo);
		
		player.getPlayer().spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdScc.Updatetarget.IsUpdated")
				.replace("%target%", playername)));
		target.getPlayer().spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString("CmdScc.Updatetarget.YouWasUpdated")
				.replace("%target%", target.getName())));
		return;
	}
}