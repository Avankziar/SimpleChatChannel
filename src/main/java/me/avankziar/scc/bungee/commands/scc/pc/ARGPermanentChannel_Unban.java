package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Unban extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Unban(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc==null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tctl("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice"));
			return;
		}
		String otherplayer = args[3];
		String targetuuid = Utility.convertNameToUUID(otherplayer).toString();
		if(targetuuid == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(!cc.getBanned().contains(targetuuid))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.PlayerNotBanned")));
			return;
		}
		
		cc.removeBanned(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.YouUnbanPlayer")
				.replace("%player%", otherplayer)));
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(otherplayer);
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.PlayerWasUnbanned")
				.replace("%player%", otherplayer)
				.replace("%channel%", cc.getNameColor()+cc.getName());
		if(target != null)
		{
			target.sendMessage(ChatApiOld.tctl(msg));
		}
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApiOld.tctl(msg));
			}
		}
	}
}