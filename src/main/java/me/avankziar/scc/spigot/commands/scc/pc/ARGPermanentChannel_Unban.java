package main.java.me.avankziar.scc.spigot.commands.scc.pc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

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
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc==null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice"));
			return;
		}
		String otherplayer = args[3];
		String targetuuid = Utility.convertNameToUUID(otherplayer).toString();
		if(targetuuid == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(!cc.getBanned().contains(targetuuid))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.PlayerNotBanned")));
			return;
		}
		
		cc.removeBanned(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.YouUnbanPlayer")
				.replace("%player%", otherplayer)));
		
		Player target = plugin.getServer().getPlayer(otherplayer);
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.PlayerWasUnbanned")
				.replace("%player%", otherplayer)
				.replace("%channel%", cc.getNameColor()+cc.getName());
		if(target != null)
		{
			target.spigot().sendMessage(ChatApi.tctl(msg));
		}
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.spigot().sendMessage(ChatApi.tctl(msg));
			}
		}
	}
}