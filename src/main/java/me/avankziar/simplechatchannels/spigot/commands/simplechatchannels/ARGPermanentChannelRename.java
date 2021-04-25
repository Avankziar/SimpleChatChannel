package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;

public class ARGPermanentChannelRename extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelRename(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String scc = "CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PermanentChannel.YouAreNotTheOwner")));
			return;
		}
		PermanentChannel other = PermanentChannel.getChannelFromName(args[2]);
		if(other != null)
		{
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString("PCRename.NameAlreadyExist")
					.replace("%name%", other.getName())
					.replace("%channel%", other.getNameColor()+other.getName())));
			return;
		}
		final String oldchannel = cc.getName();
		cc.setName(args[2]);
		plugin.getUtility().updatePermanentChannels(cc);
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der &5perma&fnenten &eChannel %oldchannel% &r&ewurde in %channel% &r&eumbenannt.
				members.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(scc+"PCRename.Renaming")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%oldchannel%", cc.getNameColor()+oldchannel)));
			}
		}
		return;
	}
}