package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.KeyHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.PluginSettings;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Invite extends ArgumentModule
{
	private SimpleChatChannels plugin;
	private LinkedHashMap<ProxiedPlayer, Long> inviteCooldown;
	
	public ARGPermanentChannel_Invite(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice")
					.replace("%channel%", cc.getName())));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis()<inviteCooldown.get(player))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.Cooldown")));
			return;
		}
		String t = args[2];
		if(ProxyServer.getInstance().getPlayer(t) == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(t);
		String cmd = PluginSettings.settings.getCommands(KeyHandler.SCC_PC_JOIN)+cc.getName();
		if(cc.getPassword()!=null)
		{
			cmd += " "+cc.getPassword();
		}
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.SendInvite")
				.replace("%target%", target.getName()).replace("%channel%", cc.getNameColor()+cc.getName())));
		target.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Invite.Invitation")
				.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName()), 
				ClickEvent.Action.RUN_COMMAND, cmd));
		if(inviteCooldown.containsKey(player))
		{
			inviteCooldown.replace(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("PermanentChannel.InviteCooldown", 60));
		} else
		{
			inviteCooldown.put(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().getConfig().getInt("PermanentChannel.InviteCooldown", 60));
		}
	}
}