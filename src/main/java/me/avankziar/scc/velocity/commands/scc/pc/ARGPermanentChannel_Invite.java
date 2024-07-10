package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import java.util.LinkedHashMap;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.PluginSettings;

public class ARGPermanentChannel_Invite extends ArgumentModule
{
	private SCC plugin;
	private LinkedHashMap<Player, Long> inviteCooldown;
	
	public ARGPermanentChannel_Invite(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
		inviteCooldown = new LinkedHashMap<>();
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice")
					.replace("%channel%", cc.getName())));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis() < inviteCooldown.get(player))
		{
			
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.Cooldown")
					.replace("%time%", TimeHandler.getDateTime(inviteCooldown.get(player)))));
			return;
		}
		String t = args[3];
		if(plugin.getServer().getPlayer(t).isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(t).get();
		String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_PC_JOIN)+cc.getName();
		if(cc.getPassword() != null)
		{
			cmd += " "+cc.getPassword();
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.SendInvite")
				.replace("%target%", target.getUsername()).replace("%channel%", cc.getNameColor()+cc.getName())));
		target.sendMessage(ChatApi.tl(ChatApi.click(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.Invitation")
				.replace("%player%", player.getUsername()).replace("%channel%", cc.getNameColor()+cc.getName()), 
				"RUN_COMMAND", cmd)));
		if(inviteCooldown.containsKey(player))
		{
			inviteCooldown.replace(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("PermanentChannel.InviteCooldown", 60));
		} else
		{
			inviteCooldown.put(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("PermanentChannel.InviteCooldown", 60));
		}
	}
}