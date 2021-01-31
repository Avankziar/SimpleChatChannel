package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelKick extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelKick(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelViceII")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
			return;
		}
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
				"`player_name` = ?", args[2]);
		if(cuoffline == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		String targetuuid = cuoffline.getUUID();
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du als Stellvertreter kannst den Ersteller nicht kicken!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCKick.ViceCannotKickCreator")));
			return;
		}
		if(!cc.getMembers().contains(targetuuid))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelMemberII")));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCKick.CannotSelfKick")));
			return;
		}
		cc.removeMembers(targetuuid);
		cc.removeVice(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		if(ProxyServer.getInstance().getPlayer(args[2]) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[2]);
			///Du wurdest aus dem CustomChannel gekickt!
			target.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCKick.YouWereKicked")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCKick.YouKicked")
				.replace("%player%", args[2]).replace("%channel%", cc.getNameColor()+cc.getName())));
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
				members.sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCKick.KickedSomeone")
						.replace("%player%", args[2]).replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}