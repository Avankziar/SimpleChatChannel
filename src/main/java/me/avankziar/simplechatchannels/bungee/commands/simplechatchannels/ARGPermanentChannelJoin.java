package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelJoin extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelJoin(SimpleChatChannels plugin)
	{
		super("pcjoin","scc.cmd.pc.join",SimpleChatChannels.sccarguments,2,3,"pcbeitreten");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		String name = null;
		String password = null;
		if(args.length==2)
		{
			name = args[1];
		} else if(args.length==3)
		{
			name = args[1];
			password = args[2];
		}
		PermanentChannel cc = PermanentChannel.getChannelFromName(name);
		if(cc==null)
		{
			///Es gibt keinen CustomChannel mit dem Namen &f%name%&c!
			player.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCJoin.UnknowChannel")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player.getUniqueId().toString()))
		{
			///Du bist in diesem CustomChannel gebannt und darfst nicht joinen!
			player.sendMessage(utility.tctlYaml(language+"PCJoin.Banned"));
			return;
		}
		if(password==null)
		{
			if(cc.getPassword()!=null)
			{
				///Der CustomChannel hat ein Passwort, bitte gebe die beim Joinen an!
				player.sendMessage(utility.tctlYaml(language+"PCJoin.ChannelHasPassword"));
				return;
			}
		} else
		{
			if(cc.getPassword()==null)
			{
				///Es ist kein Passwort angegeben, du kannst so joinen!
				player.sendMessage(utility.tctlYaml(language+"PCJoin.ChannelHasNoPassword"));
				return;
			}
			if(!cc.getPassword().equals(password))
			{
				///Das angegebene Passwort ist nicht korrekt!
				player.sendMessage(utility.tctlYaml(language+"PCJoin.PasswordIncorrect"));
				return;
			}
		}
		cc.addMembers(player.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		
		///Du bist dem CustomChannel &f%channel% &agejoint!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCJoin.ChannelJoined")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%symbol%", plugin.getYamlHandler().getSymbol("Perma")+cc.getSymbolExtra())));
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+"PCJoin.PlayerIsJoined")
						.replace("%player%", player.getName())));
			}
		}
		return;
	}
}