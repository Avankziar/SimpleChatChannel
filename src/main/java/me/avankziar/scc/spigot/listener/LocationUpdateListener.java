package main.java.me.avankziar.scc.spigot.listener;

import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import main.java.me.avankziar.scc.spigot.assistance.BackgroundTask;

public class LocationUpdateListener implements Listener
{
	private static LinkedHashMap<String, Long> cooldown = new LinkedHashMap<>();
	
	private void sendLocation(Player player)
	{
		if(cooldown.containsKey(player.getUniqueId().toString()))
		{
			if(cooldown.get(player.getUniqueId().toString()) > System.currentTimeMillis())
			{
				return;
			}
			BackgroundTask.sendLocationToBungee(player);
			cooldown.replace(player.getUniqueId().toString(), System.currentTimeMillis()+1000L*5);
		} else
		{
			BackgroundTask.sendLocationToBungee(player);
			cooldown.put(player.getUniqueId().toString(), System.currentTimeMillis()+1000L*5);
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		sendLocation(event.getPlayer());
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event)
	{
		sendLocation(event.getPlayer());
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event)
	{
		sendLocation(event.getPlayer());
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event)
	{
		sendLocation(event.getPlayer());
	}
	
	@EventHandler
	public void onPortal(PlayerPortalEvent event)
	{
		sendLocation(event.getPlayer());
	}
	
	@EventHandler
	public void onFlight(PlayerToggleFlightEvent event)
	{
		sendLocation(event.getPlayer());
	}
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event)
	{
		sendLocation(event.getPlayer());
	}
	
	@EventHandler
	public void onSprint(PlayerToggleSprintEvent event)
	{
		sendLocation(event.getPlayer());
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		sendLocation(event.getPlayer());
	}
}
