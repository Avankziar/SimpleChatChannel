package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelPlayer extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelPlayer(SimpleChatChannels plugin)
	{
		super("pcplayer","scc.cmd.pc.player",SimpleChatChannels.sccarguments,1,2,"pcspieler");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) //TODO argument unterschied einbauen, um sich selbst anzuzeigen
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		if(plugin.getMysqlHandler().getDataI(args[1], "player_name", "player_name") == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(utility.tctlYaml(language+scc+"NoPlayerExist"));
			return;
		}
		String uuid = "";
		if(args.length == 1)
		{
			uuid = player.getUniqueId().toString();
		} else
		{
			if(!player.hasPermission(Utility.PERMBYPASSPCDELETE))
			{
				///Du hast dafür keine Rechte!
				player.sendMessage(plugin.getUtility().tctlYaml(language+".CmdScc.NoPermission"));
				return;
			}
			uuid = (String) plugin.getMysqlHandler().getDataI(args[1], "player_uuid", "player_name");
		} 
		String creator = "&r[";
		String vice = "&r[";
		String member = "&r[";
		String banned = "&r[";
		final ArrayList<PermanentChannel> pc = PermanentChannel.getPermanentChannel();
		for(int i = 0; i < pc.size(); i++)
		{
			PermanentChannel cc = pc.get(i);
			if(cc.getCreator().equals(uuid))
			{
				creator +=  pc.get(i).getNameColor()+pc.get(i).getName()+"&r,";
			}
		}
		if(creator.length()>3)
		{
			creator.substring(0, creator.length()-1);
		}
		creator += "&r]";
		for(int i = 0; i < pc.size(); i++)
		{
			PermanentChannel cc = pc.get(i);
			if(cc.getVice().contains(uuid))
			{
				vice +=  pc.get(i).getNameColor()+pc.get(i).getName()+"&r,";
			}
		}
		if(vice.length()>3)
		{
			vice.substring(0, vice.length()-1);
		}
		vice += "&r]";
		for(int i = 0; i < pc.size(); i++)
		{
			PermanentChannel cc = pc.get(i);
			if(cc.getMembers().contains(uuid))
			{
				member +=  pc.get(i).getNameColor()+pc.get(i).getName()+"&r,";
			}
		}
		if(member.length()>3)
		{
			member.substring(0, member.length()-1);
		}
		member += "&r]";
		for(int i = 0; i < pc.size(); i++)
		{
			PermanentChannel cc = pc.get(i);
			if(cc.getBanned().contains(uuid))
			{
				banned +=  pc.get(i).getNameColor()+pc.get(i).getName()+"&r,";
			}
		}
		if(banned.length()>3)
		{
			banned.substring(0, banned.length()-1);
		}
		banned += "&r]";
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Headline")
				.replace("%player%", args[1])));
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Creator")
				.replace("%creator%", creator)));
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Vice")
				.replace("%vice%", vice)));
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Member")
				.replace("%member%", member)));
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Banned")
				.replace("%banned%", banned)));
		return;
	}
}