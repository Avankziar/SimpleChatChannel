package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Unban extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_Unban(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tctl("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice"));
			return;
		}
		String otherplayer = args[3];
		String targetuuid = Utility.convertNameToUUID(otherplayer).toString();
		if(targetuuid == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(!cc.getBanned().contains(targetuuid))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.PlayerNotBanned")));
			return;
		}
		
		cc.removeBanned(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.YouUnbanPlayer")
				.replace("%player%", otherplayer)));
		
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(otherplayer);
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.PlayerWasUnbanned")
				.replace("%player%", otherplayer)
				.replace("%channel%", cc.getNameColor()+cc.getName());
		if(target != null)
		{
			target.sendMessage(ChatApi.tctl(msg));
		}
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApi.tctl(msg));
			}
		}
	}
}