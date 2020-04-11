package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;

public class ARGCustomChannelChangePassword extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelChangePassword(SimpleChatChannels plugin)
	{
		super("ccchangepassword","scc.cmd.cc.changepassword",SimpleChatChannels.sccarguments,2,2,"ccpasswortändern");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"CustomChannelGeneral.NotInAChannel"));
			return;
		}
		Player creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"CustomChannelGeneral.NotTheCreator"));
			return;
		}
		cc.setPassword(args[1]);
		///Du hast das Passwort zu &f%password% &egeändert!
		player.spigot().sendMessage(plugin.getUtility().tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"CCChangepassword.PasswordChange")
				.replace("%password%", args[1])));
		return;
	}
}
