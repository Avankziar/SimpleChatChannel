package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.ItemJson;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGItem_Replacers extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGItem_Replacers(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
						"`owner` = ?",
						player.getUniqueId().toString()));
		if(list.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Replacers.ListEmpty")));
			return;
		}
		ArrayList<BaseComponent> abc = new ArrayList<>();
		for(int i = 0; i < list.size(); i++)
		{
			ItemJson ij = list.get(i);
			abc.add(ChatApi.apiChatItem(ij.getItemDisplayName(), null, null, ij.getJsonString()));
			if(i < (list.size()-1))
			{
				abc.add(ChatApi.tctl(" "));
			}
		}
		TextComponent tc = ChatApi.tc("");
		tc.setExtra(abc);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Replacers.Headline")));
		player.spigot().sendMessage(tc);
	}
}