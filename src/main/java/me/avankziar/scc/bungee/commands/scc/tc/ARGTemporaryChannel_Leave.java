package main.java.me.avankziar.scc.bungee.commands.scc.tc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannel_Leave extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Leave(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		final String name = cc.getName();
		cc.removeMembers(player);
		if(cc.getCreator().getName().equals(player.getName()))
		{
			ProxiedPlayer newcreator = null;
			for(ProxiedPlayer pp : cc.getMembers())
			{
				if(pp!=null)
				{
					newcreator = pp;
				}
			}
			if(newcreator!=null)
			{
				cc.setCreator(newcreator);
    			newcreator.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Leave.NewCreator")
    					.replace("%channel%", cc.getName())));
			} else 
			{
				TemporaryChannel.removeCustomChannel(cc);
				cc = null;
			}
			
		}
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Leave.YouLeft")
				.replace("%channel%", name)));
	}
}
