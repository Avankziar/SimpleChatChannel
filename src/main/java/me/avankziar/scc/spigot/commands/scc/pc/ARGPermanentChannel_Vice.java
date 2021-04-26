package main.java.me.avankziar.scc.spigot.commands.scc.pc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
public class ARGPermanentChannel_Vice extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_Vice(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		String otherplayer = args[3];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwner")));
			return;
		}
		if(plugin.getServer().getPlayer(otherplayer) == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(otherplayer); 
		if(!cc.getMembers().contains(target.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.NotAChannelMember")));
			return;
		}
		if(cc.getVice().contains(target.getUniqueId().toString()))
		{
			cc.getVice().remove(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Vice.Degraded")
						.replace("%player%", otherplayer).replace("%channel%", cc.getNameColor()+cc.getName());
			for(Player members : plugin.getServer().getOnlinePlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					members.spigot().sendMessage(ChatApi.tctl(msg));
				}
			}
		} else
		{
			cc.getVice().add(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Vice.Promoted")
					.replace("%player%", otherplayer).replace("%channel%", cc.getNameColor()+cc.getName());
			for(Player members : plugin.getServer().getOnlinePlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					members.spigot().sendMessage(ChatApi.tctl(msg));
				}
			}
		}
	}
}