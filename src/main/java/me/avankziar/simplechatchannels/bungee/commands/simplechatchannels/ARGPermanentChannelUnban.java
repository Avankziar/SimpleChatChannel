package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelUnban extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelUnban(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
			///Du bist weder der Ersteller noch ein Stellvertreter im &5perma&fnenten &cChannel &f%channel%&c!
			player.sendMessage(ChatApi.tctl(language+"ChannelGeneral.NotChannelViceII"));
			return;
		}
		String targetuuid = Utility.convertNameToUUID(args[2]).toString();
		if(targetuuid == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		if(!cc.getBanned().contains(targetuuid))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCUnban.PlayerNotBanned")));
			return;
		}
		cc.removeBanned(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCUnban.YouUnbanPlayer")
				.replace("%player%", args[2])));
		if(ProxyServer.getInstance().getPlayer(args[2]) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCUnban.CreatorUnbanPlayer")
					.replace("%player%", args[2])
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
				members.sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCUnban.CreatorUnbanPlayer")
						.replace("%player%", args[2])
						.replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}