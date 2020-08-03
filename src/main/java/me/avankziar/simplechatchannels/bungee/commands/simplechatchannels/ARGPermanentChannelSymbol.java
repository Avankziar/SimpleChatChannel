package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelSymbol extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelSymbol(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
		String symbol = args[2];
		PermanentChannel other = PermanentChannel.getChannelFromSymbol(symbol);
		if(other != null)
		{
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCSymbol.SymbolAlreadyExist")
					.replace("%symbol%", other.getSymbolExtra())
					.replace("%channel%", other.getNameColor()+other.getName())));
			return;
		}
		cc.setSymbolExtra(symbol);
		plugin.getUtility().updatePermanentChannels(cc);
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCSymbol.NewSymbol")
						.replace("%symbol%", plugin.getYamlHandler().getSymbol("Perma")+cc.getSymbolExtra())
						.replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}