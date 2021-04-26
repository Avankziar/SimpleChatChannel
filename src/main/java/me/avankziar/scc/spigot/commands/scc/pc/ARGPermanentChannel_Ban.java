package main.java.me.avankziar.scc.spigot.commands.scc.pc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;

public class ARGPermanentChannel_Ban extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_Ban(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc==null)
		{
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()) 
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice")
					.replace("%channel%", cc.getName())));
			return;
		}
		String target = args[3];
		ChatUser cut = ChatUserHandler.getChatUser(target);
		if(cut == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		String targetuuid = cut.getUUID();
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du als Stellvertreter kannst den Ersteller nicht ban!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Ban.ViceCannotBanCreator")));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Ban.OwnerCantSelfBan")));
			return;
		}
		if(cc.getBanned().contains(targetuuid))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Ban.AlreadyBanned")));
			return;
		}
		cc.addBanned(targetuuid);
		cc.removeVice(targetuuid);
		cc.removeMembers(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Ban.YouHasBanned")
				.replace("%player%", target)));
		Player t = plugin.getServer().getPlayer(target);
		if(t != null)
		{
			t.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Ban.YourWereBanned")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Ban.PlayerWasBanned")
				.replace("%player%", target);
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.spigot().sendMessage(ChatApi.tctl(msg));
			}
		}
	}
}