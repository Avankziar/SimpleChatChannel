package main.java.me.avankziar.scc.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import main.java.me.avankziar.scc.spigot.assistance.BackgroundTask;

public class LocationUpdateListener implements Listener
{	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event)
	{
		BackgroundTask.sendLocationToBungee(event.getPlayer());
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event)
	{
		BackgroundTask.sendLocationToBungee(event.getPlayer());
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event)
	{
		BackgroundTask.sendLocationToBungee(event.getPlayer());
	}
	
	@EventHandler
	public void onPortal(PlayerPortalEvent event)
	{
		BackgroundTask.sendLocationToBungee(event.getPlayer());
	}
	
	@EventHandler
	public void onFlight(PlayerToggleFlightEvent event)
	{
		BackgroundTask.sendLocationToBungee(event.getPlayer());
	}
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent event)
	{
		BackgroundTask.sendLocationToBungee(event.getPlayer());
	}
	
	@EventHandler
	public void onSprint(PlayerToggleSprintEvent event)
	{
		BackgroundTask.sendLocationToBungee(event.getPlayer());
	}
}
