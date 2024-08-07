package main.java.me.avankziar.scc.spigot.commands.scc.pc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

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
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwner")));
			return;
		}
		String symbol = args[3];
		PermanentChannel other = PermanentChannel.getChannelFromSymbol(symbol);
		if(other != null)
		{
			player.spigot().sendMessage(ChatApi.tctl(
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
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.spigot().sendMessage(ChatApi.tctl(msg));
			}
		}
	}
}