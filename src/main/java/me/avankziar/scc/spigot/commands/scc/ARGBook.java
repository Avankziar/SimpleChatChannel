package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ItemJson;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.BypassPermission;

public class ARGBook extends ArgumentModule
{
	private SCC plugin;
	
	public ARGBook(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String bookname = args[1];
		String owner = player.getUniqueId().toString();
		if(args.length >= 3 && player.hasPermission(BypassPermission.PERMBYPASSBOOK))
		{
			UUID uuid = Utility.convertNameToUUID(args[2]);
			if(uuid != null)
			{
				owner = uuid.toString();
			}
		}
		ItemJson ij = (ItemJson) plugin.getMysqlHandler().getData(MysqlType.ITEMJSON, "`owner` = ? AND `itemname` = ?",
				owner, bookname);
		if(ij == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Book.IsNotABook")));
			return;
		}
		ItemStack is = plugin.getUtility().fromBase64itemStack(ij.getBase64Data());
		if(is == null || is.getType() != Material.WRITTEN_BOOK)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Book.IsNotABook")));
			return;
		}
		player.openBook(is);
	}
}