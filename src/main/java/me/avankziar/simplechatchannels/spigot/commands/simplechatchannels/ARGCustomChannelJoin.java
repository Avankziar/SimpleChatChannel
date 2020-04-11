package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;

public class ARGCustomChannelJoin extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelJoin(SimpleChatChannels plugin)
	{
		super("ccjoin","scc.cmd.cc.join",SimpleChatChannels.sccarguments,1,1,"ccbeitreten");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage() + ".CmdScc.";
		String name = null;
		String password = null;
		if(args.length==2)
		{
			name = args[1];
		} else if(args.length==3)
		{
			name = args[1];
			password = args[2];
		} else
		{
			plugin.getUtility().rightArgs(player,args,3);
			return;
		}
		CustomChannel cc = CustomChannel.getCustomChannel(name);
		CustomChannel oldcc = CustomChannel.getCustomChannel(player);
		if(oldcc!=null)
		{
			///Du bist schon in einem anderen Channel gejoint, verlasse erst diesen!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CCJoin.AlreadyInAChannel"));
			return;
		}
		if(cc==null)
		{
			///Es gibt keinen CustomChannel mit dem Namen &f%name%&c!
			player.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"CCJoin.UnknowChannel")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player))
		{
			///Du bist in diesem CustomChannel gebannt und darfst nicht joinen!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CCJoin.Banned"));
			return;
		}
		if(password==null)
		{
			if(cc.getPassword()!=null)
			{
				///Der CustomChannel hat ein Passwort, bitte gebe die beim Joinen an!
				player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CCJoin.ChannelHasPassword"));
				return;
			}
		} else
		{
			if(cc.getPassword()==null)
			{
				///Es ist kein Passwort angegeben, du kannst so joinen!
				player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CCJoin.ChannelHasNoPassword"));
				return;
			}
			if(!cc.getPassword().equals(password))
			{
				///Das angegebene Passwort ist nicht korrekt!
				player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CCJoin.PasswordIncorrect"));
				return;
			}
		}
		cc.addMembers(player);
		///Du bist dem CustomChannel &f%channel% &agejoint!
		player.spigot().sendMessage(plugin.getUtility().tctl(
				plugin.getYamlHandler().getL().getString(language+"CCJoin.ChannelJoined")
				.replace("%channel%", cc.getName())));
		return;
	}
}