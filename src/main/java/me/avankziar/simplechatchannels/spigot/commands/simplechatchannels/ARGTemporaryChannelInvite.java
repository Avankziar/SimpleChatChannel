package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.objects.TemporaryChannel;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGTemporaryChannelInvite extends ArgumentModule
{
	private SimpleChatChannels plugin;
	private LinkedHashMap<Player, Long> inviteCooldown;
	
	public ARGTemporaryChannelInvite(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
		inviteCooldown = new LinkedHashMap<>();
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String scc = "CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.NotInAChannel")));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis()<inviteCooldown.get(player))
		{
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"TCInvite.Cooldown")));
			return;
		}
		String t = args[1];
		if(plugin.getServer().getPlayer(t) == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"NoPlayerExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(t);
		String cmd = "/scc tcjoin "+cc.getName();
		if(cc.getPassword()!=null)
		{
			cmd += " "+cc.getPassword();
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"TCInvite.spigot().sendInvite")
				.replace("%target%", target.getName()).replace("%channel%", cc.getName())));
		target.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString(scc+"TCInvite.Invitation")
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
