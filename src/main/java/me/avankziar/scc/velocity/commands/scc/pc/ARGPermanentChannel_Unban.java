package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.assistance.Utility;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGPermanentChannel_Unban extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Unban(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc==null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice"));
			return;
		}
		String otherplayer = args[3];
		String targetuuid = Utility.convertNameToUUID(otherplayer).toString();
		if(targetuuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(!cc.getBanned().contains(targetuuid))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.PlayerNotBanned")));
			return;
		}
		
		cc.removeBanned(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.YouUnbanPlayer")
				.replace("%player%", otherplayer)));
		
		Optional<Player> target = plugin.getServer().getPlayer(otherplayer);
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Unban.PlayerWasUnbanned")
				.replace("%player%", otherplayer)
				.replace("%channel%", cc.getNameColor()+cc.getName());
		if(target.isPresent())
		{
			target.get().sendMessage(ChatApi.tl(msg));
		}
		for(Player members : plugin.getServer().getAllPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApi.tl(msg));
			}
		}
	}
}