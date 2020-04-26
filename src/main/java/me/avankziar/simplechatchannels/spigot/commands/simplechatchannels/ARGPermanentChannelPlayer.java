package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;

public class ARGPermanentChannelPlayer extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelPlayer(SimpleChatChannels plugin)
	{
		super("pcplayer","scc.cmd.pc.player",SimpleChatChannels.sccarguments,1,2,"pcspieler");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
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
				player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+".CmdScc.NoPermission"));
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
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Headline")
				.replace("%player%", name)));
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Creator")
				.replace("%creator%", creator)));
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Vice")
				.replace("%vice%", vice)));
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Member")
				.replace("%member%", member)));
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCPlayer.Banned")
				.replace("%banned%", banned)));
		return;
	}
}