package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.objects.TemporaryChannel;

public class ARGTemporaryChannelKick extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelKick(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.TemporaryChannel.YouAreNotTheOwner")));
			return;
		}
		if(plugin.getServer().getPlayer(args[1])==null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(args[1]); 
		if(!cc.getMembers().contains(target))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.TemporaryChannel.NotAChannelMember")));
			return;
		}
		if(target.getName().equals(creator.getName()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCKick.CreatorCannotSelfKick")));
			return;
		}
		cc.removeMembers(target);
		///Du wurdest aus dem CustomChannel gekickt!
		target.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCKick.YouWereKicked")
				.replace("%channel%", cc.getName())));
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCKick.YouKicked")
				.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		for(Player members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
			members.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCKick.CreatorKickedSomeone")
					.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		}
		return;
	}
}
