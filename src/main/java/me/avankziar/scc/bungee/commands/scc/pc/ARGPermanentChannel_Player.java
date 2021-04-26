package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import java.util.ArrayList;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.BypassPermission;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Player extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_Player(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
		if(args.length == 1)
		{
			uuid = player.getUniqueId().toString();
			name = player.getName();
		} else
		{
			if(!player.hasPermission(BypassPermission.PERMBYPASSPC))
			{
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
				return;
			}
			uuid = Utility.convertNameToUUID(args[1]).toString();
			if(uuid == null)
			{
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
				return;
			}
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
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Headline")
				.replace("%player%", name)));
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Creator")
				.replace("%creator%", creator)));
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Vice")
				.replace("%vice%", vice)));
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Member")
				.replace("%member%", member)));
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Player.Banned")
				.replace("%banned%", banned)));
	}
}