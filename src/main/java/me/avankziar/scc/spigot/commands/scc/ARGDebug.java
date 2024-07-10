package main.java.me.avankziar.scc.spigot.commands.scc;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer;

public class ARGDebug extends ArgumentModule
{
	private SCC plugin;
	
	public ARGDebug(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(args.length <= 1)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.PleaseEnterAMessage")));
			return;
		}
		String message = "";
		int i = 1;
		while(i < args.length)
		{
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
			i++;
		}
		ItemStack item = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(BukkitComponentSerializer.gson().serialize(ChatApi.tl("&aDas &7eisener &6Schwert")));
		ArrayList<String> l = new ArrayList<>();
		//ArrayList<Component> c = new ArrayList<>();
		l.add(BukkitComponentSerializer.gson().serialize(ChatApi.tl("&7Geschmiedet in einer Schmiede Arnors")));//Dont work
		l.add(ChatApi.oldBukkitFormat("&bBlub"));
		im.setLore(l);
		item.setItemMeta(im);
		player.getInventory().addItem(item);
		player.sendMessage(message);
	}
	
	/*private String getItem(ItemStack item)
	{
		if (item.getType().equals(Material.AIR)) 
		{
	        return "[]";
	    }
	    else {
	        String itemFormat = String.format("\"%s\":%d", item.getType().getKey(), item.getAmount());
	        String itemName = item.hasItemMeta() ? 
		    		(item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().toString()) 
		    		: item.getType().toString();
	        if (item.hasItemMeta()) 
	        {
	            itemFormat = String.format("%s:'%s'", itemFormat, item.getItemMeta().getAsString());
	        }
	        return String.format("<hover:show_item:%s>%s</hover>", itemFormat, itemName);
	    }
	}*/
}