package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelVice extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelVice(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotTheCreatorII")));
			return;
		}
		if(plugin.getProxy().getPlayer(args[2])==null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[2]); 
		if(!cc.getMembers().contains(target.getUniqueId().toString()))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelMemberII")));
			return;
		}
		if(cc.getVice().contains(target.getUniqueId().toString()))
		{
			cc.getVice().remove(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					///Der Spieler &f%player% &ewurde zum Mitglied im &5perma&fnenten &eChannel &f%channel% &cdegradiert&e!
					members.sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString(language+"PCVice.Degraded")
							.replace("%player%", args[2]).replace("%channel%", cc.getNameColor()+cc.getName())));
				}
			}
		} else
		{
			cc.getVice().add(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					///Der Spieler &f%player% &ewurde zum Stellvertreter im &5perma&fnenten &eChannel &f%channel% &abefördert&e!
					members.sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString(language+"PCVice.Promoted")
							.replace("%player%", args[2]).replace("%channel%", cc.getNameColor()+cc.getName())));
				}
			}
		}
		return;
	}
}