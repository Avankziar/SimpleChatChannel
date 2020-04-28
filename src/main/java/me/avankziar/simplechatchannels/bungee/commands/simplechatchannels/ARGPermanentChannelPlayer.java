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
		super("pcplayer","scc.cmd.pc.player",SimpleChatChannels.sccarguments,1,2,"pcspieler",
				"[Spieler]".split(";"));
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		String uuid = "";
		String name = "";
		if(args.length == 1)
		{
			uuid = player.getUniqueId().toString();
			name = player.getName();
		} else
		{
			if(!player.hasPermission(Utility.PERMBYPASSPCDELETE))
			{
				///Du hast dafür keine Rechte!
				player.sendMessage(plugin.getUtility().tctlYaml(language+".CmdScc.NoPermission"));
				return;
			}
			uuid = (String) plugin.getMysqlHandler().getDataI(args[1], "player_uuid", "player_name");
			name = args[1];
		}
		int creators = 0;
		int vices = 0;
		int members = 0;
		int banneds = 0;
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
				creators++;
			}
			if(cc.getVice().contains(uuid))
			{
				vices++;
			}
			if(cc.getMembers().contains(uuid))
			{
				members++;
			}
			if(cc.getBanned().contains(uuid))
			{
				banneds++;
			}
		}
		if(creators>0)
		{
			for(int i = 0; i < pc.size(); i++)
			{
				PermanentChannel cc = pc.get(i);
				if(cc.getCreator().equals(uuid))
				{
					creators--;
					if(creators == 0)
					{
						creator +=  pc.get(i).getNameColor()+pc.get(i).getName();
					} else
					{
						creator +=  pc.get(i).getNameColor()+pc.get(i).getName()+"&r,";
					}
				}
			}
		}
		creator += "&r]";
		if(vices>0)
		{
			for(int i = 0; i < pc.size(); i++)
			{
				PermanentChannel cc = pc.get(i);
				if(cc.getVice().contains(uuid))
				{
					vices--;
					if(vices == 0)
					{
						vice +=  pc.get(i).getNameColor()+pc.get(i).getName();
					} else
					{
						vice +=  pc.get(i).getNameColor()+pc.get(i).getName()+"&r,";
					}
					
				}
			}
		}
		vice += "&r]";
		if(members>0)
		{
			for(int i = 0; i < pc.size(); i++)
			{
				PermanentChannel cc = pc.get(i);
				if(cc.getMembers().contains(uuid))
				{
					members--;
					if(members == 0)
					{
						member +=  pc.get(i).getNameColor()+pc.get(i).getName();
					} else
					{
						member +=  pc.get(i).getNameColor()+pc.get(i).getName()+"&r,";
					}
					
				}
			}
		}
		member += "&r]";
		if(banneds>0)
		{
			for(int i = 0; i < pc.size(); i++)
			{
				PermanentChannel cc = pc.get(i);
				if(cc.getBanned().contains(uuid))
				{
					banneds--;
					if(banneds == 1)
					{
						banned +=  pc.get(i).getNameColor()+pc.get(i).getName();
					} else
					{
						banned +=  pc.get(i).getNameColor()+pc.get(i).getName()+"&r,";
					}
				}
			}
		}
		banned += "&r]";
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Headline")
				.replace("%player%", name)));
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