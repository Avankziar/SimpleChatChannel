package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.IOException;
import java.util.LinkedHashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.YamlManager.GuiType;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.ItemGenerator;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.guihandling.GUIApi;
import main.java.me.avankziar.scc.spigot.guihandling.GUIApi.SettingsLevel;
import main.java.me.avankziar.scc.spigot.guihandling.GuiValues;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;

public class ARGChannelGui extends ArgumentModule
{
	private SCC plugin;
	
	public ARGChannelGui(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		openChannelGui(plugin, player);
	}
	
	public static void openChannelGui(SCC plugin, final Player player) throws IOException
	{
		int rows = plugin.getYamlHandler().getConfig().getInt("Gui.Channels.RowAmount", 6);
		if(rows > 6 || rows < 1)
		{
			rows = 6;
		}
		String title = plugin.getYamlHandler().getLang().getString("CmdScc.ChannelGui.InvTitle")
				.replace("%player%", player.getName());
		GUIApi gapi = new GUIApi(GuiValues.PLUGINNAME, GuiValues.CHANNELGUI_INVENTORY, 
				null, rows, title);
		LinkedHashMap<String, UsedChannel> usedChannels = ChatUserHandler.getUsedChannels(player.getUniqueId());
		if(usedChannels == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("GeneralError")));
			return;
		}
		final GuiType gt = GuiType.CHANNELS;
		final SettingsLevel sl = SettingsLevel.NOLEVEL;
		for(String key : plugin.getYamlHandler().getGui(gt.toString()).getRoutesAsStrings(false))
		{
			String[] f = key.split("_");
			if(f.length != 3)
			{
				SCC.logger.info("Gui cannot add ItemStack, because "+key+" has more than 3 parts!");
				continue;
			}
			String function = f[2];
			UsedChannel uc = usedChannels.get(function);
			if(uc == null)
			{
				SCC.logger.info("Gui cannot add ItemStack, because "+key+"/"+function+" is not know channel");
				continue;
			}
			ItemStack itemstack = ItemGenerator.create(key, plugin.getYamlHandler().getGui(gt.toString()),
					gt, false, sl, uc.isUsed());
			int slot = plugin.getYamlHandler().getGui(gt.toString())
					.getInt(key+"."+sl.toString()+".Slot");
			gapi.add(slot, itemstack, GuiValues.CHANNELGUI_FUNCTION+"_"+function, sl, true, null);
		}
		GUIApi.addInGui(player.getUniqueId().toString(), GuiValues.CHANNELGUI_INVENTORY);
		gapi.open(player);
	}
}