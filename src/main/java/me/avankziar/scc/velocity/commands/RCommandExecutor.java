package main.java.me.avankziar.scc.velocity.commands;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.handler.ChatHandler;

public class RCommandExecutor implements SimpleCommand
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public RCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		RCommandExecutor.cc = cc;
	}
	
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) 
    {
        return CompletableFuture.completedFuture(new TabCompletion().getTabs(invocation.source(), cc.getName(), invocation.arguments()));
    }

    public void execute(final Invocation invocation) 
	{
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();
		if (!(sender instanceof Player)) 
		{
			plugin.getLogger().info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
			return;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(cc.getPermission()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return;
		}
		if(args.length <= 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMsg.PleaseEnterAMessage")));
			return;
		}
		if(!SCC.rPlayers.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMsg.YouHaveNoPrivateMessagePartner")));
			return;
		}
		Optional<Player> other = plugin.getServer().getPlayer(UUID.fromString(SCC.rPlayers.get(player.getUniqueId().toString())));
		if(other.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotOnline")));
			return;
		}
		String message = "";
		int i = 0;
		while(i < args.length)
		{
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
			i++;
		}
		ChatHandler ch = new ChatHandler(plugin);
		if(!ch.prePreCheck(player, message))
		{
			return;
		}
		ch.startPrivateChat(player, other.get(), message);
	}
}