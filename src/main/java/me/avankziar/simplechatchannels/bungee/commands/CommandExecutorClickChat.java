package main.java.me.avankziar.simplechatchannels.bungee.commands;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
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
		if(!(sender instanceof ProxiedPlayer))
    	{
    		return;
    	}
    	if(args.length<3)
    	{
    		return;
    	}
    	if(ProxyServer.getInstance().getPlayer(args[0])==null)
    	{
    		return;
    	}
    	ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[0]);

    	String msg = "";
    	for(int i = 2;i < args.length;i++)
    	{
    		msg += args[i]+" "; 
    	}
    	///Klicke hier um die %number%.te Antwortmöglichkeit zu nehmen!
		t.sendMessage(plugin.getUtility().apichat(msg, ClickEvent.Action.RUN_COMMAND, args[1],
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(language+".CmdClickChat.ClickAnswer")));
    	return;
	}

}
