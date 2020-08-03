package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelBan extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelBan(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = "CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Du bist in keinem permanenten Channel!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()) && !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist weder der Ersteller noch der Stellvertreter im permanenten Channel %channel%!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelViceII")
					.replace("%channel%", cc.getName())));
			return;
		}
		ChatUser cut = ChatUser.getChatUser(args[2]);
		if(cut == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		String targetuuid = cut.getUUID();
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du als Stellvertreter kannst den Ersteller nicht ban!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCBan.ViceCannotBanCreator")));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCBan.YourselfCannotBan")));
			return;
		}
		if(cc.getBanned().contains(targetuuid))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCBan.AlreadyBanned")));
			return;
		}
		cc.addBanned(targetuuid);
		cc.removeVice(targetuuid);
		cc.removeMembers(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCBan.YouHasBanned")
				.replace("%player%", args[2])));
		if(ProxyServer.getInstance().getPlayer(args[2]) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCBan.YourWereBanned")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem &5perma&fnenten &eChannel verbannt.
				members.sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCBan.CreatorHasBanned")
						.replace("%player%", args[2])));
			}
		}
		return;
	}
}