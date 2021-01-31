package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String scc = "CmdScc.";
		PermanentChannel cc = null;
		if(args.length == 1)
		{
			cc = PermanentChannel.getChannelFromPlayer(player.getUniqueId().toString());
			if(cc==null)
			{
				///Du bist in keinem CustomChannel!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.NotInAChannelII")));
				return;
			}
		} else
		{
			cc = PermanentChannel.getChannelFromName(args[1]);
			if(cc==null)
			{
				///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.ChannelNotExistII")
						.replace("%channel%", args[1])));
				return;
			}
		}
		
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Headline")
				.replace("%channel%", cc.getNameColor()+cc.getName())));
		///Channel Ersteller: &f%creator%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.ID")
				.replace("%id%", cc.getId()+"")));
		///Channel Ersteller: &f%creator%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Creator")
				.replace("%creator%", getPlayer(cc.getCreator()))));
		///Channel Stellvertreter: &f%vice%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Vice")
				.replace("%amount%", String.valueOf(cc.getVice().size()))
				.replace("%vice%", getPlayers(cc.getVice()))));
		///Channel Mitglieder: &f%members%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Members")
				.replace("%amount%", String.valueOf(cc.getMembers().size()))
				.replace("%members%", getPlayers(cc.getMembers()))));
		if(cc.getPassword()!=null)
		{
			if(cc.getCreator().equals(player.getUniqueId().toString())
					|| cc.getVice().contains(player.getUniqueId().toString())
					|| player.hasPermission(Utility.PERMBYPASSPCDELETE))
			{
				///Channel Passwort: &f%password%
				player.sendMessage(ChatApi.tctl(
						plugin.getYamlHandler().getL().getString(scc+"PCInfo.Password")
						.replace("%password%", cc.getPassword())));
			}
		}
		///Channel Symbol: &f%symbol%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Symbol")
				.replace("%symbol%", plugin.getYamlHandler().getSymbol("Perma")+cc.getSymbolExtra())));
		///Channel NamenFarben: &f%color%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.NameColor")
				.replace("%color%", cc.getNameColor()+cc.getName())));
		///Channel ChatFarben: &f%color%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.ChatColor")
				.replace("%color%", cc.getChatColor())));
		///Channel Gebannte Spieler: &f%banned%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCInfo.Banned")
				.replace("%amount%", String.valueOf(cc.getBanned().size()))
				.replace("%banned%", getPlayers(cc.getBanned()))));
		return;
	}
	
	private String getPlayer(String uuid)
	{
		return (ChatUserHandler.getChatUser(UUID.fromString(uuid)) != null) ? ChatUserHandler.getChatUser(UUID.fromString(uuid)).getName() : uuid;
	}
	
	private String getPlayers(ArrayList<String> uuids)
	{
		String s = "[";
		if(!uuids.isEmpty())
		{
			for(int i = 0; i < uuids.size(); i++)
			{
				String uuid = uuids.get(i);
				try
				{
					ChatUser cuu = ChatUserHandler.getChatUser(UUID.fromString(uuid));
					if(cuu != null && !uuid.equals("null"))
					{
						if(uuids.size()-1 == i)
						{
							s += cuu.getName();
						} else
						{
							s += cuu.getName()+", ";
						}
					}
				} catch(IllegalArgumentException e)
				{
					continue;
				}
			}
		}
		s += "]";
		return s;
	}
}