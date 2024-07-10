package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGPermanentChannel_Vice extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Vice(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		String otherplayer = args[3];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwner")));
			return;
		}
		if(plugin.getServer().getPlayer(otherplayer).isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(otherplayer).get(); 
		if(!cc.getMembers().contains(target.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.NotAChannelMember")));
			return;
		}
		if(cc.getVice().contains(target.getUniqueId().toString()))
		{
			cc.getVice().remove(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Vice.Degraded")
						.replace("%player%", otherplayer).replace("%channel%", cc.getNameColor()+cc.getName());
			for(Player members : plugin.getServer().getAllPlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					members.sendMessage(ChatApi.tl(msg));
				}
			}
		} else
		{
			cc.getVice().add(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Vice.Promoted")
					.replace("%player%", otherplayer).replace("%channel%", cc.getNameColor()+cc.getName());
			for(Player members : plugin.getServer().getAllPlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					members.sendMessage(ChatApi.tl(msg));
				}
			}
		}
	}
}