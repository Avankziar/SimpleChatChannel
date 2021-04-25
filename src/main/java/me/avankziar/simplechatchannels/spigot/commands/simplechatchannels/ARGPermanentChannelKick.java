package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
public class ARGPermanentChannelKick extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelKick(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = "CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
			return;
		}
		ChatUser cuoffline = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
				"`player_name` = ?", args[2]);
		if(cuoffline == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		String targetuuid = cuoffline.getUUID();
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du als Stellvertreter kannst den Ersteller nicht kicken!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCKick.ViceCannotKickCreator")));
			return;
		}
		if(!cc.getMembers().contains(targetuuid))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PermanentChannel.NotAChannelMember")));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCKick.CannotSelfKick")));
			return;
		}
		cc.removeMembers(targetuuid);
		cc.removeVice(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		if(plugin.getServer().getPlayer(args[2]) != null)
		{
			Player target = plugin.getServer().getPlayer(args[2]);
			///Du wurdest aus dem CustomChannel gekickt!
			target.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCKick.YouWereKicked")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCKick.YouKicked")
				.replace("%player%", args[2]).replace("%channel%", cc.getNameColor()+cc.getName())));
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
				members.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCKick.KickedSomeone")
						.replace("%player%", args[2]).replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}