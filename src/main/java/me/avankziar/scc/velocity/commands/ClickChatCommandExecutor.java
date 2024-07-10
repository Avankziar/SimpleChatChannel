package main.java.me.avankziar.scc.velocity.commands;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.velocity.SCC;

public class ClickChatCommandExecutor implements SimpleCommand
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public ClickChatCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		ClickChatCommandExecutor.cc = cc;
	}

	public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) 
    {
        return CompletableFuture.completedFuture(new TabCompletion().getTabs(invocation.source(), cc.getName(), invocation.arguments()));
    }
	
	public void execute(final Invocation invocation) 
	{
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();
        
        Player player = null;
        if(sender instanceof Player)
        {
        	player = (Player) sender;
        }
		if(cc.getPermission() != null)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
		}
    	if(args.length<3)
    	{
    		return;
    	}
    	if(plugin.getServer().getPlayer(args[0]).isEmpty())
    	{
    		return;
    	}
    	Player t = plugin.getServer().getPlayer(args[0]).get();	
    	String msg = "";
    	for(int i = 2;i < args.length;i++)
    	{
    		msg += args[i]+" "; 
    	}
    	///Klicke hier um die %number%.te Antwortmöglichkeit zu nehmen!
		t.sendMessage(ChatApi.tl(ChatApi.clickHover(msg, "RUN_COMMAND", args[1],
				"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("CmdClickChat.ClickAnswer")
				.replace("%number%", args[1]))));
    	return;
	}

}
