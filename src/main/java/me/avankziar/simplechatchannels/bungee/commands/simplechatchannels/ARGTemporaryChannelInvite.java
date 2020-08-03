package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.LinkedHashMap;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelInvite extends ArgumentModule
{
	private SimpleChatChannels plugin;
	private LinkedHashMap<ProxiedPlayer, Long> inviteCooldown;
	
	public ARGTemporaryChannelInvite(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
		inviteCooldown = new LinkedHashMap<>();
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String scc = "CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.NotInAChannel")));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis()<inviteCooldown.get(player))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"TCInvite.Cooldown")));
			return;
		}
		String t = args[1];
		if(ProxyServer.getInstance().getPlayer(t) == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"NoPlayerExist")));
			return;
		}
		ProxiedPlayer target = ProxyServer.getInstance().getPlayer(t);
		String cmd = "/scc tcjoin "+cc.getName();
		if(cc.getPassword()!=null)
		{
			cmd += " "+cc.getPassword();
		}
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"TCInvite.SendInvite")
				.replace("%target%", target.getName()).replace("%channel%", cc.getName())));
		target.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString(scc+"TCInvite.Invitation")
				.replace("%player%", player.getName()).replace("%channel%", cc.getName()), ClickEvent.Action.RUN_COMMAND, cmd));
		if(inviteCooldown.containsKey(player))
		{
			inviteCooldown.replace(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().get().getInt("TCInviteCooldown", 60));
		} else
		{
			inviteCooldown.put(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().get().getInt("TCInviteCooldown", 60));
		}
		return;
	}
}
