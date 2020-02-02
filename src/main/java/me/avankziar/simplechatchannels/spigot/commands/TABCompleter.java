package main.java.me.avankziar.simplechatchannels.spigot.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TABCompleter implements TabCompleter
{
	private List<String> admin = new ArrayList<>(); //alle
	private List<String> player = new ArrayList<>(); //nur die für spieler
	private List<String> wordfilter = new ArrayList<>();
	
	public TABCompleter()
	{
		init();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		Player player = (Player)sender;
		if(args.length==1)
		{
			if(player.hasPermission("scc.admin"))
			{
				return this.admin;
			} else
			{
				return this.player;
			}
		} else if(args.length==2 && args[0].equalsIgnoreCase("wordfilter"))
		{
			return this.wordfilter;
		}
		return null;
	}
	
	public void init()
	{
		admin.add("global");
		admin.add("trade");
		admin.add("support");
		admin.add("auction");
		admin.add("local");
		admin.add("world");
		admin.add("team");
		admin.add("admin");
		admin.add("custom");
		admin.add("pm");
		admin.add("spy");
		admin.add("joinmessage");
		admin.add("ignorelist");
		admin.add("bungee");
		admin.add("mute");
		admin.add("unmute");
		admin.add("ignore");
		admin.add("wordfilter");
		admin.add("broadcast");
		admin.add("cccreate");
		admin.add("ccjoin");
		admin.add("ccleave");
		admin.add("ccinfo");
		admin.add("cckick");
		admin.add("ccban");
		admin.add("ccunban");
		admin.add("ccchangepassword");
		admin.add("playerlist");
		admin.add("grouplist");
		Collections.sort(admin, String.CASE_INSENSITIVE_ORDER);
		
		player.add("global");
		player.add("trade");
		player.add("support");
		player.add("auction");
		player.add("local");
		player.add("world");
		player.add("team");
		player.add("custom");
		player.add("pm");
		player.add("joinmessage");
		player.add("ignorelist");
		player.add("ignore");
		player.add("cccreate");
		player.add("ccjoin");
		player.add("ccleave");
		player.add("ccinfo");
		player.add("cckick");
		player.add("ccban");
		player.add("ccunban");
		player.add("ccchangepassword");
		Collections.sort(player, String.CASE_INSENSITIVE_ORDER);
		
		wordfilter.add("add");
		wordfilter.add("remove");
		Collections.sort(wordfilter, String.CASE_INSENSITIVE_ORDER);
	}
}
