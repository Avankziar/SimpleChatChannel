package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import java.util.ArrayList;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGPermanentChannel_Create extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Create(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String prename = args[2];		
		String name = null;
		String password = null;
		if(args.length == 3)
		{
			name = plugin.getUtility().removeColor(prename);
		} else if(args.length == 4)
		{
			name = plugin.getUtility().removeColor(prename);
			password = args[3];
		}
		PermanentChannel check = PermanentChannel.getChannelFromName(name);
		if(check != null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Create.ChannelNameAlreadyExist")));
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
		if(plugin.getYamlHandler().getConfig().getInt("PermanentChannel.AmountPerPlayer") <= amount)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Create.MaximumAmount")));
			return;
		}
		String symbol = name;
		check = PermanentChannel.getChannelFromSymbol(symbol);
		if(check != null)
		{
			for(int i = 1; i < 1000; i++)
			{
				symbol = name+i;
				check = PermanentChannel.getChannelFromSymbol(symbol);
				if(check == null)
				{
					break;
				}
			}
		}
		Channel perm = plugin.getChannel("Permanent");
		String color = "&f";
		ArrayList<String> members = new ArrayList<String>();
		members.add(player.getUniqueId().toString());
		PermanentChannel cc = new PermanentChannel(0, name, player.getUniqueId().toString(), new ArrayList<String>(), 
				members, password, new ArrayList<String>(),
				symbol, color, color);
		plugin.getMysqlHandler().create(MysqlType.PERMANENTCHANNEL, cc);
		int last = plugin.getMysqlHandler().lastID(MysqlType.PERMANENTCHANNEL);
		cc.setId(last);
		PermanentChannel.addCustomChannel(cc);
		if(password == null)
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Create.ChannelCreateWithoutPassword")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%symbol%", (perm != null ? perm.getSymbol() : "")+symbol)));
		} else
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Create.ChannelCreateWithPassword")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%password%", password)
					.replace("%symbol%", (perm != null ? perm.getSymbol() : "")+symbol)));
		}
	}
}