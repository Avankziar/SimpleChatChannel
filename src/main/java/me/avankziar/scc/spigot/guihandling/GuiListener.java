package main.java.me.avankziar.scc.spigot.guihandling;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.ItemJson;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.guihandling.events.BottomGuiClickEvent;
import main.java.me.avankziar.scc.spigot.guihandling.events.UpperGuiClickEvent;

public class GuiListener implements Listener
{
	private SimpleChatChannels plugin;
	
	public GuiListener(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
		InventarHandler.initEnchantments();
	}
	
	@EventHandler
	public void onBottomGui(BottomGuiClickEvent event)
	{
		if(!event.getPluginName().equals(GuiValues.PLUGINNAME))
		{
			return;
		}
		if(!event.getInventoryIdentifier().equals(GuiValues.ITEM_REPLACER_INVENTORY))
		{
			return;
		}
		Inventory inv = event.getEvent().getView().getTopInventory();
		ItemStack clicked = event.getEvent().getCurrentItem().clone();
		Player player = (Player) event.getEvent().getWhoClicked();
		if(InventarHandler.isSimilarShort(clicked, inv.getContents()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdAsh.InventoryClick.ItemExist")));
			return;
		}
		if(InventarHandler.isFull(inv))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdAsh.InventoryClick.InventoryFull")));
			return;
		}
		inv.addItem(clicked);
		ItemJson ij = new ItemJson(player.getUniqueId().toString(), 
				String.valueOf(event.getEvent().getSlot()),
				(clicked.hasItemMeta()) ? 
				((clicked.getItemMeta().hasDisplayName()) ? clicked.getItemMeta().getDisplayName() : clicked.getType().toString())
				: clicked.getType().toString(), 
				plugin.getUtility().convertItemStackToJson(clicked), plugin.getUtility().toBase64itemStack(clicked));
		plugin.getMysqlHandler().create(Type.ITEMJSON, ij);
	}
	
	@EventHandler
	public void onUpperGui(UpperGuiClickEvent event)
	{
		if(!event.getPluginName().equals(GuiValues.PLUGINNAME))
		{
			return;
		}
		if(event.getInventoryIdentifier().equals(GuiValues.ITEM_REPLACER_INVENTORY))
		{
			itemReplacerHandling(event);
		}
	}
	
	private void itemReplacerHandling(UpperGuiClickEvent event)
	{
		final int slot = event.getEvent().getSlot();
		Inventory inv = event.getEvent().getView().getTopInventory();
		inv.setItem(slot, null);
		ArrayList<ItemJson> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getAllListAt(Type.ITEMJSON, "`id`", false, "`owner` = ? AND `itemname` != ?",
						event.getEvent().getWhoClicked().getUniqueId().toString(), "default"));
		ItemJson ij = list.get(slot);
		plugin.getMysqlHandler().deleteData(Type.ITEMJSON, "`owner` = ? AND `itemname` = ?",
				ij.getOwner(), ij.getItemName());
	}
}
