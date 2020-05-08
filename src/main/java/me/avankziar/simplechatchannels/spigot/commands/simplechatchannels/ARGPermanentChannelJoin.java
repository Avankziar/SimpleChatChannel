package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;

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
		Player player = (Player) sender;
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
			player.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCJoin.UnknowChannel")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player.getUniqueId().toString()))
		{
			///Du bist in diesem CustomChannel gebannt und darfst nicht joinen!
			player.spigot().sendMessage(utility.tctlYaml(language+"PCJoin.Banned"));
			return;
		}
		if(cc.getMembers().contains(player.getUniqueId().toString()))
		{
			///Du bist schon diesem permanenten Channel beigetreten!
			player.spigot().sendMessage(utility.tctlYaml(language+"PCJoin.AlreadyInTheChannel"));
			return;
		}
		if(password==null)
		{
			if(cc.getPassword()!=null)
			{
				///Der CustomChannel hat ein Passwort, bitte gebe die beim Joinen an!
				player.spigot().sendMessage(utility.tctlYaml(language+"PCJoin.ChannelHasPassword"));
				return;
			}
		} else
		{
			if(cc.getPassword()==null)
			{
				///Es ist kein Passwort angegeben, du kannst so joinen!
				player.spigot().sendMessage(utility.tctlYaml(language+"PCJoin.ChannelHasNoPassword"));
				return;
			}
			if(!cc.getPassword().equals(password))
			{
				///Das angegebene Passwort ist nicht korrekt!
				player.spigot().sendMessage(utility.tctlYaml(language+"PCJoin.PasswordIncorrect"));
				return;
			}
		}
		cc.addMembers(player.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		
		///Du bist dem CustomChannel &f%channel% &agejoint!
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCJoin.ChannelJoined")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%symbol%", plugin.getYamlHandler().getSymbol("Perma")+cc.getSymbolExtra())));
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.spigot().sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+"PCJoin.PlayerIsJoined")
						.replace("%player%", player.getName())
						.replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}