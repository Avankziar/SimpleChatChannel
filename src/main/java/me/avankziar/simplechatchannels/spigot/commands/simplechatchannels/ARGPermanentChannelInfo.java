package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;

public class ARGPermanentChannelInfo extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelInfo(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String scc = "CmdScc.";
		PermanentChannel cc = null;
		if(args.length == 1)
		{
			cc = PermanentChannel.getChannelFromPlayer(player.getUniqueId().toString());
			if(cc==null)
			{
				///Du bist in keinem CustomChannel!
				player.spigot().sendMessage(
						ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.NotInAChannelII")));
				return;
			}
		} else
		{
			cc = PermanentChannel.getChannelFromName(args[1]);
			if(cc==null)
			{
				///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.ChannelNotExistII")
						.replace("%channel%", args[1])));
				return;
			}
		}
		
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Headline")
				.replace("%channel%", cc.getNameColor()+cc.getName())));
		///Channel Ersteller: &f%creator%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.ID")
				.replace("%id%", cc.getId()+"")));
		///Channel Ersteller: &f%creator%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Creator")
				.replace("%creator%", getPlayer(cc.getCreator()))));
		///Channel Stellvertreter: &f%vice%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Vice")
				.replace("%vice%", getPlayers(cc.getVice()))));
		///Channel Mitglieder: &f%members%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Members")
				.replace("%members%", getPlayers(cc.getMembers()))));
		if(cc.getPassword()!=null)
		{
			if(cc.getCreator().equals(player.getUniqueId().toString())
					|| cc.getVice().contains(player.getUniqueId().toString())
					|| player.hasPermission(Utility.PERMBYPASSPCDELETE))
			{
				///Channel Passwort: &f%password%
				player.spigot().sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(scc+"PCInfo.Password")
						.replace("%password%", cc.getPassword())));
			}
		}
		///Channel Symbol: &f%symbol%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Symbol")
				.replace("%symbol%", plugin.getYamlHandler().getSymbol("Perma")+cc.getSymbolExtra())));
		///Channel NamenFarben: &f%color%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.NameColor")
				.replace("%color%", cc.getNameColor()+cc.getName())));
		///Channel ChatFarben: &f%color%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.ChatColor")
				.replace("%color%", cc.getChatColor())));
		///Channel Gebannte Spieler: &f%banned%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Banned")
				.replace("%banned%", getPlayers(cc.getBanned()))));
		return;
	}
	
	private String getPlayer(String uuid)
	{
		ChatUser cu = ChatUser.getChatUser(UUID.fromString(uuid));
		return cu.getName();
	}
	
	private String getPlayers(ArrayList<String> uuids)
	{
		String s = "[";
		if(!uuids.isEmpty())
		{
			for(int i = 0; i < uuids.size(); i++)
			{
				String uuid = uuids.get(i);
				ChatUser cuu = ChatUser.getChatUser(UUID.fromString(uuid));
				if(!uuid.equals("null") 
						&& cuu != null)
				{
					if(uuids.size()-1 == i)
					{
						s += cuu.getName();
					} else
					{
						s += cuu.getName()+",";
					}
				}
			}
		}
		s += "]";
		return s;
	}
}