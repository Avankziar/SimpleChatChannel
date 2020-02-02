package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EVENTTabComplete implements Listener
{
	private List<String> admin = new ArrayList<>(); //alle
	private List<String> player = new ArrayList<>(); //nur die für spieler
	private List<String> wordfilter = new ArrayList<>();
	
	public EVENTTabComplete()
	{
		init();
	}
	
	@EventHandler
	public void onTab(TabCompleteEvent event)
	{
		String c = event.getCursor();
		if(c.equalsIgnoreCase("/scc wordfilter "))
		{
			event.getSuggestions().clear();
			event.getSuggestions().addAll(wordfilter);
		} else if(c.equalsIgnoreCase("/scc "))
		{
			event.getSuggestions().clear();
			ProxiedPlayer pp = (ProxiedPlayer) event.getSender();
			if(pp.hasPermission("scc.admin"))
			{
				event.getSuggestions().addAll(admin);
			} else
			{
				event.getSuggestions().addAll(player);
			}
		}
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
