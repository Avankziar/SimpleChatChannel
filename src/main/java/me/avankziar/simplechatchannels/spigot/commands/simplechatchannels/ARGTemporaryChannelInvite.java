package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGTemporaryChannelInvite extends CommandModule
{
	private SimpleChatChannels plugin;
	private LinkedHashMap<Player, Long> inviteCooldown;
	
	public ARGTemporaryChannelInvite(SimpleChatChannels plugin)
	{
		super("tcinvite","scc.cmd.tc.invite",SimpleChatChannels.sccarguments,2,2,"tceinladen");
		this.plugin = plugin;
		inviteCooldown = new LinkedHashMap<>();
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotInAChannel"));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis()<inviteCooldown.get(player))
		{
			player.spigot().sendMessage(utility.tctlYaml(language+scc+"TCInvite.Cooldown"));
			return;
		}
		String t = args[1];
		if(plugin.getServer().getPlayer(t) == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+scc+"NoPlayerExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(t);
		String cmd = "/scc tcjoin "+cc.getName();
		if(cc.getPassword()!=null)
		{
			cmd += " "+cc.getPassword();
		}
		player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+scc+"TCInvite.spigot().sendInvite")
				.replace("%target%", target.getName()).replace("%channel%", cc.getName())));
		target.spigot().sendMessage(utility.clickEvent(plugin.getYamlHandler().getL().getString(language+scc+"TCInvite.Invitation")
				.replace("%player%", player.getName()).replace("%channel%", cc.getName()), ClickEvent.Action.RUN_COMMAND, cmd, false));
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
