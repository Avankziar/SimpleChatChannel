package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Symbol extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Symbol(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwner")));
			return;
		}
		String symbol = args[3];
		PermanentChannel other = PermanentChannel.getChannelFromSymbol(symbol);
		if(other != null)
		{
			player.sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Symbol.SymbolAlreadyExist")
					.replace("%symbol%", other.getSymbolExtra())
					.replace("%channel%", other.getNameColor()+other.getName())));
			return;
		}
		cc.setSymbolExtra(symbol);
		plugin.getUtility().updatePermanentChannels(cc);
		Channel c = plugin.getChannel("Permanent");
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Symbol.NewSymbol")
				.replace("%symbol%", c.getSymbol()+cc.getSymbolExtra())
				.replace("%channel%", cc.getNameColor()+cc.getName());
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApiOld.tctl(msg));
			}
		}
	}
}