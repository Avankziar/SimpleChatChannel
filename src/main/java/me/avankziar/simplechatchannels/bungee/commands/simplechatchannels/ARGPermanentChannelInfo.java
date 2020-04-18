package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelInfo extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelInfo(SimpleChatChannels plugin)
	{
		super("pcinfo","scc.cmd.pc.info",SimpleChatChannels.sccarguments,1,1);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromPlayer(player.getUniqueId().toString());
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotInAChannelII"));
			return;
		}
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCInfo.Headline")
				.replace("%channel%", cc.getName())));
		///Channel Ersteller: &f%creator%
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCInfo.Creator")
				.replace("%creator%", getPlayer(cc.getCreator()))));
		///Channel Stellvertreter: &f%vice%
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCInfo.Vice")
				.replace("%vice%", getPlayers(cc.getVice()))));
		///Channel Mitglieder: &f%members%
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCInfo.Members")
				.replace("%members%", getPlayers(cc.getMembers()))));
		if(cc.getPassword()!=null)
		{
			///Channel Passwort: &f%password%
			player.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"PCInfo.Password")
					.replace("%password%", cc.getPassword())));
		}
		///Channel Gebannte Spieler: &f%banned%
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCInfo.Banned")
				.replace("%banned%", getPlayers(cc.getBanned()))));
		return;
	}
	
	private String getPlayer(String uuid)
	{
		return (String) plugin.getMysqlHandler().getDataI(uuid, "player_name", "player_uuid");
	}
	
	private String getPlayers(ArrayList<String> uuids)
	{
		String s = "[";
		for(String uuid : uuids)
		{
			s += (String) plugin.getMysqlHandler().getDataI(uuid, "player_name", "player_uuid")+",";
		}
		s += "]";
		return s;
	}
}