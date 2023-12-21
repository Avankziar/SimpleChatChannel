package main.java.me.avankziar.scc.spigot.guihandling;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.ItemJson;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGChannel;
import main.java.me.avankziar.scc.spigot.commands.scc.ARGChannelGui;
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
		ItemStack iij = event.getEvent().getCurrentItem().clone();
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
		clicked = GUIApi.recreate(clicked, GuiValues.PLUGINNAME, GuiValues.ITEM_REPLACER_INVENTORY,
				GuiValues.ITEM_REPLACER_FUNCTION, GUIApi.SettingsLevel.NOLEVEL, true, null);
		inv.addItem(clicked);
		int count = plugin.getMysqlHandler().lastID(Type.ITEMJSON)+1;
		String name = "";
		if(iij.getType() == Material.WRITTEN_BOOK
				&& iij.getItemMeta() instanceof BookMeta)
		{
			BookMeta bm = (BookMeta) iij.getItemMeta();
			name = bm.getTitle();
		} else
		{
			name = (iij.hasItemMeta()) ? 
					((iij.getItemMeta().hasDisplayName()) ? iij.getItemMeta().getDisplayName() : iij.getType().toString())
					: iij.getType().toString();
		}
		ItemJson ij = new ItemJson(player.getUniqueId().toString(), 
				String.valueOf(count),
				name, 
				plugin.getUtility().convertItemStackToJson(iij),
				plugin.getUtility().toBase64itemStack(iij));
		plugin.getMysqlHandler().create(Type.ITEMJSON, ij);
	}
	
	@EventHandler
	public void onUpperGui(UpperGuiClickEvent event) throws IOException
	{
		if(!event.getPluginName().equals(GuiValues.PLUGINNAME))
		{
			
			return;
		}
		if(event.getInventoryIdentifier().equals(GuiValues.ITEM_REPLACER_INVENTORY))
		{
			itemReplacerHandling(event);
		} else if(event.getInventoryIdentifier().equals(GuiValues.CHANNELGUI_INVENTORY))
		{
			channelGuiHandling(event);
		}
	}
	
	private void itemReplacerHandling(UpperGuiClickEvent event)
	{
		if(event.getFunction().equals(GuiValues.ITEM_REPLACER_FUNCTION))
		{
			final int slot = event.getEvent().getSlot();
			Inventory inv = event.getEvent().getView().getTopInventory();
			ArrayList<ItemJson> list = ConvertHandler.convertListIV(
					plugin.getMysqlHandler().getAllListAt(Type.ITEMJSON, "`id`", false, "`owner` = ? AND `itemname` != ?",
							event.getEvent().getWhoClicked().getUniqueId().toString(), "default"));
			if(list.size() < slot+1)
			{
				return;
			}
			inv.setItem(slot, null);
			ItemJson ij = list.get(slot);
			plugin.getMysqlHandler().deleteData(Type.ITEMJSON, "`owner` = ? AND `itemname` = ?",
					ij.getOwner(), ij.getItemName());
		}
	}
	
	private void channelGuiHandling(UpperGuiClickEvent event) throws IOException
	{
		if(event.getFunction().startsWith(GuiValues.CHANNELGUI_FUNCTION)
				&& event.getFunction().contains("_"))
		{
			String[] f = event.getFunction().split("_");
			if(f.length != 3)
			{
				return;
			}
			String channel = f[2];
			boolean boo = ARGChannel.updateUsedChannel(plugin, (Player) event.getEvent().getWhoClicked(), channel);
			if(!boo)
			{
				event.getEvent().getWhoClicked().closeInventory();
				return;
			}
			ARGChannelGui.openChannelGui(plugin, (Player) event.getEvent().getWhoClicked());
		}
	}
}