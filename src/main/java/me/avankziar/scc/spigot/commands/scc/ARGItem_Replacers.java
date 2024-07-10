package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.ItemJson;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class ARGItem_Replacers extends ArgumentModule
{
	private SCC plugin;
	
	public ARGItem_Replacers(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		ArrayList<ItemJson> list = ConvertHandler.convertListIV(
				plugin.getMysqlHandler().getFullList(MysqlType.ITEMJSON, "`id` ASC", 
						"`owner` = ?",
						player.getUniqueId().toString()));
		if(list.isEmpty())
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Replacers.ListEmpty")));
			return;
		}
		ArrayList<String> abc = new ArrayList<>();
		for(int i = 0; i < list.size(); i++)
		{
			ItemJson ij = list.get(i);
			abc.add(ChatApi.hover("<gray><"+ij.getItemName()+"<gray>><white>"+ij.getItemDisplayName(), ij.getJsonString()));
			if(i < (list.size()-1))
			{
				abc.add("<white>, ");
			}
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Replacers.Headline")));
		player.spigot().sendMessage(ChatApi.tctl(String.join("", abc)));
	}
}