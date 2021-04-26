package main.java.me.avankziar.scc.bungee.commands;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClickChatCommandExecutor extends Command
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public ClickChatCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		super(cc.getName(),null);
		this.plugin = plugin;
		ClickChatCommandExecutor.cc = cc;
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if(!(sender instanceof ProxiedPlayer))
    	{
    		return;
    	}	
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if(cc.getPermission() != null)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
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
		t.sendMessage(ChatApi.apiChat(msg, ClickEvent.Action.RUN_COMMAND, args[1],
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("CmdClickChat.ClickAnswer")
				.replace("%number%", args[1])));
    	return;
	}

}
