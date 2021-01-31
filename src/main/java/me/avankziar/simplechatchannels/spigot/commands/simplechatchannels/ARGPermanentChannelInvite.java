package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGPermanentChannelInvite extends ArgumentModule
{
	private SimpleChatChannels plugin;
	private LinkedHashMap<Player, Long> inviteCooldown;
	
	public ARGPermanentChannelInvite(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.NotChannelViceII")
					.replace("%channel%", cc.getName())));
			return;
		}
		if(inviteCooldown.containsKey(player) && System.currentTimeMillis()<inviteCooldown.get(player))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"PCInvite.Cooldown")));
			return;
		}
		String t = args[2];
		if(plugin.getServer().getPlayer(t) == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"NoPlayerExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(t);
		String cmd = "/scc pcjoin "+cc.getName();
		if(cc.getPassword()!=null)
		{
			cmd += " "+cc.getPassword();
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"PCInvite.spigot().sendInvite")
				.replace("%target%", target.getName()).replace("%channel%", cc.getNameColor()+cc.getName())));
		target.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString(scc+"PCInvite.Invitation")
				.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName()), 
				ClickEvent.Action.RUN_COMMAND, cmd));
		if(inviteCooldown.containsKey(player))
		{
			inviteCooldown.replace(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().get().getInt("PCInviteCooldown", 60));
		} else
		{
			inviteCooldown.put(player, System.currentTimeMillis()+1000L*plugin.getYamlHandler().get().getInt("PCInviteCooldown", 60));
		}
		return;
	}
}