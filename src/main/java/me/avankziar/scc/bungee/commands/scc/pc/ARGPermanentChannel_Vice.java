package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Vice extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Vice(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channel = args[2];
		String otherplayer = args[3];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwner")));
			return;
		}
		if(plugin.getProxy().getPlayer(otherplayer) == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(otherplayer); 
		if(!cc.getMembers().contains(target.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.NotAChannelMember")));
			return;
		}
		if(cc.getVice().contains(target.getUniqueId().toString()))
		{
			cc.getVice().remove(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Vice.Degraded")
						.replace("%player%", otherplayer).replace("%channel%", cc.getNameColor()+cc.getName());
			for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					members.sendMessage(ChatApiOld.tctl(msg));
				}
			}
		} else
		{
			cc.getVice().add(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Vice.Promoted")
					.replace("%player%", otherplayer).replace("%channel%", cc.getNameColor()+cc.getName());
			for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					members.sendMessage(ChatApiOld.tctl(msg));
				}
			}
		}
	}
}