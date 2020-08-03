package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelInherit extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelInherit(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String scc = "CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
				"`player_name` = ?", args[2]);
		if(cuoffline == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"NoPlayerExist")));
			return;
		}
		String uuid =  cuoffline.getUUID();
		final String oldcreatoruuid = cc.getCreator();
		final String oldcreator = Utility.convertUUIDToName(oldcreatoruuid);
		cc.removeMembers(oldcreatoruuid);
		cc.setCreator(uuid);
		cc.addMembers(uuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Im &5perma&fnenten &eChannel %channel% &r&ebeerbt der Spieler &a%creator% &eden Spieler &c%oldcreator% &eals neuer Ersteller des Channels.
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInherit.NewCreator")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%creator%", args[2])
				.replace("%oldcreator%", oldcreator)));
		if(ProxyServer.getInstance().getPlayer(oldcreator) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(oldcreator);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(scc+"PCInherit.NewCreator")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%creator%", args[2])
					.replace("%oldcreator%", oldcreator)));
		}
		if(ProxyServer.getInstance().getPlayer(args[2]) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(scc+"PCInherit.NewCreator")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%creator%", args[2])
					.replace("%oldcreator%", oldcreator)));
		}
		for(ProxiedPlayer member : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(member.getUniqueId().toString()))
			{
				member.sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(scc+"PCInherit.NewCreator")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%creator%", args[2])
						.replace("%oldcreator%", oldcreator)));
			}
		}
		return;
	}
}
