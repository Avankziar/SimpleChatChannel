package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Invite extends ArgumentModule
{
	private SCC plugin;
	private LinkedHashMap<ProxiedPlayer, Long> inviteCooldown;
	
	public ARGPermanentChannel_Invite(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
		inviteCooldown = new LinkedHashMap<>();
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice")
					.replace("%channel%", cc.getName())));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis() < inviteCooldown.get(player))
		{
			
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.Cooldown")
					.replace("%time%", TimeHandler.getDateTime(inviteCooldown.get(player)))));
			return;
		}
		String t = args[3];
		if(ProxyServer.getInstance().getPlayer(t) == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(t);
		String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_PC_JOIN)+cc.getName();
		if(cc.getPassword() != null)
		{
			cmd += " "+cc.getPassword();
		}
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.SendInvite")
				.replace("%target%", target.getName()).replace("%channel%", cc.getNameColor()+cc.getName())));
		target.sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.Invitation")
				.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName()), 
				"RUN_COMMAND", cmd));
		if(inviteCooldown.containsKey(player))
		{
			inviteCooldown.replace(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("PermanentChannel.InviteCooldown", 60));
		} else
		{
			inviteCooldown.put(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("PermanentChannel.InviteCooldown", 60));
		}
	}
}