package main.java.me.avankziar.scc.velocity.commands.scc.tc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;

public class ARGTemporaryChannel_Leave extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Leave(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		final String name = cc.getName();
		cc.removeMembers(player);
		if(cc.getCreator().getUsername().equals(player.getUsername()))
		{
			Player newcreator = null;
			for(Player pp : cc.getMembers())
			{
				if(pp!=null)
				{
					newcreator = pp;
				}
			}
			if(newcreator!=null)
			{
				cc.setCreator(newcreator);
    			newcreator.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Leave.NewCreator")
    					.replace("%channel%", cc.getName())));
			} else 
			{
				TemporaryChannel.removeCustomChannel(cc);
				cc = null;
			}
			
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Leave.YouLeft")
				.replace("%channel%", name)));
	}
}
