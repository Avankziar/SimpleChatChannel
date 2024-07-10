package main.java.me.avankziar.scc.spigot.commands.scc.tc;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;

public class ARGTemporaryChannel_Invite extends ArgumentModule
{
	private SCC plugin;
	private LinkedHashMap<Player, Long> inviteCooldown;
	
	public ARGTemporaryChannel_Invite(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
		inviteCooldown = new LinkedHashMap<>();
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis()<inviteCooldown.get(player))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.Cooldown")));
			return;
		}
		String t = args[2];
		if(plugin.getServer().getPlayer(t) == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(t);
		String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_TC_JOIN)+cc.getName();
		if(cc.getPassword() != null)
		{
			cmd += " "+cc.getPassword();
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.SendInvite")
				.replace("%target%", target.getName()).replace("%channel%", cc.getName())));
		target.spigot().sendMessage(ChatApi.tctl(ChatApi.click(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.Invitation")
				.replace("%player%", player.getName()).replace("%channel%", cc.getName()), "RUN_COMMAND", cmd)));
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
