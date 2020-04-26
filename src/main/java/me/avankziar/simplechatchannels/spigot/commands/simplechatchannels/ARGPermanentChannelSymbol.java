package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;

public class ARGPermanentChannelSymbol extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelSymbol(SimpleChatChannels plugin)
	{
		super("pcsymbol","scc.cmd.pc.symbol",SimpleChatChannels.sccarguments,3,3);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotTheCreatorII"));
			return;
		}
		String symbol = args[2];
		PermanentChannel other = PermanentChannel.getChannelFromSymbol(symbol);
		if(other != null)
		{
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"PCSymbol.SymbolAlreadyExist")
					.replace("%symbol%", other.getSymbolExtra())
					.replace("%channel%", other.getNameColor()+other.getName())));
			return;
		}
		cc.setSymbolExtra(symbol);
		plugin.getUtility().updatePermanentChannels(cc);
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.spigot().sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+"PCSymbol.NewSymbol")
						.replace("%symbol%", plugin.getYamlHandler().getSymbol("Perma")+cc.getSymbolExtra())
						.replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}