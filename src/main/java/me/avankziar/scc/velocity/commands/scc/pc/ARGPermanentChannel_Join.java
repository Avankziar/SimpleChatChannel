package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGPermanentChannel_Join extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Join(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String name = null;
		String password = null;
		if(args.length >= 3)
		{
			name = args[2];
		}
		if(args.length == 4)
		{
			password = args[3];
		}
		PermanentChannel cc = PermanentChannel.getChannelFromName(name);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Join.UnknownChannel")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Join.Banned")));
			return;
		}
		if(cc.getMembers().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Join.AlreadyInTheChannel")));
			return;
		}
		if(password == null && cc.getPassword() != null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Join.ChannelHasPassword")));
			return;
		} else
		{
			if(cc.getPassword() != null 
					&& password != null
					&& !cc.getPassword().equals(password))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Join.PasswordIncorrect")));
				return;
			}
		}
		cc.addMembers(player.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		
		Channel c = plugin.getChannel("Permanent");
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Join.ChannelJoined")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%symbol%", (c != null) ? c.getSymbol() : ""+cc.getSymbolExtra())));
		
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Join.PlayerIsJoined")
				.replace("%player%", player.getUsername())
				.replace("%channel%", cc.getNameColor()+cc.getName());
		for(Player members : plugin.getServer().getAllPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApi.tl(msg));
			}
		}
	}
}