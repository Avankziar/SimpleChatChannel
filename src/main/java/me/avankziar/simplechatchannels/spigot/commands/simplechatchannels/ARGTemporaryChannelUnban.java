package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.objects.TemporaryChannel;

public class ARGTemporaryChannelUnban extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelUnban(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = "CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		Player creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(ChatApi.tctl("CmdScc.TemporaryChannel.YouAreNotTheOwner"));
			return;
		}
		if(plugin.getServer().getPlayer(args[1])==null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(args[1]); 
		if(!cc.getBanned().contains(target))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCUnban.PlayerNotBanned")));
			return;
		}
		cc.removeBanned(target);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCUnban.YouUnbanPlayer")
				.replace("%player%", target.getName())));
		for(Player members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
			members.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCUnban.CreatorUnbanPlayer")
					.replace("%player%", target.getName())));
		}
		return;
	}
}
