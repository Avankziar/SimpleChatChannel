package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;

public class ARGPermanentChannelCreate extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelCreate(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = "CmdScc.";		
		String name = null;
		String password = null;
		if(args.length==2)
		{
			name = plugin.getUtility().removeColor(args[1]);
		} else if(args.length==3)
		{
			name = plugin.getUtility().removeColor(args[1]);
			password = args[2];
		}
		PermanentChannel check = PermanentChannel.getChannelFromName(name);
		if(check != null)
		{
			///&cDieser Name für &5perma&fnenten &cChannels ist schon vergeben!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCCreate.ChannelNameAlreadyExist")));
			return;
		}
		int amount = 0;
		for(PermanentChannel c : PermanentChannel.getPermanentChannel())
		{
			if(c.getCreator().equals(player.getUniqueId().toString()))
			{
				amount++;
			}
		}
		if(plugin.getYamlHandler().get().getInt(""PermanentChannel.AmountPerPlayer"") <= amount)
		{
			///&cDieser Name für &5perma&fnenten &cChannels ist schon vergeben!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"PCCreate.MaximumAmount")));
			return;
		}
		String symbol = name;
		check = PermanentChannel.getChannelFromSymbol(symbol);
		if(check != null)
		{
			for(int i = 1; i<1000; i++)
			{
				symbol = name+i;
				check = PermanentChannel.getChannelFromSymbol(symbol);
				if(check == null)
				{
					break;
				}
			}
		}
		String color = plugin.getYamlHandler().getL().getString("ChannelColor.Perma");
		ArrayList<String> members = new ArrayList<String>();
		members.add(player.getUniqueId().toString());
		PermanentChannel cc = new PermanentChannel(0, name, player.getUniqueId().toString(), new ArrayList<String>(), 
				members, password, new ArrayList<String>(),
				symbol, color, color);
		plugin.getMysqlHandler().create(MysqlHandler.Type.PERMANENTCHANNEL, cc);
		int last = plugin.getMysqlHandler().lastID(MysqlHandler.Type.PERMANENTCHANNEL);
		cc.setId(last);
		PermanentChannel.addCustomChannel(cc);
		if(password==null)
		{
			///Du hast den permanenten Channel %channel% erstellt!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCCreate.ChannelCreateWithoutPassword")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%symbol%", plugin.getYamlHandler().getSymbol("Perma")+symbol)));
		} else
		{
			///Du hast den permanenten Channel %channel% mit dem Passwort %password% erstellt!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCCreate.ChannelCreateWithPassword")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%password%", password)
					.replace("%symbol%", plugin.getYamlHandler().getSymbol("Perma")+symbol)));
		}
		return;
	}
}