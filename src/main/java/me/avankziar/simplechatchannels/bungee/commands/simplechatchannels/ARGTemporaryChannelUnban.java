package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
			player.sendMessage(ChatApi.tctl(language+"ChannelGeneral.NotTheCreator"));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])==null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(!cc.getBanned().contains(target))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCUnban.PlayerNotBanned")));
			return;
		}
		cc.removeBanned(target);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCUnban.YouUnbanPlayer")
				.replace("%player%", target.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
			members.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCUnban.CreatorUnbanPlayer")
					.replace("%player%", target.getName())));
		}
		return;
	}
}
