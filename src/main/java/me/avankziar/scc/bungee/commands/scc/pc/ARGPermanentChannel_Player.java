package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.BypassPermission;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Player extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Player(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String uuid = "";
		String name = "";
		if(args.length == 2)
		{
			uuid = player.getUniqueId().toString();
			name = player.getName();
		} else
		{
			if(!player.hasPermission(BypassPermission.PERMBYPASSPC))
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
			UUID u = Utility.convertNameToUUID(args[2]);
			if(u == null)
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
				return;
			}
			uuid = u.toString();
			name = args[2];
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
		if(members > 0)
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
		player.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Headline")
				.replace("%player%", name)));
		player.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Creator")
				.replace("%creator%", creator)));
		player.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Vice")
				.replace("%vice%", vice)));
		player.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Member")
				.replace("%member%", member)));
		player.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Banned")
				.replace("%banned%", banned)));
	}
}