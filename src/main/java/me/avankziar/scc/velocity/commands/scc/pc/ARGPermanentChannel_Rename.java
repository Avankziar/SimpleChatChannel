package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGPermanentChannel_Rename extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Rename(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		String otherchannel = args[3];
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
		PermanentChannel other = PermanentChannel.getChannelFromName(otherchannel);
		if(other != null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Rename.NameAlreadyExist")
					.replace("%name%", other.getName())
					.replace("%channel%", other.getNameColor()+other.getName())));
			return;
		}
		final String oldchannel = cc.getName();
		cc.setName(otherchannel);
		plugin.getUtility().updatePermanentChannels(cc);
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Rename.Renaming")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%oldchannel%", cc.getNameColor()+oldchannel);
		for(Player members : plugin.getServer().getAllPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApi.tl(msg));
			}
		}
	}
}