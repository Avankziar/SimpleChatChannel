package main.java.me.avankziar.scc.velocity.commands.scc.tc;

import java.util.LinkedHashMap;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.PluginSettings;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;

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
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis()<inviteCooldown.get(player))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.Cooldown")));
			return;
		}
		String t = args[2];
		if(plugin.getServer().getPlayer(t).isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(t).get();
		String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_TC_JOIN)+cc.getName();
		if(cc.getPassword() != null)
		{
			cmd += " "+cc.getPassword();
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.SendInvite")
				.replace("%target%", target.getUsername()).replace("%channel%", cc.getName())));
		target.sendMessage(ChatApi.tl(ChatApi.click(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Invite.Invitation")
				.replace("%player%", player.getUsername()).replace("%channel%", cc.getName()), "RUN_COMMAND", cmd)));
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
