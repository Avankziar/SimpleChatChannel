package main.java.me.avankziar.scc.velocity.commands;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.handler.ChatHandler;

public class WCommandExecutor implements SimpleCommand
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public WCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		WCommandExecutor.cc = cc;
	}
	
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) 
    {
        return CompletableFuture.completedFuture(new TabCompletion().getTabs(invocation.source(), cc.getName(), invocation.arguments()));
    }

    public void execute(final Invocation invocation) 
	{
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();
        
		if (sender instanceof Player) 
		{
			plugin.getLogger().info("/%cmd% is only for Consol!".replace("%cmd%", cc.getName()));
			return;
		}
		if(args.length <= 1)
		{
			return;
		}
		String otherPlayer = args[0];
		Optional<Player> other = plugin.getServer().getPlayer(otherPlayer);
		if(other.isEmpty())
		{
			return;
		}
		String message = "";
		int i = 1;
		while(i < args.length)
		{
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
		}
		ChatHandler ch = new ChatHandler(plugin);
		ch.startPrivateConsoleChat(sender, other.get(), message);
	}
}