package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.ItemJson;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.guihandling.GUIApi;
import main.java.me.avankziar.scc.spigot.guihandling.GuiValues;
import main.java.me.avankziar.scc.spigot.objects.BypassPermission;

public class ARGItem extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGItem(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		ArrayList<ItemJson> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getAllListAt(Type.ITEMJSON, "`id`", false, 
						"`owner` = ? AND `itemname` != ?",
						player.getUniqueId().toString(), "default"));
		int rows = getMaxRows(player);
		if(rows == 0)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.YouCannotSaveItems")));
			return;
		}
		GUIApi gapi = new GUIApi(GuiValues.PLUGINNAME, GuiValues.ITEM_REPLACER_INVENTORY, 
				null, rows, plugin.getYamlHandler().getLang().getString("CmdScc.Item.InvTitle")
				.replace("%player%", player.getName()));
		int i = 0;
		for(ItemJson ij : list)
		{
			if(i >= (rows*9))
			{
				plugin.getMysqlHandler().deleteData(Type.ITEMJSON, "`owner` = ? AND `itemname` = ?",
						ij.getOwner(), ij.getItemName());
				continue;
			}
			ItemStack itemstack = plugin.getUtility().fromBase64itemStack(ij.getBase64Data());
			gapi.add(i, itemstack, GuiValues.ITEM_REPLACER_FUNCTION, GUIApi.SettingsLevel.NOLEVEL, true, null);
			i++;
		}
		GUIApi.addInGui(player.getUniqueId().toString(), GuiValues.ITEM_REPLACER_INVENTORY);
		gapi.open(player);
	}
	
	private int getMaxRows(Player player)
	{
		for(int i = 6; i > 0; i--)
		{
			if(player.hasPermission(BypassPermission.CUSTOM_ITEMREPLACERSTORAGE+i))
			{
				return i;
			}
		}
		return 0;
	}
}