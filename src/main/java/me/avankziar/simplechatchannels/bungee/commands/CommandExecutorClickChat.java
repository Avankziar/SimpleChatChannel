package main.java.me.avankziar.simplechatchannels.bungee.commands;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandExecutorClickChat extends Command 
{
	private SimpleChatChannels plugin;
	
	public CommandExecutorClickChat(SimpleChatChannels plugin)
	{
		super("clch",null,"clickchat");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		String language = plugin.getUtility().getLanguage();
		ProxiedPlayer player = (ProxiedPlayer) sender;
		player.sendMessage(plugin.getUtility().tc(this.getName()));
		return;
	}

}
