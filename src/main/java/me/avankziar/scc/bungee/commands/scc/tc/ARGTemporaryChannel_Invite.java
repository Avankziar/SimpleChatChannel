package main.java.me.avankziar.scc.bungee.commands.scc.tc;

import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannel_Invite extends ArgumentModule
{
	private SCC plugin;
	private LinkedHashMap<ProxiedPlayer, Long> inviteCooldown;
	
	public ARGTemporaryChannel_Invite(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
		inviteCooldown = new LinkedHashMap<>();
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis()<inviteCooldown.get(player))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.Cooldown")));
			return;
		}
		String t = args[2];
		if(ProxyServer.getInstance().getPlayer(t) == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(t);
		String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_TC_JOIN)+cc.getName();
		if(cc.getPassword() != null)
		{
			cmd += " "+cc.getPassword();
		}
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.SendInvite")
				.replace("%target%", target.getName()).replace("%channel%", cc.getName())));
		target.sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.Invitation")
				.replace("%player%", player.getName()).replace("%channel%", cc.getName()), "RUN_COMMAND", cmd));
		if(inviteCooldown.containsKey(player))
		{
			inviteCooldown.replace(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("TemporaryChannel.InviteCooldown", 60));
		} else
		{
			inviteCooldown.put(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("TemporaryChannel.InviteCooldown", 60));
		}
		return;
	}
}
