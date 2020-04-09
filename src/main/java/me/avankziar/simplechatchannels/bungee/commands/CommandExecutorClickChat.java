package main.java.me.avankziar.simplechatchannels.bungee.commands;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
    	TextComponent msg1 = plugin.getUtility().tcl(msg);
		msg1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
				new ComponentBuilder(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".CMDClickChat.msg01")
						.replace("%number%", args[1]))).create()));
		msg1.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND,args[1]));
    	t.sendMessage(msg1);
    	return;
	}

}
