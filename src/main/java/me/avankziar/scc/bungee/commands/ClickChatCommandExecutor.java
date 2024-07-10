package main.java.me.avankziar.scc.bungee.commands;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClickChatCommandExecutor extends Command
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public ClickChatCommandExecutor(SCC plugin, CommandConstructor cc)
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
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
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
		t.sendMessage(ChatApiOld.clickHover(msg, "RUN_COMMAND", args[1],
				"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("CmdClickChat.ClickAnswer")
				.replace("%number%", args[1])));
    	return;
	}

}
