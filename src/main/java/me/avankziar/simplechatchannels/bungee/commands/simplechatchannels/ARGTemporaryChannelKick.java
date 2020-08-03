package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = "CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotInAChannel")));
			return;
		}
		ProxiedPlayer creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotTheCreator")));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])==null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(!cc.getMembers().contains(target))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelMember")));
			return;
		}
		if(target.getName().equals(creator.getName()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCKick.CreatorCannotSelfKick")));
			return;
		}
		cc.removeMembers(target);
		///Du wurdest aus dem CustomChannel gekickt!
		target.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCKick.YouWereKicked")
				.replace("%channel%", cc.getName())));
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCKick.YouKicked")
				.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
			members.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCKick.CreatorKickedSomeone")
					.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		}
		return;
	}
}
