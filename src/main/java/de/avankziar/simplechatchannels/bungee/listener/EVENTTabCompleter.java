package main.java.de.avankziar.simplechatchannels.bungee.listener;

import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EVENTTabCompleter implements Listener
{	
	@EventHandler
	public void onTabComplete(TabCompleteEvent event)
	{
		if(event.getCursor().startsWith("/scc "))
		{
			event.getSuggestions().add("");
		}
	}
}
