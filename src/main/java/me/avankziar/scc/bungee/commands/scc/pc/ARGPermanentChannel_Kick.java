package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Kick extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Kick(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channel = args[2];
		String other = args[3];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
			return;
		}
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER,"`player_name` = ?", other);
		if(cuoffline == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		String targetuuid = cuoffline.getUUID();
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.ViceCannotKickCreator")));
			return;
		}
		if(!cc.getMembers().contains(targetuuid))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.NotAChannelMember")));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.CannotSelfKick")));
			return;
		}
		cc.removeMembers(targetuuid);
		cc.removeVice(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		if(ProxyServer.getInstance().getPlayer(other) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(other);
			target.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.YouWereKicked")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		player.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.YouKicked")
				.replace("%player%", other).replace("%channel%", cc.getNameColor()+cc.getName())));
		
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Kick.KickedSomeone")
				.replace("%player%", other).replace("%channel%", cc.getNameColor()+cc.getName());
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApiOld.tctl(msg));
			}
		}
	}
}