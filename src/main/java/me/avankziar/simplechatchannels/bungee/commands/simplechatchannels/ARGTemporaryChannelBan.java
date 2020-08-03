package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelBan extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelBan(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);;
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
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NoPlayerExist")));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);
		if(target.getName().equals(player.getName()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCBan.CreatorCannotSelfBan")));
			return;
		}
		if(cc.getBanned().contains(target))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCBan.AlreadyBanned")));
			return;
		}
		cc.addBanned(target);
		cc.removeMembers(target);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCBan.YouHasBanned")
				.replace("%player%", args[1])));
		///Du wurdest vom CustomChannel %channel% gebannt!
		target.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCBan.YourWereBanned")
				.replace("%channel%", cc.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem CustomChannel verbannt.
			members.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCBan.CreatorHasBanned")
					.replace("%player%", args[1])));
		}
		return;
	}
}
