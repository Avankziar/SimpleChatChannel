package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.objects.TemporaryChannel;

public class ARGTemporaryChannelJoin extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelJoin(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = "CmdScc.";
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
			utility.rightArgs(player,args,3);
			return;
		}
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(name);
		TemporaryChannel oldcc = TemporaryChannel.getCustomChannel(player);
		if(oldcc!=null)
		{
			///Du bist schon in einem anderen Channel gejoint, verlasse erst diesen!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCJoin.AlreadyInAChannel")));
			return;
		}
		if(cc==null)
		{
			///Es gibt keinen CustomChannel mit dem Namen &f%name%&c!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCJoin.UnknowChannel")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player))
		{
			///Du bist in diesem CustomChannel gebannt und darfst nicht joinen!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCJoin.Banned")));
			return;
		}
		if(password==null)
		{
			if(cc.getPassword()!=null)
			{
				///Der CustomChannel hat ein Passwort, bitte gebe die beim Joinen an!
				player.spigot().sendMessage(
						ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCJoin.ChannelHasPassword")));
				return;
			}
		} else
		{
			if(cc.getPassword()==null)
			{
				///Es ist kein Passwort angegeben, du kannst so joinen!
				player.spigot().sendMessage(
						ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCJoin.ChannelHasNoPassword")));
				return;
			}
			if(!cc.getPassword().equals(password))
			{
				///Das angegebene Passwort ist nicht korrekt!
				player.spigot().sendMessage(
						ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"TCJoin.PasswordIncorrect")));
				return;
			}
		}
		cc.addMembers(player);
		///Du bist dem CustomChannel &f%channel% &agejoint!
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCJoin.ChannelJoined")
				.replace("%channel%", cc.getName())));
		for(Player members : cc.getMembers())
		{
			members.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCJoin.PlayerIsJoined")
					.replace("%player%", player.getName())));
		}
		return;
	}
}
