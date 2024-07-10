package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGPermanentChannel_Symbol extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Symbol(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwner")));
			return;
		}
		String symbol = args[3];
		PermanentChannel other = PermanentChannel.getChannelFromSymbol(symbol);
		if(other != null)
		{
			player.sendMessage(ChatApi.tl(
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
		for(Player members : plugin.getServer().getAllPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApi.tl(msg));
			}
		}
	}
}